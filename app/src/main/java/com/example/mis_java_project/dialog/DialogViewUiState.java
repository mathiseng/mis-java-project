package com.example.mis_java_project.dialog;

import androidx.annotation.Nullable;

public record DialogViewUiState(String title, String errorMessage) {
    public DialogViewUiState copy(@Nullable String title, @Nullable String errorMessage) {
        return new DialogViewUiState(
                title != null ? title : this.title,
                errorMessage
        );
    }
}
