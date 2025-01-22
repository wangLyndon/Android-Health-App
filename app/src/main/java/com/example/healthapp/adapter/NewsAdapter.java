package com.example.healthapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.activities.AddNote;
import com.example.healthapp.activities.NewsDetails;
import com.example.healthapp.entities.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    private Context context;
    private List<News> news;

    public NewsAdapter(Context context, List<News> news) {
        this.context = context;
        this.news = news;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News newsItem = news.get(position);

        String newsTitle = "";
        if (newsItem.getTitle().length() > 15){
            newsTitle = newsItem.getTitle().substring(0, 15) + "...";
        }else{
            newsTitle = newsItem.getTitle();
        }

        holder.title.setText(newsTitle);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetails.class);
                intent.putExtra("news", newsItem);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news.size();
    }
}

class NewsViewHolder extends RecyclerView.ViewHolder{
    LinearLayout linearLayout;
    TextView title;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);

        linearLayout = itemView.findViewById(R.id.newsItem);
        title = itemView.findViewById(R.id.newsTitle);
    }
}
