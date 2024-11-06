package com.example.mis_java_project.list;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mis_java_project.R;
import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.databinding.ActivityMainBinding;
import com.example.mis_java_project.dialog.MediaItemDialogFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListViewViewModel mediaItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //Setup ViewModel
        mediaItemViewModel = new ViewModelProvider(this).get(ListViewViewModel.class);

        //Setup DataBinding
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.mediaItemList.setLayoutManager(new LinearLayoutManager(this));
        binding.setViewModel(mediaItemViewModel);


        //Setup RecyclerView
        MediaItemListAdapter adapter = new MediaItemListAdapter(new ArrayList<>(), mediaItemViewModel::onOptionsIconClicked);
        binding.mediaItemList.setAdapter(adapter);

        //Observe ListView UiState changes
        mediaItemViewModel.uiState().observe(this, listViewUiState -> {
            adapter.setMediaItems(listViewUiState.mediaItemList());
            if (listViewUiState.showDialog()) {
                showMediaItemDialog();
            }

            if (listViewUiState.showOptions()) {
                showMediaItemOptionsDialog(mediaItemViewModel);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showMediaItemDialog() {
        MediaItemDialogFragment dialogFragment = new MediaItemDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "MediaItemDialogFragment");
    }

    private void showMediaItemOptionsDialog(ListViewViewModel listViewViewModel) {
        Log.d("TESTOO", "In showMethod");
        MediaItemOptionsFragment optionsFragment = new MediaItemOptionsFragment(listViewViewModel);
        optionsFragment.show(getSupportFragmentManager(), "MediaItemOptionsFragment");
    }
}