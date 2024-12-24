package com.example.mis_java_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.mis_java_project.databinding.FragmentMapBinding;


public class MapFragment extends Fragment {
    FragmentMapBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);

        getActivity().setTitle("Map");

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }
}