package com.example.healthapp.fragments;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthapp.R;
import com.example.healthapp.activities.ChangeFrag;
import com.example.healthapp.activities.StepHistory;
import com.example.healthapp.database.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class StepFragment extends Fragment implements SensorEventListener {
    private AppDatabase db;
    private SensorManager sensorManager;
    private Sensor sensor;
    private int totalSteps = -1;
    private int prevSteps = 0;
    private ProgressBar progressBar;
    private TextView steps;
    private TextView hintInfo;
    private TextView change;
    private SharedPreferences stepSF;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        db = AppDatabase.getInstance(getContext());

        progressBar = view.findViewById(R.id.progressbar);
        steps = view.findViewById(R.id.steps);
        hintInfo = view.findViewById(R.id.hintText);
        change = view.findViewById(R.id.change);

        initialize();

        sensorManager = (SensorManager) requireContext().getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // see history of steps
        view.findViewById(R.id.stepHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StepHistory.class);
                startActivity(intent);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeFrag.class);
                intent.putExtra("fragName", "Step");
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            totalSteps = (int) sensorEvent.values[0];
            String lastUpdates = stepSF.getString("date", "");

            if (lastUpdates.equals(getCurrentDay())){
                int currentStep = totalSteps - prevSteps;
                steps.setText(String.valueOf(currentStep));
                progressBar.setProgress(currentStep);
                updateHint(currentStep);
            }else{
                saveData();
                prevSteps = totalSteps;
                steps.setText("0");
                progressBar.setProgress(0);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void initialize(){
        // Room operation is too slow, so it is changed to SharedPreferences

        stepSF = getActivity().getSharedPreferences("Steps", Context.MODE_PRIVATE);
        prevSteps = stepSF.getInt("prev", 0);
    }

    private void saveData() {
        SharedPreferences.Editor edit = stepSF.edit();

        // Used to store last week's step counts
        edit.putInt("before7Days", stepSF.getInt("before6Days", 0));
        edit.putInt("before6Days", stepSF.getInt("before5Days", 0));
        edit.putInt("before5Days", stepSF.getInt("before4Days", 0));
        edit.putInt("before4Days", stepSF.getInt("before3Days", 0));
        edit.putInt("before3Days", stepSF.getInt("before2Days", 0));
        edit.putInt("before2Days", stepSF.getInt("yesterday", 0));

        if (stepSF.getString("date", "").isEmpty()){
            // When the user runs the app for the first time, the number of steps taken yesterday needs to be set to 0
            edit.putInt("yesterday", 0);
        }else{
            edit.putInt("yesterday", totalSteps - prevSteps);
        }

        edit.putInt("prev", totalSteps);
        edit.putString("date", getCurrentDay());
        edit.apply();
    }


    private String getCurrentDay(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private void updateHint(int currentSteps){
        if (currentSteps < 2500){
            hintInfo.setText("Keep working hard, it’s almost a quarter!");
        }else if (currentSteps >= 2500 && currentSteps < 5000){
            hintInfo.setText("Great! You have already walke " + currentSteps + " steps. Keep going!");
        } else if (currentSteps >= 5000 && currentSteps < 7500) {
            hintInfo.setText("You have already walked halfway. You have " + currentSteps + " steps!");
        } else if (currentSteps >= 7500 && currentSteps < 10000) {
            hintInfo.setText("The achievement of " + currentSteps + " steps is right under your feet.");
        } else {
            hintInfo.setText("Congratulations on reaching the goal of " + currentSteps + " steps!");
        }
    }
}