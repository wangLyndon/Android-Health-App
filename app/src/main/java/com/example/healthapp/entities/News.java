package com.example.healthapp.entities;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private String description;
    private String content;
    private String publishedAt;
    private String url;

    public News(String title, String description, String content, String publishedAt, String url) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.publishedAt = publishedAt;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
