package com.teamzeromtu.studyr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.teamzeromtu.studyr.Callbacks.AppUserIdSetter;
import com.teamzeromtu.studyr.Tasks.AppUserId;
import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker tokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        final Activity thisActivity = this;
        tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken != null) {
                    StudyrApplication app = (StudyrApplication)getApplication();
                    AppUserId getter = new AppUserId(new AppUserIdSetter(thisActivity, (StudyrApplication)getApplication()));
                    NetworkTaskManager manager = app.taskManager;
                    manager.execute( getter );
                }
            }
        };
        tokenTracker.startTracking();
        callbackManager = CallbackManager.Factory.create();

        if(com.facebook.Profile.getCurrentProfile() != null) {
            StudyrApplication app = (StudyrApplication)getApplication();
            AppUserId getter = new AppUserId(new AppUserIdSetter(this, app));

            NetworkTaskManager manager = app.taskManager;
            manager.execute( getter );
        }

        setContentView(R.layout.activity_login);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        class StudyrFacebookLogin implements FacebookCallback<LoginResult> {
            private final Activity mActivity;
            StudyrFacebookLogin(Activity activity) {
                mActivity = activity;
            }
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login", "Success");
                StudyrApplication app = (StudyrApplication)getApplication();
                AppUserId getter = new AppUserId(new AppUserIdSetter(mActivity, (StudyrApplication)getApplication()));
                NetworkTaskManager manager = app.taskManager;
                manager.execute(getter);
            }

            @Override
            public void onCancel() {
                Log.d("Login", "Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Login", "Error");
            }
        }
        loginButton.registerCallback(callbackManager, new StudyrFacebookLogin(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tokenTracker.stopTracking();
    }
}
