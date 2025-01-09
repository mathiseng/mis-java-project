package com.example.mis_java_project.map;

import com.example.mis_java_project.data.model.MediaItem;
import com.google.android.gms.maps.model.MarkerOptions;

public record MarkerInfo(MediaItem mediaItem, MarkerOptions markerOption) {
}
