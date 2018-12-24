package com.lyhoangvinh.testroomdb2.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lyhoangvinh.testroomdb2.listener.OnResponseListener;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxUtil {

    public static <T> Disposable makeRequestFollowable(
            Flowable<T> request, boolean shouldUpdateUi,
            @NonNull final OnResponseListener<T> responseConsumer,
            @Nullable final Action onComplete) {

        Flowable<T> flyable = request.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io());
        if (shouldUpdateUi) {
            flyable = flyable.observeOn(AndroidSchedulers.mainThread());
        }

        return flyable.subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                responseConsumer.onRespond(t);
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
    }

    public static <T> Disposable makeRequestSingle(
            Single<T> request, boolean shouldUpdateUi,
            @NonNull final OnResponseListener<T> responseConsumer,
            @Nullable final Action onComplete) {

        Single<T> single = request.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io());
        if (shouldUpdateUi) {
            single = single.observeOn(AndroidSchedulers.mainThread());
        }
        return single.subscribe(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                responseConsumer.onRespond(t);
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
    }
}
