package com.example.mis_java_project.dialog;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.example.mis_java_project.data.model.MediaItem;

public record DialogViewUiState(String title, Uri imageUri, MediaItem selectedItem,
                                String errorMessage, Boolean shouldDismiss,
                                Boolean isStoredInCloud) {
    public static final DialogViewUiState initialUiState = new DialogViewUiState("", null, null, null, false, false);

    public DialogViewUiState copy(@Nullable String title, @Nullable Uri imageUri, @Nullable MediaItem selectedItem, @Nullable String errorMessage, Boolean shouldDismiss, @Nullable Boolean isStoredInCloud) {
        return new DialogViewUiState(title != null ? title : this.title, imageUri != null ? imageUri : this.imageUri, selectedItem, errorMessage, shouldDismiss, isStoredInCloud != null ? isStoredInCloud : this.isStoredInCloud);
    }
}
