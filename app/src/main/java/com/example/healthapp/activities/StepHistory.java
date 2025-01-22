package com.example.healthapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthapp.R;

public class StepHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step_history);

        TextView yesterdaySteps = findViewById(R.id.yesterdaySteps);
        TextView weekSteps = findViewById(R.id.weekSteps);

        SharedPreferences stepSF = getSharedPreferences("Steps", Context.MODE_PRIVATE);

        yesterdaySteps.setText(String.valueOf(stepSF.getInt("yesterday", 0)));

        int totalStepOfWeek = stepSF.getInt("before7Days", 0) +
                stepSF.getInt("before6Days", 0) +
                stepSF.getInt("before5Days", 0) +
                stepSF.getInt("before4Days", 0) +
                stepSF.getInt("before3Days", 0) +
                stepSF.getInt("before2Days", 0) +
                stepSF.getInt("yesterday", 0);

        weekSteps.setText(String.valueOf(totalStepOfWeek));
    }
}