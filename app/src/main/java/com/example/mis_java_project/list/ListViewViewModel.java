package com.example.mis_java_project.list;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mis_java_project.MediaItemRepository;
import com.example.mis_java_project.data.model.MediaItem;

import java.util.concurrent.CompletableFuture;

public class ListViewViewModel extends AndroidViewModel {
    private final MediaItemRepository mediaItemRepository;

    private final MutableLiveData<ListViewUiState> uiState = new MutableLiveData<>();

    public LiveData<ListViewUiState> uiState() {
        return uiState;
    }

    public ListViewViewModel(Application application) {
        super(application);
        mediaItemRepository = new MediaItemRepository(application);
        loadMediaItems();
    }

    public void insert(MediaItem mediaItem) {
        mediaItemRepository.insert(mediaItem).thenRun(this::loadMediaItems);
    }

    public void update(MediaItem mediaItem) {
        mediaItemRepository.update(mediaItem).thenRun(this::loadMediaItems);
    }

    public void delete(MediaItem mediaItem) {
        mediaItemRepository.delete(mediaItem).thenRun(this::loadMediaItems);
    }

    public void onSelectItem(MediaItem mediaItem) {
        if (uiState.getValue() != null) {
            uiState.setValue(uiState.getValue().copy(null, mediaItem, true));
        }
    }

    public void onDialogFinished() {
        if (uiState.getValue() != null) {
            uiState.setValue(uiState.getValue().copy(null, null, false));
        }
    }


    public void onAddIconClicked() {
        if (uiState.getValue() != null) {
            uiState.setValue(uiState.getValue().copy(null, null, true));
        }
    }

    public void onSaveMediaItem(MediaItem mediaItem, String title) {
        if (uiState.getValue() != null) {
            CompletableFuture<Void> action;
            if (mediaItem != null) {
                mediaItem.setTitle(title);
                action = mediaItemRepository.update(mediaItem);
            } else {
                MediaItem newItem = new MediaItem(title, "https://example.com/image.jpg", System.currentTimeMillis());
                action = mediaItemRepository.insert(newItem);
            }
            action.thenRun(this::loadMediaItems);
        }
    }


    private void loadMediaItems() {
        new Thread(() -> {
            var mediaItems = mediaItemRepository.getAllMediaItems();

            uiState.postValue(new ListViewUiState(mediaItems, null, false));
        }).start();
    }

}
