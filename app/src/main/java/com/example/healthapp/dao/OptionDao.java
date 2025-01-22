package com.example.healthapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthapp.entities.Option;

import java.util.List;

@Dao
public interface OptionDao {
    @Insert
    void insert(Option option);

    @Delete
    void delete(Option option);

    @Update
    void update(Option option);

    @Query("SELECT * FROM options")
    LiveData<List<Option>> getAllOptions();

    @Query("SELECT * FROM options WHERE isSelected = 1 LIMIT 1")
    LiveData<Option> getSelectedOption();

    @Query("SELECT * FROM options WHERE selectNum = 1 LIMIT 1")
    LiveData<Option> getSelectedNum1();

    @Query("SELECT * FROM options WHERE selectNum = 2 LIMIT 1")
    LiveData<Option> getSelectedNum2();

    @Query("SELECT * FROM options WHERE selectNum = 3 LIMIT 1")
    LiveData<Option> getSelectedNum3();

    @Query("SELECT * FROM options WHERE selectNum = 0")
    LiveData<List<Option>> getNotSelectedNum();

    // Find Option by name
    @Query("SELECT * FROM options WHERE name = :optionName LIMIT 1")
    LiveData<Option> getOptionByName(String optionName);
}
