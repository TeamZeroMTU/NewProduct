package com.teamzeromtu.studyr.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.ArrayList;

/**
 * Created by Derrick on 4/4/2016.
 */
public class GetSimilar extends AsyncTask<Void, Void, ArrayList<User>> {
    private StudyrApplication app;
    private HttpRequestCallback<ArrayList<User>> callback;
    public GetSimilar(StudyrApplication app, HttpRequestCallback<ArrayList<User>> callback) {
        this.app = app;
        this.callback = callback;
    }
    @Override
    protected ArrayList<User> doInBackground(Void... params) {
        StringBuilder responseBuilder = new StringBuilder();
        try {
            Log.d("GetSimilar", "doInBackground()" );
            URL url = new URL("http://" + app.backendBaseURL() + "/u/" + app.user.getUserID() + "/similar");
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
            Log.d("GetSimilar:code", Integer.toString(connection.getResponseCode()) );
            int code = connection.getResponseCode();
            if(code >= 200 && code < 300) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input;
                while ((input = reader.readLine()) != null) {
                    responseBuilder.append(input);
                }
                final String jsonString = responseBuilder.toString();
                Log.d("GetSimilar:jsonString", jsonString);
                return new Gson().fromJson(jsonString, new TypeToken<ArrayList<User>>(){}.getType());
            }
        } catch (Exception e) {
            Log.e("GetSimilar:e", Log.getStackTraceString( e ));
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<User> result) {
        if(result == null) {
            Log.d("GetSimilar:user", "Null user");
            callback.onError(null);
        } else {
            //Log.d("GetMatches:user", result.getUserID());
            callback.onSuccess(result);
        }
    }
}
