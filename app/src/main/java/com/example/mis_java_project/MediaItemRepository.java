package com.example.mis_java_project;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.mis_java_project.data.database.LocalAppDatabase;
import com.example.mis_java_project.data.database.MediaItemDao;
import com.example.mis_java_project.data.model.MediaItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * This MediaItemRepository should handle and hide logic for calling local and remote databases under the hood . ViewModels should only know what data they need independent of the origin
 */
public class MediaItemRepository {
    private final MediaItemDao mediaItemDao;
    public final LiveData<List<MediaItem>> mediaItems;

    private static MediaItemRepository INSTANCE;

    private Context context;

    private MediaItemRepository(Application application) {
        LocalAppDatabase db = LocalAppDatabase.getInstance(application);
        this.context = application.getApplicationContext();
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
            // Convert the temporary Uri to a permanent internal storage Uri

            // Save the image from temp Uri to the permanent internal file
            Uri uri = saveFileFromUri(mediaItem.getImageUri());
            if (uri != null) {
                // After saving, update the mediaItem with the permanent Uri (internal storage)
                mediaItem.setImageUri(uri);
                mediaItemDao.insert(mediaItem);
            } else {
                // Handle failure (e.g., log an error, show a message to the user)
                Log.e("MediaItemRepo", "Failed to save image from Uri: " + mediaItem.getImageSource());
            }
        });
    }

    public void update(MediaItem mediaItem) {
        Executors.newSingleThreadExecutor().execute(() -> {
            MediaItem oldMediaItem = mediaItems.getValue().stream().filter(item -> item.getId() == mediaItem.getId()).findFirst().orElse(null);

            if (oldMediaItem != null && oldMediaItem.getImageUri() != null && !oldMediaItem.getImageSource().equals(mediaItem.getImageSource())) {
                deleteFileFromUri(oldMediaItem.getImageUri());
                Uri uri = saveFileFromUri(mediaItem.getImageUri());
                if (uri != null) {
                    // After saving, update the mediaItem with the permanent Uri (internal storage)
                    mediaItem.setImageUri(uri);
                } else {
                    // Handle failure (e.g., log an error, show a message to the user)
                    Log.e("MediaItemRepo", "Failed to update image from Uri: " + mediaItem.getImageSource());
                }
            }
            mediaItemDao.update(mediaItem);
        });
    }

    public void delete(MediaItem mediaItem) {
        Executors.newSingleThreadExecutor().execute(() -> {

            boolean deleteSuccess = deleteFileFromUri(mediaItem.getImageUri());
            if (deleteSuccess) {
                mediaItemDao.delete(mediaItem);
            } else {
                Log.e("MediaItemRepo", "Failed to delete image from Uri: " + mediaItem.getImageSource());
            }
        });
    }

    // Save URI to internal storage (this method contains the file-related logic)
    private Uri saveFileFromUri(Uri sourceUri) {
        try {
            File destinationFile = new File(context.getFilesDir(), "media_" + System.currentTimeMillis() + ".jpg");

            InputStream inputStream = context.getContentResolver().openInputStream(sourceUri);
            OutputStream outputStream = new FileOutputStream(destinationFile);

            if (inputStream != null && outputStream != null) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();
                return Uri.fromFile(destinationFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean deleteFileFromUri(Uri sourceUri) {
        if (sourceUri.getPath() != null) {
            File oldFile = new File(sourceUri.getPath());
            if (oldFile.exists()) {
                return oldFile.delete();
            }
        }
        return false;
    }
}