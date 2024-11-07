package com.example.mis_java_project.dialog;

import androidx.annotation.Nullable;

import com.example.mis_java_project.data.model.MediaItem;

public record DialogViewUiState(String title, MediaItem selectedItem, String errorMessage) {

    public static final DialogViewUiState initialUiState = new DialogViewUiState("", new MediaItem("", "", 0), null);

    public DialogViewUiState copy(@Nullable String title, @Nullable MediaItem selectedItem, @Nullable String errorMessage) {
        return new DialogViewUiState(
                title != null ? title : this.title,
                selectedItem,
                errorMessage
        );
    }
}
