package com.example.mis_java_project.list;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.mis_java_project.repository.MediaItemRepository;
import com.example.mis_java_project.repository.SharedStateRepository;
import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.data.model.StorageOption;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListViewViewModel extends AndroidViewModel {

    private final MediatorLiveData<ListViewUiState> uiState = new MediatorLiveData<>();

    public LiveData<ListViewUiState> uiState() {
        return uiState;
    }

    private final SharedStateRepository sharedStateRepository;

    //to enhance performance we cache them here in viewmodel.
    private List<MediaItem> completeMediaItemList;

    Context context;

    public ListViewViewModel(Application application) {
        super(application);
        context = application.getApplicationContext();
        uiState.setValue(new ListViewUiState(new ArrayList<>(), null, false, false, StorageOption.BOTH));

        //Observe MediaItemRepository and merge changes of list
        MediaItemRepository mediaItemRepository = MediaItemRepository.getInstance(application);
        sharedStateRepository = SharedStateRepository.getInstance();
        uiState.addSource(mediaItemRepository.mediaItems, mediaItems -> {
            completeMediaItemList = mediaItems;
            var list = filterMediaItems(uiState.getValue().selectedStorageOption(), mediaItems);
            //resets the showDialog automatically if actions to repository happend
            uiState.setValue(uiState.getValue().copy(list, null, false, false, uiState.getValue().selectedStorageOption()));
            sharedStateRepository.onChangeSelectedMediaItem(null);
        });

        uiState.addSource(sharedStateRepository.selectedItem, mediaItem -> {
            //if a mediaItem is deselected we should deselect it in this viewModel
            if (mediaItem == null) {
                uiState.setValue(uiState.getValue().copy(null, null, false, false, uiState.getValue().selectedStorageOption()));
            }
        });

    }

    public void onEditItem(MediaItem mediaItem) {
        sharedStateRepository.onChangeSelectedMediaItem(mediaItem);
        sharedStateRepository.setResetStateOnClose(true);
        uiState.setValue(uiState.getValue().copy(null, mediaItem, true, false, uiState.getValue().selectedStorageOption()));

    }

    public void onSelectItem(MediaItem mediaItem) {
        sharedStateRepository.setResetStateOnClose(false);
        sharedStateRepository.onChangeSelectedMediaItem(mediaItem);
        uiState.setValue(uiState.getValue().copy(null, mediaItem, false, false, uiState.getValue().selectedStorageOption()));
    }

    public void onAddIconClicked() {
        if (uiState.getValue() != null) {
            sharedStateRepository.setResetStateOnClose(true);
            uiState.setValue(uiState.getValue().copy(null, null, true, null, uiState.getValue().selectedStorageOption()));
        }
    }

    public void onOptionsIconClicked(MediaItem mediaItem) {
        sharedStateRepository.onChangeSelectedMediaItem(mediaItem);
        sharedStateRepository.setResetStateOnClose(true);
        uiState.setValue(uiState.getValue().copy(null, mediaItem, false, true, uiState.getValue().selectedStorageOption()));

    }

    public void onChangeSelectedStorageOption(StorageOption storageOption) {
        var newList = filterMediaItems(storageOption, completeMediaItemList);
        uiState.setValue(uiState.getValue().copy(newList, null, false, false, storageOption));
    }

    private List<MediaItem> filterMediaItems(StorageOption selectedOption, List<MediaItem> mediaItems) {
//        if (mediaItems == null) {
//            filteredMediaItems.setValue(Collections.emptyList());
//            return;
//        }

        List<MediaItem> filteredList = mediaItems.stream()
                .filter(mediaItem -> {
                    switch (selectedOption) {
                        case LOCAL:
                            return mediaItem.getStorageOption() == StorageOption.LOCAL;
                        case REMOTE:
                            return mediaItem.getStorageOption() == StorageOption.REMOTE;
                        case BOTH:
                            return true;  // Zeige alle Items
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());

        return filteredList;
    }

}
