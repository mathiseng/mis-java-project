package com.example.mis_java_project.list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mis_java_project.databinding.OptionsMediaItemBinding;
import com.example.mis_java_project.dialog.DialogViewViewModel;

public class MediaItemOptionsFragment extends DialogFragment {
    private OptionsMediaItemBinding binding;
    ListViewViewModel listViewViewModel;

    // Constructor to pass in the item and ViewModel
    public MediaItemOptionsFragment(ListViewViewModel listViewViewModel) {
        this.listViewViewModel = listViewViewModel;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DialogViewViewModel dialogViewViewModel = new ViewModelProvider(requireActivity()).get(DialogViewViewModel.class);
        var uiState = dialogViewViewModel.uiState();
        var mediaItem = uiState.getValue().selectedItem();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        binding = OptionsMediaItemBinding.inflate(inflater);
        binding.setMediaItem(mediaItem);
        builder.setView(binding.getRoot());

        binding.deleteButton.setOnClickListener(view -> {
            dialogViewViewModel.onDeleteMediaItem(mediaItem);
            dismiss();
        });

        binding.editButton.setOnClickListener(view -> {
            listViewViewModel.onSelectItem(mediaItem);
            dismiss();
        });

        return builder.create();
    }
}
