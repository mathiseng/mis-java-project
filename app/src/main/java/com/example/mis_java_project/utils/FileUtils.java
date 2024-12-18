package com.example.mis_java_project.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

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
}
