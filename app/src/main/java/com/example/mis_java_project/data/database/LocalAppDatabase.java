package com.example.mis_java_project.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mis_java_project.data.model.MediaItem;

@Database(entities = {MediaItem.class}, version = 1)
@TypeConverters({LatLngConverter.class}) // Register the converter here
public abstract class LocalAppDatabase extends RoomDatabase {
    public abstract MediaItemDao mediaItemDao();

    private static LocalAppDatabase INSTANCE;

    public static LocalAppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalAppDatabase.class, "media_database")
                    .build();
        }
        return INSTANCE;
    }
}
