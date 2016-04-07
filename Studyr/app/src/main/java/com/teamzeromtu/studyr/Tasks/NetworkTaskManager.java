package com.teamzeromtu.studyr.Tasks;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by jbdaley on 4/7/16.
 */
public class NetworkTaskManager {
    private BlockingQueue<Runnable> tasks;
    Thread executor;
    public NetworkTaskManager() {
        tasks = new LinkedBlockingDeque<>();
        executor = new Thread( new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Runnable task = tasks.take();
                        Log.d("NetworkManager", "running a task...");
                        task.run();
                        Log.d("NetworkManager", "finished task");
                    } catch (Exception e) {
                    }
                }
            }
        });
        executor.start();
    }
    public synchronized  <Params, Progress, Result> void execute(@NonNull final AsyncTask<Params, Progress, Result> task, final Params... params) {
        Log.d("NetworkManager", "adding a task...");
        try {
            tasks.put(new Runnable() {
                @Override
                public void run() {
                    task.execute( params );
                }
            });
        } catch (InterruptedException e) {
            // this is really bad
        }
    }
}
