package com.lyhoangvinh.testroomdb2.view;

import com.lyhoangvinh.testroomdb2.model.User;

import java.util.List;

public interface UserView extends BaseView {
    void getAllData(List<User> list);
}
