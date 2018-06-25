package com.lyhoangvinh.testroomdb2.presenter;

import android.content.Context;

import com.lyhoangvinh.testroomdb2.model.User;
import com.lyhoangvinh.testroomdb2.view.UserView;

public class UserPresenter extends BasePresenter<UserView> {

    public UserPresenter(UserView view, Context context) {
        super(view, context);
    }

    public void getAddData() {
        if (getView() != null) {
            getView().getAllData(getAllUser());
        }
    }

    public void addUser(User user) {
        getDatabaseManager().insertAll(user);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAddData();
            }
        }, 300);
    }

    public void editUser(User user){
        getDatabaseManager().update(user);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAddData();
            }
        }, 300);
    }

    public void deleteUser(User user){
        getDatabaseManager().delete(user);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAddData();
            }
        }, 300);
    }
}
