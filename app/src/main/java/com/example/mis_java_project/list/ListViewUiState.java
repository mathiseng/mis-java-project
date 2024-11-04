package com.example.mis_java_project.list;

import androidx.annotation.Nullable;

import com.example.mis_java_project.data.model.MediaItem;

import java.util.List;

public record ListViewUiState(List<MediaItem> mediaItemList, MediaItem selectedMediaItem, Boolean showDialog) {
    public ListViewUiState copy(@Nullable List<MediaItem> mediaItemList, @Nullable MediaItem selectedMediaItem, @Nullable Boolean showDialog) {
        return new ListViewUiState(
                mediaItemList != null ? mediaItemList : this.mediaItemList,
                selectedMediaItem,
                showDialog != null ? showDialog : this.showDialog
        );
    }
}
