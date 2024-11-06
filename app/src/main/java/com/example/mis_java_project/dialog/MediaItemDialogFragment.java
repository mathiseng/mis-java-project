package com.example.mis_java_project.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.databinding.DialogMediaItemBinding;

public class MediaItemDialogFragment extends DialogFragment {
    private DialogMediaItemBinding binding;
    // Constructor to pass in the item and ViewModel

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DialogViewViewModel dialogViewViewModel = new ViewModelProvider(requireActivity()).get(DialogViewViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        var uiState = dialogViewViewModel.uiState();
        var mediaItem = uiState.getValue().selectedItem();

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = DialogMediaItemBinding.inflate(inflater);
        binding.setMediaItem(mediaItem);
        binding.setViewModel(dialogViewViewModel);

        builder.setView(binding.getRoot())
                .setPositiveButton(mediaItem == null ? "Erstellen" : "Ändern", null);

        if (mediaItem != null) {
            builder.setNegativeButton("Löschen", (dialog, id) -> dialogViewViewModel.onDeleteMediaItem(mediaItem));
        } else {
            builder.setNegativeButton("Abbrechen", (dialog, id) -> {
                dialog.dismiss();
            });
        }
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {

                // Check if title is empty
                if (dialogViewViewModel.uiState().getValue().title().isEmpty()) {
                    binding.editTextTitle.setError("Titel darf nicht leer sein");
                    return;  // Exit the listener, keeping the dialog open
                }

                // If title is not empty, save item
                dialogViewViewModel.onSaveMediaItem(mediaItem);

                // Close the dialog if validation is successful
                dialog.dismiss();
            });
        });
        return dialog;
    }
}
