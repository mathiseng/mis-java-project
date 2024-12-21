package com.example.mis_java_project.data.model;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "media_items")
public class MediaItem {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String imageSource;
    private long creationDate;

    private StorageOption storageOption;

    public MediaItem(String title, String imageSource, long creationDate, StorageOption storageOption) {
        this.title = title;
        this.imageSource = imageSource;
        this.creationDate = creationDate;
        this.storageOption = storageOption;
    }

    public MediaItem(MediaItem mediaItem) {
        this.id = mediaItem.id;
        this.title = mediaItem.title;
        this.imageSource = mediaItem.imageSource;
        this.creationDate = mediaItem.creationDate;
        this.storageOption = mediaItem.storageOption;
    }


    // Getters
    public String getTitle() {
        return title;
    }

    public String getImageSource() {
        return imageSource;
    }

    public StorageOption getStorageOption() {
        return storageOption;
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

    public void setStorageOption(StorageOption storageOption) {
        this.storageOption = storageOption;
    }

    public void setImageUri(Uri imageSource) {
        this.imageSource = imageSource.toString();
    }

    public Uri getImageUri() {
        return Uri.parse(imageSource);
    }
}