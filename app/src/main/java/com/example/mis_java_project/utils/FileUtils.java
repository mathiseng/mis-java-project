package com.example.mis_java_project.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.exif.GpsDirectory;
import com.google.android.gms.maps.model.LatLng;

import java.io.InputStream;

public class FileUtils {

    @SuppressLint("Range")
    public static String getFileName(Uri uri, Context context) {
        if (uri == null) {
            return null;
        }
        ContentResolver contentResolver = context.getContentResolver();
        try (Cursor cursor = contentResolver.query(uri, null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (!cursor.isNull(displayNameIndex)) {
                    String fileName = cursor.getString(displayNameIndex);
                    return fileName.split("\\.")[0];
                } else {
                    return "";
                }
            } else {
                return "";
            }
        }
    }

    public static LatLng extractLocationFromFile(Uri uri, Context context) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            var metadata = ImageMetadataReader.readMetadata(inputStream);

            var gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            var latitude = gpsDirectory.getGeoLocation().getLatitude();
            var longitude = gpsDirectory.getGeoLocation().getLongitude();

            return new LatLng(latitude, longitude);
        } catch (Exception Ignored) {
        }
        return null;
    }
}
