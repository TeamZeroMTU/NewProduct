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
public class MatchUser extends AsyncTask<Void, Void, User> {

    private StudyrApplication app;
    private String matchId;
    private HttpRequestCallback<User> callback;
    public MatchUser(StudyrApplication app, String matchId, HttpRequestCallback<User> dataCallback) {
        this.app = app;
        this.callback = dataCallback;
        this.matchId = matchId;
    }
    @Override
    protected User doInBackground(Void... params) {
        StringBuilder responseBuilder = new StringBuilder();
        try {
            URL url = new URL("http://" + app.backendBaseURL() + "/u/" + app.user.getUserID() + "/match");
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
            sb.append("&");
            sb.append(URLEncoder.encode("matchId", "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(matchId, "UTF-8"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            int code = connection.getResponseCode();
            if(code >= 200 && code < 300) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input;
                while ((input = reader.readLine()) != null) {
                    responseBuilder.append(input);
                }
                final String jsonString = responseBuilder.toString();
                Log.d("MatchUser:jsonString", jsonString);
                return new Gson().fromJson(jsonString, User.class);
            }
            Log.d("MatchUser:code", "Bad code!");
        } catch (Exception e) {
            Log.e("MatchUser:e", Log.getStackTraceString( e ));
        }
        return null;
    }

    @Override
    protected void onPostExecute(User result) {
        if(result == null) {
            Log.d("MatchUser:id", "Null id");
            callback.onError(new InvalidUserException("") );
        } else {
            callback.onSuccess( result );
        }
    }
}
