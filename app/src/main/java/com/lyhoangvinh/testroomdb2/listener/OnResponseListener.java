package com.lyhoangvinh.testroomdb2.listener;

import android.support.annotation.NonNull;

public interface OnResponseListener<T> {
    void onRespond(@NonNull T t);
}
