package com.example.healthapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.activities.SportDetails;
import com.example.healthapp.entities.Sport;

import java.util.List;

public class SportAdapter extends RecyclerView.Adapter<SportViewHolder> {
    private Context context;
    private List<Sport> sports;

    public SportAdapter(Context context, List<Sport> sports) {
        this.context = context;
        this.sports = sports;
    }

    @NonNull
    @Override
    public SportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sport_item, parent, false);
        return new SportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SportViewHolder holder, int position) {
        holder.name.setText(sports.get(position).getTitle());
        holder.img.setImageResource(sports.get(position).getImg());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SportDetails.class);
                intent.putExtra("image", sports.get(holder.getAdapterPosition()).getImg());
                intent.putExtra("name", sports.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("desc", sports.get(holder.getAdapterPosition()).getDesc());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sports.size();
    }
}

class SportViewHolder extends RecyclerView.ViewHolder {
    LinearLayout linearLayout;
    TextView name;
    ImageView img;

    public SportViewHolder(@NonNull View itemView) {
        super(itemView);

        linearLayout = itemView.findViewById(R.id.sportItem);
        name = itemView.findViewById(R.id.sportTitle);
        img = itemView.findViewById(R.id.img);
    }
}
