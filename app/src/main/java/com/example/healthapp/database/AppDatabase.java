package com.example.healthapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.healthapp.dao.NoteDao;
import com.example.healthapp.dao.OptionDao;
import com.example.healthapp.entities.Note;
import com.example.healthapp.entities.Option;

@Database(entities = {Option.class, Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract OptionDao optionDao();
    public abstract NoteDao noteDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "health_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
