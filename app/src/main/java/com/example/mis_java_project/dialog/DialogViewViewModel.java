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

import java.util.Objects;

public class DialogViewViewModel extends AndroidViewModel {
    private final MediaItemRepository mediaItemRepository;

    private final MutableLiveData<DialogViewUiState> uiState = new MutableLiveData<>();

    private final SharedStateRepository sharedStateRepository;

    public LiveData<DialogViewUiState> uiState() {
        return uiState;
    }


    private String errorMessage = "Titel darf nicht Leer sein";

    private boolean preserveStateOnNavigation = true;
    private final boolean shouldResetSateOnClose;

    private Context context;


    public DialogViewViewModel(Application application) {
        super(application);
        this.context = application.getApplicationContext();
        mediaItemRepository = MediaItemRepository.getInstance(application);
        uiState.setValue(new DialogViewUiState("", null, null, null));
        sharedStateRepository = SharedStateRepository.getInstance();
        sharedStateRepository.selectedItem.observeForever(mediaItem -> {
            if (mediaItem != null) {
                uiState.setValue(uiState.getValue().copy(mediaItem.getTitle(), mediaItem.getImageUri(), mediaItem, null));
            } else {
                uiState.setValue(uiState.getValue().copy("", null, mediaItem, null));
            }
        });

        shouldResetSateOnClose = Boolean.TRUE.equals(sharedStateRepository.shouldResetStateOnClose.getValue());
    }

    public void onDeleteMediaItem(MediaItem mediaItem) {
        mediaItemRepository.delete(mediaItem);
        uiState.setValue(uiState.getValue().copy("", null, null, null));
    }

    public void onSaveMediaItem(MediaItem mediaItem) {
        if (uiState.getValue() != null) {
            var title = uiState.getValue().title();
            var imageUri = uiState.getValue().imageUri();
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
            uiState.setValue(uiState.getValue().copy("", null, null, null));
        }
    }

    public void onTextChanged(CharSequence charSequence) {
        var text = charSequence.toString().trim();
        if (uiState.getValue() != null) {
            String errorMessage = text.isEmpty() ? this.errorMessage : null;
            uiState.postValue(uiState.getValue().copy(text, null, uiState.getValue().selectedItem(), errorMessage));
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
                uiState.postValue(uiState.getValue().copy(filename, uri, uiState.getValue().selectedItem(), null));
            } else {
                uiState.postValue(uiState.getValue().copy(null, uri, uiState.getValue().selectedItem(), null));
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
        if (preserveStateOnNavigation && shouldResetSateOnClose) {
            sharedStateRepository.onChangeSelectedMediaItem(null);
            uiState.setValue(uiState.getValue().copy("", null, null, null));
            sharedStateRepository.setResetStateOnClose(true);
        }
        //Reset value to default state after handling
        preserveStateOnNavigation = true;
    }
}
