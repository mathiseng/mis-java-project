package com.example.mis_java_project.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mis_java_project.data.model.MediaItem;

/**
 * This MediaItemRepository should handle and hide logic for calling local and remote databases under the hood . ViewModels should only know what data they need independent of the origin
 */
public class SharedStateRepository {

    private final MutableLiveData<MediaItem> _selectedItem = new MutableLiveData<>();
    public final LiveData<MediaItem> selectedItem = _selectedItem;

    private final MutableLiveData<Boolean> _shouldResetStateOnClose = new MutableLiveData<>(false);
    public final LiveData<Boolean> shouldResetStateOnClose = _shouldResetStateOnClose;

    private static SharedStateRepository INSTANCE;

    public static SharedStateRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SharedStateRepository();
        }

        return INSTANCE;
    }

    public void onChangeSelectedMediaItem(MediaItem mediaItem) {
        _selectedItem.setValue(mediaItem);
    }

    public void setResetStateOnClose(Boolean shouldReset) {
        _shouldResetStateOnClose.setValue(shouldReset);
    }
}
