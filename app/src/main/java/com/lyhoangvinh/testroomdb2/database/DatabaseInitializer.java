package com.lyhoangvinh.testroomdb2.database;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lyhoangvinh.testroomdb2.model.User;

import java.util.List;

public class DatabaseInitializer {
    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final DatabaseManager db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final DatabaseManager db) {
        populateWithTestData(db);
    }

    private static User addUser(final DatabaseManager db, User user) {
        db.getUserDao().insertAll(user);
        return user;
    }

    private static void populateWithTestData(DatabaseManager db) {
        User user = new User();
        user.setFirstName("Ajay");
        user.setLastName("Saini");
        user.setAge(25);
        addUser(db, user);

        List<User> userList = db.getUserDao().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + userList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final DatabaseManager mDb;

        PopulateDbAsync(DatabaseManager db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}
