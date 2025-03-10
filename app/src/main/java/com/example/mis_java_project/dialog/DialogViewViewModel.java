package com.example.mis_java_project.dialog;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.data.model.StorageOption;
import com.example.mis_java_project.repository.MediaItemRepository;
import com.example.mis_java_project.repository.SharedStateRepository;
import com.example.mis_java_project.utils.FileUtils;
import com.google.android.gms.maps.model.LatLng;

public class DialogViewViewModel extends AndroidViewModel {
    private final MediaItemRepository mediaItemRepository;

    private final MutableLiveData<DialogViewUiState> _uiState = new MutableLiveData<>();

    private final SharedStateRepository sharedStateRepository;

    public LiveData<DialogViewUiState> uiState = _uiState;


    public void setUiState(DialogViewUiState newState) {
        _uiState.setValue(newState);
    }

    //Flag that keeps the information wheter we should pass information to another Dialog or we reset them
    private boolean preserveStateOnNavigation = false;

    private final Context context;


    public DialogViewViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
        mediaItemRepository = MediaItemRepository.getInstance(application);
        _uiState.setValue(new DialogViewUiState("", null, null, null, false, false));
        sharedStateRepository = SharedStateRepository.getInstance();
        sharedStateRepository.selectedItem.observeForever(mediaItem -> {
            if (mediaItem != null) {
                _uiState.setValue(uiState.getValue().copy(mediaItem.getTitle(), mediaItem.getImageUri(), mediaItem, null, false, mediaItem.getStorageOption() == StorageOption.REMOTE));
            } else {
                _uiState.setValue(DialogViewUiState.initialUiState);
            }
        });

        // shouldResetSateOnClose = Boolean.TRUE.equals(sharedStateRepository.shouldResetStateOnClose.getValue());
    }

    public void onDeleteMediaItem(MediaItem mediaItem) {
        mediaItemRepository.delete(mediaItem);
        _uiState.setValue(DialogViewUiState.initialUiState);
    }

    public void onSaveMediaItem(MediaItem mediaItem) {
        if (uiState.getValue() != null) {
            var imageLocation = FileUtils.extractLocationFromFile(uiState.getValue().imageUri(), context);
            var title = uiState.getValue().title();
            var imageUri = uiState.getValue().imageUri();
            var isStoresInCloud = uiState.getValue().isStoredInCloud();
            var storageOption = isStoresInCloud ? StorageOption.REMOTE : StorageOption.LOCAL;

            if (uiState.getValue().title().isEmpty()) {
                _uiState.setValue(_uiState.getValue().copy(null, null, mediaItem, "Titel darf nicht leer sein", false, null));
                return;
            }

            if (uiState.getValue().imageUri() == null) {
                _uiState.setValue(_uiState.getValue().copy(null, null, mediaItem, "Ein Bild muss ausgewählt werden", false, null));
                return;
            }


            if (mediaItem != null) {
                var newMediaItem = new MediaItem(mediaItem);
                newMediaItem.setTitle(title);
                newMediaItem.setImageUri(imageUri);
                newMediaItem.setStorageOption(storageOption);
                newMediaItem.setImageLocation(imageLocation != null ? imageLocation : new LatLng(52.52, 13.4));
                mediaItemRepository.update(newMediaItem);
            } else {
                MediaItem newItem = new MediaItem(title, imageUri.toString(), System.currentTimeMillis(), storageOption, imageLocation != null ? imageLocation : new LatLng(53.0, 13.13));
                mediaItemRepository.insert(newItem);
            }

            _uiState.setValue(uiState.getValue().copy("", null, null, null, true, false));
        }
    }

    public void onImageChanged(Uri uri) {

        //actually can not extract the real file name out of URI because of open issue https://stackoverflow.com/a/77481599/20470359
        // Issue is not resolved yet and might not be solved in future : https://issuetracker.google.com/issues/268079113
        // var filename = FileUtils.getFileName(uri, context);

        if (uiState.getValue() != null) {
            //here we would check if we already typed in some title. If not then set the filename as a title ... here just using a method that is not working due to open issue
            if (uiState.getValue().title().isBlank()) {
                var filename = FileUtils.getFileName(uri, context);
                _uiState.postValue(uiState.getValue().copy(filename, uri, uiState.getValue().selectedItem(), null, false, null));
            } else {
                _uiState.postValue(uiState.getValue().copy(null, uri, uiState.getValue().selectedItem(), null, false, null));
            }
        }
    }

    public void onTextChanged(CharSequence charSequence) {
        var text = charSequence.toString().trim();
        if (uiState.getValue() != null) {
            String errorMessage = text.isEmpty() ? this.uiState.getValue().errorMessage() : null;
            _uiState.postValue(uiState.getValue().copy(text, null, uiState.getValue().selectedItem(), errorMessage, false, null));
        }
    }

    public void onStorageOptionChanged() {
        if (uiState.getValue() != null) {
            _uiState.postValue(uiState.getValue().copy(null, null, uiState.getValue().selectedItem(), null, false, !uiState.getValue().isStoredInCloud()));
        }
    }

    /**
     * A helper method to prevent onClick events of buttons (e.g. edit button) to reset the selectedItem and uiState,
     * because we need that information in further navigation
     */
    public void setPreserveStateOnNavigation(boolean preserveStateOnNavigation) {
        this.preserveStateOnNavigation = preserveStateOnNavigation;
    }

    /**
     * To handle dismiss actions that where invoked due to touch outside.
     * Resets the selectedItem and the uiState to have a clean State after closing the dialog
     */

    public void onDismiss() {
        if (!preserveStateOnNavigation && Boolean.TRUE.equals(sharedStateRepository.shouldResetStateOnClose.getValue())) {
            sharedStateRepository.onChangeSelectedMediaItem(null);
            _uiState.postValue(DialogViewUiState.initialUiState);
            sharedStateRepository.setResetStateOnClose(true);
        }
        //Reset value to default state after handling
        preserveStateOnNavigation = false;
    }
}
