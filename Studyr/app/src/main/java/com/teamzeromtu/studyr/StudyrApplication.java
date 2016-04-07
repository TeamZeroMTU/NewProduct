package com.teamzeromtu.studyr;

import android.app.Application;

import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;

/**
 * Created by jbdaley on 3/22/16.
 */
public class StudyrApplication extends Application {
    public String userId = null;
    public NetworkTaskManager taskManager = new NetworkTaskManager();
}
