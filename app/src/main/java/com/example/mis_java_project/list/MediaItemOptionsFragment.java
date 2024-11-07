package com.example.mis_java_project.list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mis_java_project.databinding.OptionsMediaItemBinding;
import com.example.mis_java_project.dialog.ConfirmDeletionFragment;
import com.example.mis_java_project.dialog.DialogViewViewModel;

public class MediaItemOptionsFragment extends DialogFragment {
    private OptionsMediaItemBinding binding;
    ListViewViewModel listViewViewModel;
    DialogViewViewModel dialogViewViewModel;

    // Constructor to pass in the item and ViewModel
    public MediaItemOptionsFragment(ListViewViewModel listViewViewModel) {
        this.listViewViewModel = listViewViewModel;

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        dialogViewViewModel.onDismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialogViewViewModel = new ViewModelProvider(requireActivity()).get(DialogViewViewModel.class);
        var uiState = dialogViewViewModel.uiState();
        var mediaItem = uiState.getValue().selectedItem();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = OptionsMediaItemBinding.inflate(inflater);
        binding.setMediaItem(mediaItem);
        builder.setView(binding.getRoot());

        binding.deleteButton.setOnClickListener(view -> {
            dialogViewViewModel.setPreserveStateOnNavigation(false);
            showConfirmDeletionDialog();

            dismiss();
        });

        binding.editButton.setOnClickListener(view -> {
            dialogViewViewModel.setPreserveStateOnNavigation(false);
            listViewViewModel.onEditItem(mediaItem);
            dismiss();
        });

        return builder.create();
    }

    private void showConfirmDeletionDialog() {
        ConfirmDeletionFragment optionsFragment = new ConfirmDeletionFragment();
        optionsFragment.show(requireActivity().getSupportFragmentManager(), "ConfirmDeletionFragment");
    }
}
