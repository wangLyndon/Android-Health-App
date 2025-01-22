package com.example.healthapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "options")
public class Option {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int img;
    private String name;
    private boolean isSelected;
    private int selectNum;

//    public Option(int img, String name, boolean isSelected) {
//        this.img = img;
//        this.name = name;
//        this.isSelected = isSelected;
//    }

    public Option(int img, String name, int selectNum) {
        this.img = img;
        this.name = name;
        this.selectNum = selectNum;
    }

    public int getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(int selectNum) {
        this.selectNum = selectNum;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { // Room 需要设置 ID
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
