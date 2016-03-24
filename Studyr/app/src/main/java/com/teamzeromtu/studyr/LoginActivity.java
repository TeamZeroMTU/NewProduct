package com.teamzeromtu.studyr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.teamzeromtu.studyr.Callbacks.AppUserIdSetter;
import com.teamzeromtu.studyr.Tasks.AppUserId;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        //for testing purposes to skip past the login screen profile page won't work in this however
        if(com.facebook.Profile.getCurrentProfile() == null) {
       // if(com.facebook.Profile.getCurrentProfile() != null) {
            AppUserId getter = new AppUserId(new AppUserIdSetter(((StudyrApplication)getApplication())));
            getter.execute();
            Intent change = new Intent(this, Home.class);
            startActivity(change);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);


        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
        class StudyrFacebookLogin implements FacebookCallback<LoginResult> {
            private final Context mContext;
            StudyrFacebookLogin(Context context) {
                mContext = context;
            }
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login", "Success");
                AppUserId getter = new AppUserId(new AppUserIdSetter(((StudyrApplication)getApplication())));
                getter.execute();
                Intent change = new Intent(mContext, Home.class);
                startActivity(change);
                finish();
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
}
