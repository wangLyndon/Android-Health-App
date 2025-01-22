package com.example.healthapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthapp.R;
import com.example.healthapp.activities.AddNote;
import com.example.healthapp.activities.ChangeFrag;
import com.example.healthapp.adapter.NoteAdapter;
import com.example.healthapp.database.AppDatabase;
import com.example.healthapp.entities.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NoteFragment extends Fragment {
    private AppDatabase db;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        db = AppDatabase.getInstance(getActivity());

        recyclerView = view.findViewById(R.id.noteList);
        searchView = view.findViewById(R.id.searchNote);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchNote(s);
                return true;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        db.noteDao().getAllNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter = new NoteAdapter(getContext(), notes);
                recyclerView.setAdapter(noteAdapter);
            }
        });

        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNote.class);
                // To make the type is add
                intent.putExtra("type", "add");
                startActivity(intent);
            }
        });

        view.findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeFrag.class);
                intent.putExtra("fragName", "Note");
                startActivity(intent);
            }
        });

        return view;
    }

    private void searchNote(String text) {
        db.noteDao().getAllNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                List<Note> result = new ArrayList<>();

                for (Note note : notes) {
                    if (note.getTitle().toLowerCase().contains(text.toLowerCase()) ||
                    note.getContent().toLowerCase().contains(text.toLowerCase())){
                        result.add(note);
                    }
                }

                noteAdapter = new NoteAdapter(getContext(), result);
                recyclerView.setAdapter(noteAdapter);
            }
        });
    }
}