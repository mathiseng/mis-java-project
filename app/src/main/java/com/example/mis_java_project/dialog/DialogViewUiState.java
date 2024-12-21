package com.example.mis_java_project.dialog;

import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.mis_java_project.BR;
import com.example.mis_java_project.data.model.MediaItem;

public class DialogViewUiState extends BaseObservable {

    private String title;
    private Uri imageUri;
    private MediaItem selectedItem;


    private String errorMessage;
    private Boolean shouldDismiss;

    private Boolean isStoredInCloud;

    public DialogViewUiState(String title, Uri imageUri, MediaItem selectedItem, String errorMessage, Boolean shouldDismiss, Boolean isStoredInCloud) {
        this.title = title;
        this.imageUri = imageUri;
        this.selectedItem = selectedItem;
        this.errorMessage = errorMessage;
        this.shouldDismiss = shouldDismiss;
        this.isStoredInCloud = isStoredInCloud;
    }

    public static final DialogViewUiState initialUiState = new DialogViewUiState("", null, null, null, false, false);

    @Bindable
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        notifyPropertyChanged(BR.errorMessage);
    }

    @Bindable
    public MediaItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(MediaItem selectedItem) {
        this.selectedItem = selectedItem;
        notifyPropertyChanged(BR.selectedItem);
    }

    @Bindable
    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
        notifyPropertyChanged(BR.imageUri);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    public Boolean getShouldDismiss() {
        return shouldDismiss;
    }

    public void setShouldDismiss(Boolean shouldDismiss) {
        this.shouldDismiss = shouldDismiss;
    }

    @Bindable
    public Boolean getIsStoredInCloud() {
        return isStoredInCloud;
    }

    public void setIsStoredInCloud(Boolean storedInCloud) {
        isStoredInCloud = storedInCloud;
    }

    public DialogViewUiState copy(@Nullable String title, @Nullable Uri imageUri, @Nullable MediaItem selectedItem, @Nullable String errorMessage, Boolean shouldDismiss, @Nullable Boolean isStoredInCloud) {
        return new DialogViewUiState(title != null ? title : this.title, imageUri != null ? imageUri : this.imageUri, selectedItem, errorMessage, shouldDismiss, isStoredInCloud != null ? isStoredInCloud : this.isStoredInCloud);
    }
}
