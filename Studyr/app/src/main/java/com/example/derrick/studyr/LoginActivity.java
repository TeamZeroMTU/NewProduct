package com.example.derrick.studyr;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private AsyncTask<Context, Void, Boolean>  loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameField = (EditText)findViewById(R.id.usernameEditText);
        passwordField = (EditText)findViewById(R.id.passwordEditText);
        loginTask = null;
    }

    public void submitLogin(View view)
    {
        if(loginTask != null) { // If we're already loging in, short circuit
            return;
        }
        final String username = usernameField.getText().toString();
        final String password = passwordField.getText().toString();
        loginTask = new UserLoginTask( username, password );
        loginTask.execute(this);
    }

    public class UserLoginTask extends AsyncTask<Context, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private Context mContext;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Context... params) {
            mContext = params[0];
            // TODO: login stuff goes here
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            loginTask = null;

            if (success) {
                Intent change = new Intent(mContext, Home.class);
                startActivity(change);
                finish();
            } else {
                passwordField.setError(getString(R.string.error_incorrect_password));
                passwordField.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            loginTask = null;
        }
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
}
