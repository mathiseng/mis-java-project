package com.example.mis_java_project.list;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.mis_java_project.MediaItemRepository;
import com.example.mis_java_project.SharedStateRepository;
import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.details.DetailsActivity;

import java.util.ArrayList;

public class ListViewViewModel extends AndroidViewModel {

    private final MediatorLiveData<ListViewUiState> uiState = new MediatorLiveData<>();

    public LiveData<ListViewUiState> uiState() {
        return uiState;
    }

    private final SharedStateRepository sharedStateRepository;

    Context context;

    public ListViewViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
        uiState.setValue(new ListViewUiState(new ArrayList<>(), null, false, false));

        //Observe MediaItemRepository and merge changes of list
        MediaItemRepository mediaItemRepository = MediaItemRepository.getInstance(application);
        sharedStateRepository = SharedStateRepository.getInstance();
        uiState.addSource(mediaItemRepository.mediaItems, mediaItems -> {
            //resets the showDialog automatically if actions to repository happend
            uiState.setValue(uiState.getValue().copy(mediaItems, null, false, false));
            sharedStateRepository.onChangeSelectedMediaItem(null);
        });

        uiState.addSource(sharedStateRepository.selectedItem, mediaItem -> {
            //if a mediaItem is deselected we should deselect it in this viewModel
            if (mediaItem == null) {
                uiState.setValue(uiState.getValue().copy(null, null, false, false));
            }
        });

    }

    public void onEditItem(MediaItem mediaItem) {
        sharedStateRepository.onChangeSelectedMediaItem(mediaItem);
        uiState.setValue(uiState.getValue().copy(null, mediaItem, true, false));

    }

    public void onSelectItem(MediaItem mediaItem) {
        sharedStateRepository.onChangeSelectedMediaItem(mediaItem);
        uiState.setValue(uiState.getValue().copy(null, mediaItem, false, false));
    }

    public void onAddIconClicked() {
        if (uiState.getValue() != null) {
            uiState.setValue(uiState.getValue().copy(null, null, true, null));
        }
    }

    public void onOptionsIconClicked(MediaItem mediaItem) {
        sharedStateRepository.onChangeSelectedMediaItem(mediaItem);
        uiState.setValue(uiState.getValue().copy(null, mediaItem, false, true));

    }
}
