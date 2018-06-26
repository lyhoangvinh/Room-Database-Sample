package com.lyhoangvinh.testroomdb2.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lyhoangvinh.testroomdb2.listener.OnResponseListener;
import com.lyhoangvinh.testroomdb2.model.User;
import com.lyhoangvinh.testroomdb2.view.UserView;

import java.util.List;

public class UserPresenter extends BasePresenter<UserView> {

    public UserPresenter(UserView view, Context context) {
        super(view, context);
    }

    public void getAddData() {
        addRequestFollowable(true, getDatabaseManager().getAllUserFlowable(), new OnResponseListener<List<User>>() {
            @Override
            public void onRespond(@NonNull List<User> list) {
                if (getView() != null) {
                    getView().getAllData(list);
                }
            }
        });

//        addRequestSingle(true, getDatabaseManager().getAllUserSingle(), new OnResponseListener<List<User>>() {
//            @Override
//            public void onRespond(@NonNull List<User> list) {
//                if (getView() != null) {
//                    getView().getAllData(list);
//                }
//            }
//        });
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

    public void editUser(User user) {
        getDatabaseManager().update(user);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                getAddData();
            }
        }, 300);
    }

    public void deleteUser(User user) {
        getDatabaseManager().delete(user);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAddData();
            }
        }, 300);
    }
}
