package com.example.mis_java_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mis_java_project.data.model.MediaItem;
import com.example.mis_java_project.databinding.FragmentMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    FragmentMapBinding binding;

    MapViewViewModel mapViewViewModel;

    GoogleMap googleMap;

    Map<Marker, MediaItem> markerMediaItemMap = new HashMap<>();


    @Override
    public void onResume() {
        super.onResume();
        // Invalidate the options menu to trigger the re-creation of the toolbar menu
        requireActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mapViewViewModel = new ViewModelProvider(requireActivity()).get(MapViewViewModel.class);

        //Binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
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

    // Get a handle to the GoogleMap object and display marker.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53, 13), 8));

        //When reloading the fragment because we already visited the map we initially want to load marker and data
        mapViewViewModel.uiState().getValue().markerInfos().forEach(markerInfo -> {
            Marker marker = googleMap.addMarker(markerInfo.markerOption());
            markerMediaItemMap.put(marker, markerInfo.mediaItem());
        });

        mapViewViewModel.uiState().observe(getViewLifecycleOwner(), mapViewUiState -> {
            //To prevent unneccessary updates i added flags for updating only markers or cameraPosition.
            if (mapViewUiState.shouldUpdateMarker()) {

                //To remove marker in an efficient way and not redrawing the whole map we compare values of new items and existing ones
                Iterator<Map.Entry<Marker, MediaItem>> iterator = markerMediaItemMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Marker, MediaItem> entry = iterator.next();
                    // Check if the entry's MediaItem exists in the markerInfos
                    boolean mediaItemExists = mapViewUiState.markerInfos().stream()
                            .anyMatch(markerInfo -> markerInfo.mediaItem().equals(entry.getValue()));

                    // If the mediaItem doesn't exist in the current markerInfos, remove the marker
                    if (!mediaItemExists) {
                        entry.getKey().remove();  // Remove the marker from the map
                        iterator.remove();        // Remove the entry from the map
                    }
                }
            }

            if (mapViewUiState.shouldUpdateCamera() && mapViewUiState.cameraPosition() != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapViewUiState.cameraPosition().target, mapViewUiState.cameraPosition().zoom));
            }
        });

        googleMap.setOnInfoWindowClickListener(marker -> {
            MediaItem mediaItem = markerMediaItemMap.get(marker);
            if (mediaItem != null) {
                mapViewViewModel.onSelectMarker(mediaItem);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetailsFragment()).addToBackStack(null).commit();
            }
        });

        googleMap.setOnCameraIdleListener(() -> {
            CameraPosition cameraPosition = googleMap.getCameraPosition();
            mapViewViewModel.updateRecentCameraPosition(cameraPosition);
        });
    }
}