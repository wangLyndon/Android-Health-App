package com.example.healthapp.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthapp.R;

public class SportDetails extends AppCompatActivity {

    private TextView sportName;
    private ImageView sportImg;
    private TextView sportDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sport_details);

        sportName = findViewById(R.id.sportName);
        sportImg = findViewById(R.id.sportImg);
        sportDescription = findViewById(R.id.sportDescription);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            sportName.setText(bundle.getString("name"));
            sportImg.setImageResource(bundle.getInt("image"));
            sportDescription.setText(bundle.getString("desc"));
        }
    }
}