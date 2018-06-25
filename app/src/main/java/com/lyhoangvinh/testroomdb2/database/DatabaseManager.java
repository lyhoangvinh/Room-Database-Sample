package com.lyhoangvinh.testroomdb2.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.lyhoangvinh.testroomdb2.dao.UserDao;
import com.lyhoangvinh.testroomdb2.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class DatabaseManager extends RoomDatabase {
    private static DatabaseManager INSTANCE;

    public abstract UserDao getUserDao();

    public static DatabaseManager getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), DatabaseManager.class, "user-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
