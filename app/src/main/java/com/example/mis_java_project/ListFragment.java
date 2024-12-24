package com.example.mis_java_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.databinding.FragmentListBinding;
import com.example.mis_java_project.dialog.MediaItemDialogFragment;
import com.example.mis_java_project.list.ListViewViewModel;
import com.example.mis_java_project.list.MediaItemListAdapter;
import com.example.mis_java_project.list.MediaItemOptionsFragment;

import java.util.ArrayList;


public class ListFragment extends Fragment {
    ListViewViewModel mediaItemViewModel;
    FragmentListBinding binding;

    @Override
    public void onResume() {
        super.onResume();
        // Invalidate the options menu to trigger the re-creation of the toolbar menu
        requireActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mediaItemViewModel = new ViewModelProvider(requireActivity()).get(ListViewViewModel.class);
        getActivity().setTitle("Media Items");

        //Binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        binding.mediaItemList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.setViewModel(mediaItemViewModel);

        MediaItemListAdapter adapter = new MediaItemListAdapter(new ArrayList<>(), this::startDetailActivity, mediaItemViewModel::onOptionsIconClicked);
        binding.mediaItemList.setAdapter(adapter);

        //Observe ListView UiState changes
        mediaItemViewModel.uiState().observe(getViewLifecycleOwner(), listViewUiState -> {
            binding.setSelectedStorageOption(listViewUiState.selectedStorageOption());
            adapter.setMediaItems(listViewUiState.mediaItemList());
            if (listViewUiState.showDialog()) {
                showMediaItemDialog();
            }

            if (listViewUiState.showOptions()) {
                showMediaItemOptionsDialog(mediaItemViewModel);
            }
        });

        return binding.getRoot();
    }

    private void showMediaItemDialog() {
        MediaItemDialogFragment dialogFragment = new MediaItemDialogFragment();
        dialogFragment.show(requireActivity().getSupportFragmentManager(), "MediaItemDialogFragment");
    }

    private void showMediaItemOptionsDialog(ListViewViewModel listViewViewModel) {
        MediaItemOptionsFragment optionsFragment = new MediaItemOptionsFragment(listViewViewModel);
        optionsFragment.show(requireActivity().getSupportFragmentManager(), "MediaItemOptionsFragment");
    }

    public void startDetailActivity(MediaItem mediaItem) {
        mediaItemViewModel.onSelectItem(mediaItem);
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetailsFragment()).addToBackStack(null).commit();
    }
}