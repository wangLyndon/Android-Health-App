package com.example.healthapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthapp.R;
import com.example.healthapp.database.AppDatabase;
import com.example.healthapp.entities.Note;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNote extends AppCompatActivity {
    private AppDatabase db;

    private Note selectNote;
    private boolean isUpdating;

    private TextView noteDate;
    private TextView noteTitle;
    private TextView noteContent;
    private ImageView save;
    private TextView deleteNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);

        db = AppDatabase.getInstance(this);

        noteDate = findViewById(R.id.noteDate);
        noteTitle = findViewById(R.id.noteTitle);
        noteContent = findViewById(R.id.noteContent);

        noteDate.setText(getCurrentDay());

        save = findViewById(R.id.save);
        deleteNote = findViewById(R.id.deleteNote);

        isUpdating = false;

        if (getIntent().getStringExtra("type").equals("update")){
            selectNote = (Note) getIntent().getSerializableExtra("note");
            isUpdating = true;
            updateNote();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    private void saveNote(){
        if (isUpdating){
            selectNote.setTitle(noteTitle.getText().toString());
            selectNote.setDate(noteDate.getText().toString());
            selectNote.setContent(noteContent.getText().toString());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.noteDao().update(selectNote);
                }
            }).start();
        }else{
            Note note = new Note(noteTitle.getText().toString(), noteDate.getText().toString(), noteContent.getText().toString());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    db.noteDao().insert(note);
                }
            }).start();
        }

        finish();
    }

    private void updateNote(){
        // To fill the edit areas
        noteTitle.setText(selectNote.getTitle());
        noteDate.setText(selectNote.getDate());
        noteContent.setText(selectNote.getContent());

        // To let the note can be deleted
        deleteNote.setVisibility(View.VISIBLE);

        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNote();
            }
        });
    }

    private void deleteNote(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.noteDao().delete(selectNote);
            }
        }).start();
        finish();
    }

    private String getCurrentDay(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}