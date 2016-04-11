package com.teamzeromtu.studyr.Callbacks;

/**
 * Created by jbdaley on 4/10/16.
 */
public class NoOp<T> implements HttpRequestCallback<T> {
    @Override
    public void onSuccess(T t) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(Exception e) {

    }
}
