package com.example.mis_java_project.map;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mis_java_project.repository.MediaItemRepository;
import com.example.mis_java_project.repository.SharedStateRepository;
import com.example.mis_java_project.data.model.MediaItem;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapViewViewModel extends AndroidViewModel {

    private final MutableLiveData<MapViewUiState> uiState = new MutableLiveData<>();

    public LiveData<MapViewUiState> uiState() {
        return uiState;
    }

    MediaItemRepository mediaItemRepository;
    private final SharedStateRepository sharedStateRepository;


    public MapViewViewModel(Application application) {
        super(application);
        uiState.setValue(new MapViewUiState(new ArrayList<>(), new CameraPosition(new LatLng(53.0, 13.4), 8, 0, 0), false, false));

        //Observe MediaItemRepository and merge changes of list
        mediaItemRepository = MediaItemRepository.getInstance(application);
        sharedStateRepository = SharedStateRepository.getInstance();

        mediaItemRepository.mediaItems.observeForever(mediaItems -> {
            var newList = new ArrayList<MarkerInfo>();
            mediaItems.forEach(mediaItem -> {
                Log.d("TESTOO", "ITEM " + mediaItem.getImageLocation());

                var markerOption = new MarkerOptions().position(mediaItem.getImageLocation()).title(mediaItem.getTitle());
                newList.add(new MarkerInfo(mediaItem, markerOption));
            });

            uiState.postValue(uiState.getValue().copy(newList, null, true, false));
            Log.d("TESTOO", uiState.getValue().markerInfos().size() + " ");
        });
    }

    public void onSelectMarker(MediaItem mediaItem) {
        sharedStateRepository.onChangeSelectedMediaItem(mediaItem);
    }

    public void updateRecentCameraPosition(CameraPosition cameraPosition) {
        uiState.postValue(uiState.getValue().copy(null, cameraPosition, false, true));
    }
}
