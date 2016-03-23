package com.teamzeromtu.studyr.Callbacks;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.teamzeromtu.studyr.StudyrApplication;

/**
 * Created by jbdaley on 3/22/16.
 */
public class AppUserIdSetter implements FacebookCallback<String> {
    StudyrApplication app;
    public AppUserIdSetter(StudyrApplication app) {
        this.app = app;
    }
    @Override
    public void onSuccess(String s) {
        app.userId = s;
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }
}
