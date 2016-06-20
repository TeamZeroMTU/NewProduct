package com.teamzeromtu.studyr.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Data.User;
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
public class GetUser extends AsyncTask<Void, Void, User> {

    private StudyrApplication app;
    private String id;
    private HttpRequestCallback<User> callback;
    public GetUser( StudyrApplication app, String id, HttpRequestCallback<User> dataCallback) {
        this.app = app;
        this.id = id;
        this.callback = dataCallback;
    }
    @Override
    protected User doInBackground(Void... params) {
        StringBuilder responseBuilder = new StringBuilder();
        try {
            Log.d("GetUser:id", id);
            URL url = new URL("http://" + app.backendBaseURL() + "/u/" + id + "/info");
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
            sb.append(URLEncoder.encode(AccessToken.getCurrentAccessToken().getToken(), "UTF-8"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            if(connection.getResponseCode() == 201) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input;
                while ((input = reader.readLine()) != null) {
                    responseBuilder.append(input);
                }
                final String jsonString = responseBuilder.toString();
                Log.d("GetUser:jsonString", jsonString);
                return new Gson().fromJson(jsonString, User.class);
            }
        } catch (Exception e) {
            Log.e("GetUser:e", Log.getStackTraceString( e ));
        }
        return null;
    }

    @Override
    protected void onPostExecute(User result) {
        if(result == null) {
            Log.d("GetUser:user", "Null user");
            callback.onError(null);
        } else {
            Log.d("GetUser:user", result.getUserID());
            callback.onSuccess( result );
        }
    }
}
