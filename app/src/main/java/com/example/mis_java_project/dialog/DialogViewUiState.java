package com.example.mis_java_project.dialog;

import androidx.annotation.Nullable;

public record DialogViewUiState(String title, boolean isEditing, String errorMessage) {
    public DialogViewUiState copy(@Nullable String title, boolean isEditing, @Nullable String errorMessage) {
        return new DialogViewUiState(
                title != null ? title : this.title,
                isEditing,
                errorMessage
        );
    }
}
