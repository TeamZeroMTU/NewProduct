package com.teamzeromtu.studyr.Callbacks;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Exceptions.InvalidUserException;
import com.teamzeromtu.studyr.R;
import com.teamzeromtu.studyr.StudyrApplication;
import com.teamzeromtu.studyr.Tasks.CreateUser;
import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;

/**
 * Created by jbdaley on 3/22/16.
 * This class is highly coupled for usage with the LoginActivity...
 */
public class AppUserSetter implements HttpRequestCallback<User> {
    Activity activity;
    StudyrApplication app;

    private int attempts = 0;
    public AppUserSetter(Activity activity, StudyrApplication app) {
        this.activity = activity;
        this.app = app;
    }
    @Override
    public void onSuccess(User s) {
        app.user = s;
        new GoHome<>(activity).onSuccess(null);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(Exception error) {
        TextView errorView = (TextView)activity.findViewById(R.id.loginErrorView);
        if(error != null && error.getMessage() != null) {
            errorView.setText(error.getMessage());
            Log.d("AppUserSetter", error.getMessage());
        }
        if(error instanceof InvalidUserException && attempts < 3) {
            attempts += 1;
            CreateUser task = new CreateUser(app, this);
            NetworkTaskManager manager = app.taskManager;
            manager.execute( task );
        }
    }
}
