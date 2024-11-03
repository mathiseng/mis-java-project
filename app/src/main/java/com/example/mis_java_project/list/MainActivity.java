package com.example.mis_java_project.list;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mis_java_project.R;
import com.example.mis_java_project.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_main);

        //Setup Databinding
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.mediaItemList.setLayoutManager(new LinearLayoutManager(this));

        ListViewViewModel mediaItemViewModel = new ViewModelProvider(this).get(ListViewViewModel.class);

        MediaItemListAdapter adapter = new MediaItemListAdapter(new ArrayList<>());
        binding.mediaItemList.setAdapter(adapter);

        mediaItemViewModel.uiState().observe(this, listViewUiState -> {
            adapter.setMediaItems(listViewUiState.mediaItemList());
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}