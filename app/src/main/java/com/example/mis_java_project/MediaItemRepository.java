package com.example.mis_java_project;

import android.app.Application;

import com.example.mis_java_project.data.database.LocalAppDatabase;
import com.example.mis_java_project.data.database.MediaItemDao;
import com.example.mis_java_project.data.model.MediaItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MediaItemRepository {
    private final MediaItemDao mediaItemDao;

    public MediaItemRepository(Application application) {
        LocalAppDatabase db = LocalAppDatabase.getInstance(application);
        mediaItemDao = db.mediaItemDao();
    }

    public List<MediaItem> getAllMediaItems() {
        return mediaItemDao.getAllMediaItems();
    }

    public CompletableFuture<Void> insert(MediaItem mediaItem) {
        return CompletableFuture.runAsync(() -> {
            mediaItemDao.insert(mediaItem);
        });
    }

    public CompletableFuture<Void> update(MediaItem mediaItem) {
        return CompletableFuture.runAsync(() -> {
            mediaItemDao.update(mediaItem);
        });
    }

    public CompletableFuture<Void> delete(MediaItem mediaItem) {
        return CompletableFuture.runAsync(() -> {
            mediaItemDao.delete(mediaItem);
        });
    }

    public CompletableFuture<Void> deleteAllItems() {
        return CompletableFuture.runAsync(mediaItemDao::deleteAllMediaItems);
    }
}
