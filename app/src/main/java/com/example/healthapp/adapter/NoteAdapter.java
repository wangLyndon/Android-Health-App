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
import com.example.healthapp.activities.AddNote;
import com.example.healthapp.entities.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private Context context;
    private List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        String noteTitle = "";
        if (note.getTitle().length() > 15){
            noteTitle = note.getTitle().substring(0, 15) + "...";
        }else{
            noteTitle = note.getTitle();
        }

        holder.title.setText(noteTitle);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddNote.class);
                // To make the type is update
                intent.putExtra("type", "update");
                intent.putExtra("note", note);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}

class NoteViewHolder extends RecyclerView.ViewHolder{
    LinearLayout linearLayout;
    TextView title;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        linearLayout = itemView.findViewById(R.id.noteItem);
        title = itemView.findViewById(R.id.noteTitle);
    }
}
