package com.lyhoangvinh.testroomdb2.presenter;

import android.content.Context;

import com.lyhoangvinh.testroomdb2.dao.UserDao;
import com.lyhoangvinh.testroomdb2.database.DatabaseManager;
import com.lyhoangvinh.testroomdb2.model.User;
import com.lyhoangvinh.testroomdb2.view.BaseView;

import java.util.List;

import lombok.Getter;

public class BasePresenter<V extends BaseView> {

    @Getter
    private V view;

    private Context context;

    public UserDao getDatabaseManager(){
        return DatabaseManager.getAppDatabase(context).getUserDao();
    }

    public BasePresenter(V view, Context context) {
        this.view = view;
        this.context = context;
    }

    public List<User> getAllUser() {
        return DatabaseManager.getAppDatabase(context).getUserDao().getAll();
    }
}
