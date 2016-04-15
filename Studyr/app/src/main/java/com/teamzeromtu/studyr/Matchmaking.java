package com.teamzeromtu.studyr;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Callbacks.NoOp;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Tasks.GetSimilar;
import com.teamzeromtu.studyr.Tasks.MatchUser;
import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;
import com.teamzeromtu.studyr.Tasks.RejectUser;
import com.teamzeromtu.studyr.ViewAdapters.MatchmakingAdapter;

import java.util.ArrayList;

public class Matchmaking extends StudyrActivity {
    class MatchSetter implements HttpRequestCallback<ArrayList<User>> {
        @Override
        public void onSuccess(ArrayList<User> matches) {
            Log.d("ProfileReadWrite", "Success");

            matchAdapter.addMatches( matches );
        }

        @Override
        public void onCancel() {
            Log.d("ProfileReadWrite", "Cancel");
        }

        @Override
        public void onError(Exception error) {
            Log.d("ProfileReadWrite", "Error");
        }
    }
    private MatchmakingAdapter matchAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ProfileRead", "onCreate");
        Log.d("Fling", "onCreate()");

        setContentView(R.layout.activity_matchmaking_matches);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        SwipeFlingAdapterView swipeView = (SwipeFlingAdapterView)findViewById(R.id.matchmakingSwipeView);
        swipeView.setFlingListener( new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("Fling", "removeFirstObjectInAdapter()");
                matchAdapter.pop();
            }

            @Override
            public void onLeftCardExit(Object o) {
                final User user = (User)o;
                StudyrApplication app = (StudyrApplication)getApplication();
                RejectUser task = new RejectUser(app, user.getUserID(), new NoOp<User>());
                app.taskManager.execute( task );
            }

            @Override
            public void onRightCardExit(Object o) {
                Log.d("Fling", "onRightCardExit()");
                final User user = (User)o;
                StudyrApplication app = (StudyrApplication)getApplication();
                MatchUser task = new MatchUser(app, user.getUserID(), new NoOp<User>());
                app.taskManager.execute( task );
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                Log.d("Fling", "onAdapterAboutToEmpty()");
                // This function gets called too frequently.
                loadMatches();
            }

            @Override
            public void onScroll(float v) {
            }
        });


        matchAdapter = new MatchmakingAdapter(this);
        swipeView.setAdapter( matchAdapter );

        //loadMatches();
    }

    private void loadMatches() {
        Log.d("Fling", "loadMatches()");
        StudyrApplication app = (StudyrApplication)getApplication();
        final GetSimilar getProfile = new GetSimilar(app, new MatchSetter());

        NetworkTaskManager manager = app.taskManager;
        manager.execute( getProfile );
    }
}
