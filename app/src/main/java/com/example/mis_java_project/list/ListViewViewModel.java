package com.example.mis_java_project.list;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.mis_java_project.MediaItemRepository;
import com.example.mis_java_project.data.model.MediaItem;

import java.util.ArrayList;

public class ListViewViewModel extends AndroidViewModel {

    private final MediatorLiveData<ListViewUiState> uiState = new MediatorLiveData<>();

    public LiveData<ListViewUiState> uiState() {
        return uiState;
    }

    public ListViewViewModel(Application application) {
        super(application);
        uiState.setValue(new ListViewUiState(new ArrayList<>(), null, false));

        //Observe MediaItemRepository and merge changes of list
        MediaItemRepository mediaItemRepository = MediaItemRepository.getInstance(application);
        uiState.addSource(mediaItemRepository.mediaItems, mediaItems -> {
            //resets the showDialog automatically if actions to repository happend
            uiState.setValue(uiState.getValue().copy(mediaItems, null, false));
        });
    }

    public void onSelectItem(MediaItem mediaItem) {
        if (uiState.getValue() != null) {
            uiState.setValue(uiState.getValue().copy(null, mediaItem, true));
        }
    }

    public void onAddIconClicked() {
        if (uiState.getValue() != null) {
            uiState.setValue(uiState.getValue().copy(null, null, true));
        }
    }
}
