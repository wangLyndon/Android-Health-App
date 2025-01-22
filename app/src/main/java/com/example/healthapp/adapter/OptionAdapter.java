package com.example.healthapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.R;
import com.example.healthapp.database.AppDatabase;
import com.example.healthapp.entities.Option;

import java.util.List;
import java.util.logging.Handler;

public class OptionAdapter extends RecyclerView.Adapter<OptionViewHolder>{
    private Context context;
    private List<Option> options;

    public OptionAdapter(Context context, List<Option> options) {
        this.context = context;
        this.options = options;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_item, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        Option option = options.get(position);

        holder.name.setText(option.getName());
        holder.img.setImageResource(option.getImg());

        if (option.getSelectNum() != 0){
            holder.name.setTextColor(Color.BLUE);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First find the nav position
                Option lastSelected = null;
                int selectedNum = 0;

                for (Option opt : options) {
                    if (opt.getSelectNum() != 0){
                        selectedNum = opt.getSelectNum();
                        lastSelected = opt;
                    }
                }

                // Clear all options to 0
                for (Option opt : options) {
                    opt.setSelectNum(0);
                }

                holder.name.setTextColor(Color.RED);

                // Set the clicked position
                option.setSelectNum(selectedNum);

                AppDatabase db = AppDatabase.getInstance(context);
                // Make it to be final to avoid error
                Option finalLastSelected = lastSelected;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.optionDao().update(finalLastSelected);
                        db.optionDao().update(option);
                    }
                }).start();
            }
        });

    }

    @Override
    public int getItemCount() {
        return options.size();
    }
}

class OptionViewHolder extends RecyclerView.ViewHolder {
    LinearLayout linearLayout;
    TextView name;
    ImageView img;

    public OptionViewHolder(@NonNull View itemView) {
        super(itemView);

        linearLayout = itemView.findViewById(R.id.optionItem);
        name = itemView.findViewById(R.id.optionTitle);
        img = itemView.findViewById(R.id.img);
    }
}
