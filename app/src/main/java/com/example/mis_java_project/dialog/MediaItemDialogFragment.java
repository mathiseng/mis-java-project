package com.example.mis_java_project.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mis_java_project.databinding.DialogMediaItemBinding;

import java.util.Objects;

public class MediaItemDialogFragment extends DialogFragment {
    private DialogMediaItemBinding binding;
    // Constructor to pass in the item and ViewModel

    DialogViewViewModel dialogViewViewModel;


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        dialogViewViewModel.onDismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialogViewViewModel = new ViewModelProvider(requireActivity()).get(DialogViewViewModel.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        var uiState = dialogViewViewModel.uiState;
        var mediaItem = uiState.getValue().getSelectedItem();

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = DialogMediaItemBinding.inflate(inflater);

        uiState.observe(requireActivity(), dialogViewUiState -> {
            binding.setUiState(dialogViewUiState);

            if (dialogViewUiState.getImageUri() != null) {
                binding.mediaItemImage.setImageURI(dialogViewUiState.getImageUri());
            }

            if (dialogViewUiState.getErrorMessage() != null) {
                binding.editTextTitle.setError(dialogViewUiState.getErrorMessage());
            }

            if (dialogViewUiState.getShouldDismiss()) {
                dismiss();
            }
        });
        builder.setView(binding.getRoot()).setPositiveButton(mediaItem == null ? "Erstellen" : "Ändern", null);

        if (mediaItem != null) {
            builder.setNegativeButton("Löschen", (dialog, id) -> {
                showConfirmDeletionDialog();
            });
        } else {
            builder.setNegativeButton("Abbrechen", (dialog, id) -> {
                dialog.dismiss();
            });
        }
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                dialogViewViewModel.onSaveMediaItem(mediaItem);
            });
        });

        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                // previewImage.setImageURI(uri);
                dialogViewViewModel.onImageChanged(uri);
            }
        });

        binding.buttonSelectPicture.setOnClickListener((view -> {
            mGetContent.launch("image/*");
        }));
        //Handle auto focus and showing keyboard for the editText
        //binding.editTextTitle.requestFocus();
        // binding.editTextTitle.setSelection(mediaItem.getTitle().length());

//        binding.editTextTitle.postDelayed(() -> {
//            binding.editTextTitle.setSelection(binding.editTextTitle.getText().length());
//        }, 150);
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return dialog;
    }

    private void showConfirmDeletionDialog() {
        ConfirmDeletionFragment optionsFragment = new ConfirmDeletionFragment();
        optionsFragment.show(requireActivity().getSupportFragmentManager(), "ConfirmDeletionFragment");
    }
}
