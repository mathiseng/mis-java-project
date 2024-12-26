package com.example.mis_java_project.data.database;

import androidx.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;

public class LatLngConverter {

    @TypeConverter
    public static String fromLatLng(LatLng latLng) {
        if (latLng == null) {
            return null;
        }
        return latLng.latitude + "," + latLng.longitude;
    }

    @TypeConverter
    public static LatLng toLatLng(String latLngString) {
        if (latLngString == null || latLngString.isEmpty()) {
            return null;
        }
        String[] parts = latLngString.split(",");
        double latitude = Double.parseDouble(parts[0]);
        double longitude = Double.parseDouble(parts[1]);
        return new LatLng(latitude, longitude);
    }
}
