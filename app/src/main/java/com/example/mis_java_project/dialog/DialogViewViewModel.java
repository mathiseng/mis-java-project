package com.example.mis_java_project.dialog;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mis_java_project.MediaItemRepository;
import com.example.mis_java_project.SharedStateRepository;
import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.utils.FileUtils;

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
        _uiState.setValue(new DialogViewUiState("", null, null, null, false));
        sharedStateRepository = SharedStateRepository.getInstance();
        sharedStateRepository.selectedItem.observeForever(mediaItem -> {
            if (mediaItem != null) {
                _uiState.setValue(uiState.getValue().copy(mediaItem.getTitle(), mediaItem.getImageUri(), mediaItem, null, false));
            } else {
                _uiState.setValue(DialogViewUiState.initialUiState);
            }
        });

        // shouldResetSateOnClose = Boolean.TRUE.equals(sharedStateRepository.shouldResetStateOnClose.getValue());
    }

    public void onDeleteMediaItem(MediaItem mediaItem) {
        mediaItemRepository.delete(mediaItem);
        _uiState.setValue(uiState.getValue().copy("", null, null, null, false));
    }

    public void onSaveMediaItem(MediaItem mediaItem) {
        if (uiState.getValue() != null) {
            var title = uiState.getValue().getTitle();
            var imageUri = uiState.getValue().getImageUri();
            if (uiState.getValue().getTitle().isEmpty()) {
                _uiState.setValue(_uiState.getValue().copy(null, null, mediaItem, "Titel darf nicht leer sein", false));
                return;
            }

            if (uiState.getValue().getImageUri() == null) {
                _uiState.setValue(_uiState.getValue().copy(null, null, mediaItem, "Ein Bild muss ausgewählt werden", false));
                return;
            }


            if (mediaItem != null) {
                var newMediaItem = new MediaItem(mediaItem);
                newMediaItem.setTitle(title);
                newMediaItem.setImageUri(imageUri);
                mediaItemRepository.update(newMediaItem);
            } else {
                //Log.d("TESTII,", Objects.requireNonNull(uiState().getValue()).toString());
                MediaItem newItem = new MediaItem(title, imageUri.toString(), System.currentTimeMillis());
                mediaItemRepository.insert(newItem);
            }

            _uiState.setValue(uiState.getValue().copy("", null, null, null, true));
        }
    }

    public void onImageChanged(Uri uri) {

        //actually can not extract the real file name out of URI because of open issue https://stackoverflow.com/a/77481599/20470359
        // Issue is not resolved yet and might not be solved in future : https://issuetracker.google.com/issues/268079113
        // var filename = FileUtils.getFileName(uri, context);

        if (uiState.getValue() != null) {
            //here we would check if we already typed in some title. If not then set the filename as a title ... here just using a method that is not working due to open issue
            if (uiState.getValue().getTitle().isBlank()) {
                var filename = FileUtils.getFileName(uri, context);
                _uiState.postValue(uiState.getValue().copy(filename, uri, uiState.getValue().getSelectedItem(), null, false));
            } else {
                _uiState.postValue(uiState.getValue().copy(null, uri, uiState.getValue().getSelectedItem(), null, false));
            }
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
            _uiState.setValue(DialogViewUiState.initialUiState);
            sharedStateRepository.setResetStateOnClose(true);
        }
        //Reset value to default state after handling
        preserveStateOnNavigation = false;
    }
}
