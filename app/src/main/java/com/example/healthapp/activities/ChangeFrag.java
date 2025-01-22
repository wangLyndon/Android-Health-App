package com.example.healthapp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.adapter.OptionAdapter;
import com.example.healthapp.adapter.SportAdapter;
import com.example.healthapp.database.AppDatabase;
import com.example.healthapp.entities.Option;

import java.util.ArrayList;
import java.util.List;

public class ChangeFrag extends AppCompatActivity {
    private AppDatabase db;
    private RecyclerView recyclerView;
    private OptionAdapter optionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_frag);

        String fragName = getIntent().getStringExtra("fragName");

        db = AppDatabase.getInstance(this);

        recyclerView = findViewById(R.id.optionList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<Option> showOptions = new ArrayList<>();

        db.optionDao().getNotSelectedNum().observe(this, new Observer<List<Option>>() {
            @Override
            public void onChanged(List<Option> options) {
                showOptions.addAll(options);
            }
        });

        db.optionDao().getOptionByName(fragName).observe(this, new Observer<Option>() {
            @Override
            public void onChanged(Option option) {
                showOptions.add(option);

            }
        });

        optionAdapter = new OptionAdapter(getApplicationContext(), showOptions);
        recyclerView.setAdapter(optionAdapter);
    }
}