package com.example.mis_java_project.map;

import com.google.android.gms.maps.model.CameraPosition;

import java.util.List;

public record MapViewUiState(List<MarkerInfo> markerInfos, CameraPosition cameraPosition,
                             Boolean shouldUpdateMarker, Boolean shouldUpdateCamera) {
    public MapViewUiState copy(List<MarkerInfo> list, CameraPosition cameraPosition, Boolean shouldUpdateMarker, Boolean shouldUpdateCamera) {
        return new MapViewUiState(list != null ? list : this.markerInfos, cameraPosition != null ? cameraPosition : this.cameraPosition, shouldUpdateMarker, shouldUpdateCamera);
    }
}

