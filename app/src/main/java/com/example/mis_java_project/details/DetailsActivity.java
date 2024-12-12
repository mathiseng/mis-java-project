package com.example.mis_java_project.details;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.mis_java_project.R;
import com.example.mis_java_project.databinding.DetailsScreenBinding;
import com.example.mis_java_project.dialog.ConfirmDeletionFragment;

public class DetailsActivity extends AppCompatActivity {

    DetailsViewViewModel detailsViewViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //Setup ViewModel
        detailsViewViewModel = new ViewModelProvider(this).get(DetailsViewViewModel.class);


        //Setup DataBinding
        DetailsScreenBinding binding = DataBindingUtil.setContentView(this, R.layout.details_screen);
        binding.setViewModel(detailsViewViewModel);
        binding.setMediaItem(detailsViewViewModel.uiState().getValue().selectedMediaItem());
        binding.mediaImage.setImageURI(detailsViewViewModel.uiState().getValue().selectedMediaItem().getImageUri());


        //Observe ListView UiState changes
        detailsViewViewModel.uiState().observe(this, uiState -> {

            if (uiState.showDeleteConfirmation()) {
                showConfirmDeletionDialog();
            }

            if (uiState.dismissDetails()) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showConfirmDeletionDialog() {
        ConfirmDeletionFragment optionsFragment = new ConfirmDeletionFragment();
        optionsFragment.show(getSupportFragmentManager(), "ConfirmDeletionFragment");
    }
}