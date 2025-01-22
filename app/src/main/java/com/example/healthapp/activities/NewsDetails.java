package com.example.healthapp.activities;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthapp.R;
import com.example.healthapp.entities.News;

public class NewsDetails extends AppCompatActivity {

    private TextView newsTitle;
    private TextView newsDate;
    private TextView newsDescription;
    private TextView newsContent;
    private TextView newsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_details);

        newsTitle = findViewById(R.id.newsTitle);
        newsDate = findViewById(R.id.newsDate);
        newsDescription = findViewById(R.id.newsDescription);
        newsContent = findViewById(R.id.newsContent);
        newsUrl = findViewById(R.id.newsUrl);

        News news = (News) getIntent().getSerializableExtra("news");

        newsTitle.setText(news.getTitle());
        newsDate.setText(news.getPublishedAt());
        newsDescription.setText(news.getDescription());
        newsContent.setText(news.getContent());
        newsUrl.setText(news.getUrl());
    }
}