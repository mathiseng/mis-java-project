package com.example.mis_java_project.list;

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
import com.example.mis_java_project.dialog.DialogViewViewModel;

public class MediaItemDialogFragment extends DialogFragment {
    private DialogMediaItemBinding binding;
    private final MediaItem mediaItem;

    // Constructor to pass in the item and ViewModel
    public MediaItemDialogFragment(MediaItem mediaItem) {
        this.mediaItem = mediaItem;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DialogViewViewModel dialogViewViewModel = new ViewModelProvider(requireActivity()).get(DialogViewViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = DialogMediaItemBinding.inflate(inflater, null, false);
        binding.setMediaItem(mediaItem);
        // binding.setMediaItemViewModel(mediaItemViewModel);

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

                String title = binding.editTextTitle.getText().toString().trim();

                // Check if title is empty
                if (title.isEmpty()) {
                    binding.editTextTitle.setError("Titel darf nicht leer sein");
                    return;  // Exit the listener, keeping the dialog open
                }

                // If title is not empty, save item
                dialogViewViewModel.onSaveMediaItem(mediaItem, title);

                // Close the dialog if validation is successful
                dialog.dismiss();
            });
        });
        return dialog;
    }
}
