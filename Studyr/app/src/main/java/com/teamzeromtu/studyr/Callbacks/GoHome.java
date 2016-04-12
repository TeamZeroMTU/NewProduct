package com.teamzeromtu.studyr.Callbacks;

import android.app.Activity;
import android.content.Intent;

import com.teamzeromtu.studyr.Matchmaking;

/**
 * Created by jbdaley on 3/22/16.
 */
public class GoHome<T> implements HttpRequestCallback<T> {
    Activity activity;
    GoHome(Activity activity) {
        this.activity = activity;
    }
    @Override
    public void onSuccess(T t) {
        Intent change = new Intent(activity, Matchmaking.class);
        activity.startActivity(change);
        activity.finish();
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(Exception error) {

    }
}
