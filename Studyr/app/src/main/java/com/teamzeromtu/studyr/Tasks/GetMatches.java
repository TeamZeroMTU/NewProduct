package com.teamzeromtu.studyr.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Data.User;

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
public class GetMatches extends AsyncTask<Void, Void, ArrayList<User>> {

    private String user;
    private HttpRequestCallback<ArrayList<User>> callback;
    public GetMatches(String user, HttpRequestCallback<ArrayList<User>> callback) {
        this.user = user;
        this.callback = callback;
    }
    @Override
    protected ArrayList<User> doInBackground(Void... params) {
        StringBuilder responseBuilder = new StringBuilder();
        try {
            Log.d("GetMatches:id", user);
            URL url = new URL("http://jeremypi.duckdns.org/u/" + user + "/matches");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            Log.d("GetMatches:code", Integer.toString(connection.getResponseCode()) );
            if(connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input;
                while ((input = reader.readLine()) != null) {
                    responseBuilder.append(input);
                }
                final String jsonString = responseBuilder.toString();
                Log.d("GetMatches:jsonString", jsonString);
                return new Gson().fromJson(jsonString, ArrayList.class);
            }
        } catch (Exception e) {
            Log.e("GetMatches:e", Log.getStackTraceString( e ));
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<User> result) {
        if(result == null) {
            Log.d("GetMatches:user", "Null user");
            callback.onError(null);
        } else {
            //Log.d("GetMatches:user", result.getUserID());
            callback.onSuccess(result);
        }
    }
}
