package com.example.healthapp.entities;

public class Sport {
    private String title;
    private int img;
    private String desc;

    public Sport(String title, int img, String desc) {
        this.title = title;
        this.img = img;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
