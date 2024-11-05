package com.example.mis_java_project.dialog;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mis_java_project.MediaItemRepository;
import com.example.mis_java_project.data.model.MediaItem;

public class DialogViewViewModel extends AndroidViewModel {
    private final MediaItemRepository mediaItemRepository;

    private final MutableLiveData<DialogViewUiState> uiState = new MutableLiveData<>();

    public LiveData<DialogViewUiState> uiState() {
        return uiState;
    }

    public DialogViewViewModel(Application application) {
        super(application);
        uiState.setValue(new DialogViewUiState("", false, null));
        mediaItemRepository = MediaItemRepository.getInstance(application);
    }

    public void onDeleteMediaItem(MediaItem mediaItem) {
        mediaItemRepository.delete(mediaItem);
    }

    public void onSaveMediaItem(MediaItem mediaItem, String title) {
        if (uiState.getValue() != null) {
            if (mediaItem != null) {
                mediaItem.setTitle(title);
                mediaItemRepository.update(mediaItem);
            } else {
                MediaItem newItem = new MediaItem(title, "https://example.com/image.jpg", System.currentTimeMillis());
                mediaItemRepository.insert(newItem);
            }
        }
    }

}
