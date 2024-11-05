package com.example.mis_java_project;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mis_java_project.data.database.LocalAppDatabase;
import com.example.mis_java_project.data.database.MediaItemDao;
import com.example.mis_java_project.data.model.MediaItem;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * This MediaItemRepository should handle and hide logic for calling local and remote databases under the hood . ViewModels should only know what data they need independent of the origin
 */
public class MediaItemRepository {
    private final MediaItemDao mediaItemDao;
    public final LiveData<List<MediaItem>> mediaItems;

    private static MediaItemRepository INSTANCE;

    private MediaItemRepository(Application application) {
        LocalAppDatabase db = LocalAppDatabase.getInstance(application);
        mediaItemDao = db.mediaItemDao();
        mediaItems = mediaItemDao.getAllMediaItems();
    }

    public static MediaItemRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new MediaItemRepository(application);
        }

        return INSTANCE;
    }

    public void insert(MediaItem mediaItem) {
        Executors.newSingleThreadExecutor().execute(() -> {
            mediaItemDao.insert(mediaItem);
        });
    }

    public void update(MediaItem mediaItem) {
        Executors.newSingleThreadExecutor().execute(() -> {
            mediaItemDao.update(mediaItem);
        });
    }

    public void delete(MediaItem mediaItem) {
        Executors.newSingleThreadExecutor().execute(() -> {
            mediaItemDao.delete(mediaItem);
        });
    }
}
