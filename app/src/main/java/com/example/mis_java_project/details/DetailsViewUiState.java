package com.example.mis_java_project.details;

import androidx.annotation.Nullable;

import com.example.mis_java_project.data.model.MediaItem;

public record DetailsViewUiState(MediaItem selectedMediaItem,
                                 Boolean showDeleteConfirmation, Boolean dismissDetails) {
    public DetailsViewUiState copy(@Nullable MediaItem selectedMediaItem, @Nullable Boolean showDeleteConfirmation, @Nullable Boolean dismissDetails) {
        return new DetailsViewUiState(
                selectedMediaItem,
                showDeleteConfirmation != null ? showDeleteConfirmation : this.showDeleteConfirmation,
                dismissDetails != null ? dismissDetails : this.dismissDetails
        );
    }
}
