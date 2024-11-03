package com.example.mis_java_project.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "media_items")
public class MediaItem {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String imageSource;
    private long creationDate;

    public MediaItem(String title, String imageSource, long creationDate) {
        this.title = title;
        this.imageSource = imageSource;
        this.creationDate = creationDate;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getImageSource() {
        return imageSource;
    }

    public long getCreationDate() {
        return creationDate;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}