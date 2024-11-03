package com.example.mis_java_project.list;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mis_java_project.MediaItemRepository;
import com.example.mis_java_project.data.model.MediaItem;

import java.util.List;

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
        new Thread(() -> {
            mediaItemRepository.insert(mediaItem).thenRun(this::loadMediaItems);
        }).start();
    }

    public void update(MediaItem mediaItem) {
        new Thread(() -> {
            mediaItemRepository.update(mediaItem);
            loadMediaItems();
        }).start();
    }

    public void delete(MediaItem mediaItem) {
        new Thread(() -> {
            mediaItemRepository.delete(mediaItem);
            loadMediaItems();
        }).start();
    }

    private void loadMediaItems() {
        new Thread(() -> {
            var mediaItems = mediaItemRepository.getAllMediaItems();

            uiState.postValue(new ListViewUiState(mediaItems));
        }).start();
    }


    public record ListViewUiState(List<MediaItem> mediaItemList) {
    }

}
