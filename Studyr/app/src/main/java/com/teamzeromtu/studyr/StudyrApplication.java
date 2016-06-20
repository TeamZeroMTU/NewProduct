package com.teamzeromtu.studyr;

import android.app.Application;

import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;

/**
 * Created by jbdaley on 3/22/16.
 */
public class StudyrApplication extends Application {
    public User user = null;
    public NetworkTaskManager taskManager = new NetworkTaskManager();
    public String backendBaseURL() { return getString(R.string.studyrBaseURL); };
}
