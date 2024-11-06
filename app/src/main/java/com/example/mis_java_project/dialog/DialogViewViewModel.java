package com.example.mis_java_project.dialog;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mis_java_project.MediaItemRepository;
import com.example.mis_java_project.SharedStateRepository;
import com.example.mis_java_project.data.model.MediaItem;

public class DialogViewViewModel extends AndroidViewModel {
    private final MediaItemRepository mediaItemRepository;

    private final MutableLiveData<DialogViewUiState> uiState = new MutableLiveData<>();

    public LiveData<DialogViewUiState> uiState() {
        return uiState;
    }


    private String errorMessage = "Titel darf nicht Leer sein";

    public DialogViewViewModel(Application application) {
        super(application);
        mediaItemRepository = MediaItemRepository.getInstance(application);
        uiState.setValue(new DialogViewUiState("", null, null));
        SharedStateRepository sharedStateRepository = SharedStateRepository.getInstance();
        sharedStateRepository.selectedItem.observeForever(mediaItem -> {
            uiState.setValue(uiState.getValue().copy("", mediaItem, null));

        });
    }

    public void onDeleteMediaItem(MediaItem mediaItem) {
        mediaItemRepository.delete(mediaItem);
        uiState.setValue(uiState.getValue().copy("", null, null));
    }

    public void onSaveMediaItem(MediaItem mediaItem) {
        if (uiState.getValue() != null) {
            var title = uiState.getValue().title();
            if (mediaItem != null) {
                mediaItem.setTitle(title);
                mediaItemRepository.update(mediaItem);
            } else {
                MediaItem newItem = new MediaItem(title, "https://example.com/image.jpg", System.currentTimeMillis());
                mediaItemRepository.insert(newItem);
            }
            uiState.setValue(uiState.getValue().copy("", null, null));
        }
    }

    public void onTextChanged(CharSequence charSequence) {
        var text = charSequence.toString().trim();
        if (uiState.getValue() != null) {
            String errorMessage = text.isEmpty() ? this.errorMessage : null;
            uiState.postValue(uiState.getValue().copy(text, uiState.getValue().selectedItem(), errorMessage));
        }
    }
}
