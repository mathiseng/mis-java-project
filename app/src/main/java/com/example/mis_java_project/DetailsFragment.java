package com.example.mis_java_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.mis_java_project.data.model.StorageOption;
import com.example.mis_java_project.databinding.FragmentDetailsBinding;
import com.example.mis_java_project.details.DetailsViewViewModel;
import com.example.mis_java_project.dialog.ConfirmDeletionFragment;


public class DetailsFragment extends Fragment {

    DetailsViewViewModel detailsViewViewModel;

    public void onResume() {
        super.onResume();
        // Invalidate the options menu to trigger the re-creation of the toolbar menu
        requireActivity().invalidateOptionsMenu();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_details, container, false);

        getActivity().setTitle("Details");
        detailsViewViewModel = new ViewModelProvider(this).get(DetailsViewViewModel.class);


        //Setup DataBinding
        FragmentDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false);
        binding.setViewModel(detailsViewViewModel);
        binding.setMediaItem(detailsViewViewModel.uiState().getValue().selectedMediaItem());

        var item = detailsViewViewModel.uiState().getValue().selectedMediaItem();
        if (item.getStorageOption() == StorageOption.REMOTE) {
            Glide.with(requireActivity().getApplicationContext()).load(item.getImageUri())  // URI from Firebase
                    .into(binding.mediaImage);
        } else {
            binding.mediaImage.setImageURI(item.getImageUri());
        }


        //Observe ListView UiState changes
        detailsViewViewModel.uiState().observe(getViewLifecycleOwner(), uiState -> {

            if (uiState.showDeleteConfirmation()) {
                showConfirmDeletionDialog();
            }

            if (uiState.dismissDetails()) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }


    private void showConfirmDeletionDialog() {
        ConfirmDeletionFragment optionsFragment = new ConfirmDeletionFragment();
        optionsFragment.show(requireActivity().getSupportFragmentManager(), "ConfirmDeletionFragment");
    }
}