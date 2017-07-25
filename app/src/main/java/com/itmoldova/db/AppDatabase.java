package com.itmoldova.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.itmoldova.model.Item;

@Database(entities = Item.class, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context, AppDatabase.class, "items")
                    .allowMainThreadQueries() // TODO: remove
                    .build();
        }
        return instance;
    }

    public abstract ItemDao itemModel();

    // TODO: provide the instance by Dagger and then the instance will be application wide
    public void destroyInstance() {
        instance = null;
    }

}
