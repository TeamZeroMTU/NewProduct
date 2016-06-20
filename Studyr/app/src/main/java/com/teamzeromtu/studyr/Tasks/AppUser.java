package com.teamzeromtu.studyr.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Exceptions.InvalidUserException;
import com.teamzeromtu.studyr.StudyrApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by jbdaley on 3/22/16.
 */
public class AppUser extends AsyncTask<Void, Void, User> {

    private StudyrApplication app;
    private HttpRequestCallback<User> callback;
    public AppUser(StudyrApplication app, HttpRequestCallback<User> dataCallback) {
        this.app = app;
        this.callback = dataCallback;
    }
    @Override
    protected User doInBackground(Void... params) {
        StringBuilder responseBuilder = new StringBuilder();
        try {
            Log.d("AppUser", "Starting http request construction");
            URL url = new URL("http://" + app.backendBaseURL() + "/me/user");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            sb.append(URLEncoder.encode("token", "UTF-8"));
            sb.append("=");
            Log.d("AppUser:token", AccessToken.getCurrentAccessToken().getToken());
            sb.append(URLEncoder.encode(AccessToken.getCurrentAccessToken().getToken(), "UTF-8"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            int code = connection.getResponseCode();
            Log.d("AppUser:code", Integer.toString( code ));
            if(code >= 200 && code < 300) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input;
                while ((input = reader.readLine()) != null) {
                    responseBuilder.append(input);
                }
                final String jsonString = responseBuilder.toString();
                Log.d("AppUser:jsonString", jsonString);
                return new Gson().fromJson(jsonString, User.class);
            }
        } catch (Exception e) {
            Log.e("AppUser:e", Log.getStackTraceString( e ));
        }
        return null;
    }

    @Override
    protected void onPostExecute(User result) {
        if(result == null) {
            Log.d("AppUser:user", "Null user");
            callback.onError(new InvalidUserException("") );
        } else {
            Log.d("AppUser:user", "Successful login");
            callback.onSuccess( result );
        }
    }
}
