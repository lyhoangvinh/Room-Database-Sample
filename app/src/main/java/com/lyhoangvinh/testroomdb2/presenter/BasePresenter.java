package com.lyhoangvinh.testroomdb2.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lyhoangvinh.testroomdb2.dao.UserDao;
import com.lyhoangvinh.testroomdb2.database.DatabaseManager;
import com.lyhoangvinh.testroomdb2.listener.Destroyable;
import com.lyhoangvinh.testroomdb2.listener.OnResponseListener;
import com.lyhoangvinh.testroomdb2.model.User;
import com.lyhoangvinh.testroomdb2.utils.RxUtil;
import com.lyhoangvinh.testroomdb2.view.BaseView;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import lombok.Getter;

public class BasePresenter<V extends BaseView> implements Destroyable {

    @Getter
    private V view;

    private Context context;

    private CompositeDisposable mCompositeDisposable;

    public UserDao getDatabaseManager() {
        return DatabaseManager.getAppDatabase(context).getUserDao();
    }

    public BasePresenter(V view, Context context) {
        this.view = view;
        this.context = context;
        mCompositeDisposable = new CompositeDisposable();
    }

    public List<User> getAllUser() {
        return DatabaseManager.getAppDatabase(context).getUserDao().getAll();
    }

    protected <T> void addRequestFollowable(
            Flowable<T> request, final boolean showProgress,
            final boolean forceResponseWithoutCheckNullView,
            @Nullable final OnResponseListener<T> responseConsumer) {

        boolean shouldUpdateUI = showProgress || responseConsumer != null;

        if (showProgress && getView() != null) {
            getView().showLoading();
        }

        Disposable disposable = RxUtil.makeRequestFollowable(request, shouldUpdateUI, new OnResponseListener<T>() {
            @Override
            public void onRespond(@NonNull T t) {
                if (responseConsumer != null && (forceResponseWithoutCheckNullView || getView() != null)) {
                    responseConsumer.onRespond(t);
                }
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                if (showProgress && getView() != null) {
                    getView().hideLoading();
                }

            }
        });

        if (mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    protected <T> void addRequestFollowable(boolean showProgress, Flowable<T> request,
                                            @Nullable OnResponseListener<T> responseConsumer) {
        addRequestFollowable(request, showProgress, false, responseConsumer);
    }

    protected <T> void addRequestSingle(
            Single<T> request, final boolean showProgress,
            final boolean forceResponseWithoutCheckNullView,
            @Nullable final OnResponseListener<T> responseConsumer){
        boolean shouldUpdateUI = showProgress || responseConsumer != null;

        if (showProgress && getView() != null) {
            getView().showLoading();
        }

        Disposable disposable = RxUtil.makeRequstSingle(request, shouldUpdateUI, new OnResponseListener<T>() {
            @Override
            public void onRespond(@NonNull T t) {
                if (responseConsumer != null && (forceResponseWithoutCheckNullView || getView() != null)) {
                    responseConsumer.onRespond(t);
                }
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                if (showProgress && getView() != null) {
                    getView().hideLoading();
                }

            }
        });

        if (mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    protected <T> void addRequestSingle(boolean showProgress, Single<T> request,
                                            @Nullable OnResponseListener<T> responseConsumer) {
        addRequestSingle(request, showProgress, false, responseConsumer);
    }

    private void unbindView() {
        this.view = null;
    }

    @Override
    public void onDestroy() {
        unbindView();
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }
}
