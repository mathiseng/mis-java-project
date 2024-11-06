package com.example.mis_java_project.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mis_java_project.databinding.ConfirmDeletionDialogBinding;

public class ConfirmDeletionFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DialogViewViewModel dialogViewViewModel = new ViewModelProvider(requireActivity()).get(DialogViewViewModel.class);
        var uiState = dialogViewViewModel.uiState();
        var mediaItem = uiState.getValue().selectedItem();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        ConfirmDeletionDialogBinding binding = ConfirmDeletionDialogBinding.inflate(inflater);
        binding.setMediaItem(mediaItem);
        builder.setView(binding.getRoot()).setPositiveButton("Löschen", (dialog, id) -> dialogViewViewModel.onDeleteMediaItem(mediaItem));
        builder.setNegativeButton("Abbrechen", (dialog, id) -> dismiss());

        return builder.create();
    }
}
