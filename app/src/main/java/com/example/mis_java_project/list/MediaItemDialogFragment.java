package com.example.mis_java_project.list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.databinding.DialogMediaItemBinding;

public class MediaItemDialogFragment extends DialogFragment {
    private DialogMediaItemBinding binding;
    private final MediaItem mediaItem;
    private final ListViewViewModel mediaItemViewModel;

    // Constructor to pass in the item and ViewModel
    public MediaItemDialogFragment(MediaItem mediaItem, ListViewViewModel mediaItemViewModel) {
        this.mediaItem = mediaItem;
        this.mediaItemViewModel = mediaItemViewModel;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = DialogMediaItemBinding.inflate(inflater, null, false);
        binding.setMediaItem(mediaItem);
        // binding.setMediaItemViewModel(mediaItemViewModel);

        builder.setView(binding.getRoot())
                .setPositiveButton(mediaItem == null ? "Erstellen" : "Ändern", null);

        if (mediaItem != null) {
            builder.setNegativeButton("Löschen", (dialog, id) -> mediaItemViewModel.delete(mediaItem));
        } else {
            builder.setNegativeButton("Abbrechen", (dialog, id) -> {
                mediaItemViewModel.onDialogFinished();
                dialog.dismiss();
            });
        }
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {

                String title = binding.editTextTitle.getText().toString().trim();

                // Check if title is empty
                if (title.isEmpty()) {
                    binding.editTextTitle.setError("Titel darf nicht leer sein");
                    return;  // Exit the listener, keeping the dialog open
                }

                // If title is not empty, save item
                mediaItemViewModel.onSaveMediaItem(mediaItem, title);

                // Close the dialog if validation is successful
                mediaItemViewModel.onDialogFinished();
                dialog.dismiss();
            });
        });
        return dialog;
    }
}
