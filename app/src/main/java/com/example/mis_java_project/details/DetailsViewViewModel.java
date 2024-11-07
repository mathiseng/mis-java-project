package com.example.mis_java_project.details;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mis_java_project.MediaItemRepository;
import com.example.mis_java_project.SharedStateRepository;
import com.example.mis_java_project.data.model.MediaItem;

public class DetailsViewViewModel extends AndroidViewModel {

    private final MutableLiveData<DetailsViewUiState> uiState = new MutableLiveData<>();

    public LiveData<DetailsViewUiState> uiState() {
        return uiState;
    }

    MediaItemRepository mediaItemRepository;
    private final SharedStateRepository sharedStateRepository;

    private boolean isInitialized = false;

    public DetailsViewViewModel(Application application) {
        super(application);
        uiState.setValue(new DetailsViewUiState(null, false, false));

        //Observe MediaItemRepository and merge changes of list
        mediaItemRepository = MediaItemRepository.getInstance(application);
        sharedStateRepository = SharedStateRepository.getInstance();
        sharedStateRepository.selectedItem.observeForever(mediaItem -> {
            uiState.setValue(uiState.getValue().copy(mediaItem, false, false));
        });

        mediaItemRepository.mediaItems.observeForever(mediaItems -> {
            //To ensure a dismiss when we made a crud operation (deleted an item)
            if (!isInitialized) {
                isInitialized = true;
            } else {
                onDismissDetails();
            }
        });
    }


    public void onDeleteMediaItem(MediaItem mediaItem) {
        uiState.setValue(uiState.getValue().copy(mediaItem, true, false));
    }

    public void onDismissDetails() {
        sharedStateRepository.onChangeSelectedMediaItem(null);
        uiState.setValue(uiState.getValue().copy(null, false, true));
    }
}
