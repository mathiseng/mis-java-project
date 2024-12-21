package com.example.mis_java_project.list;

import androidx.annotation.Nullable;

import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.data.model.StorageOption;

import java.util.List;

public record ListViewUiState(List<MediaItem> mediaItemList, MediaItem selectedMediaItem,
                              Boolean showDialog, Boolean showOptions,
                              StorageOption selectedStorageOption) {
    public ListViewUiState copy(@Nullable List<MediaItem> mediaItemList, @Nullable MediaItem selectedMediaItem, @Nullable Boolean showDialog, @Nullable Boolean showOptions, @Nullable StorageOption selectedStorageOption) {
        return new ListViewUiState(mediaItemList != null ? mediaItemList : this.mediaItemList, selectedMediaItem, showDialog != null ? showDialog : this.showDialog, showOptions != null ? showOptions : this.showOptions, selectedStorageOption);
    }
}
