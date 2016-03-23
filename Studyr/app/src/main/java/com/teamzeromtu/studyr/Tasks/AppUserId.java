package com.teamzeromtu.studyr.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.google.gson.Gson;

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
public class AppUserId extends AsyncTask<Void, Void, String> {

    private FacebookCallback<String> callback;
    public AppUserId(FacebookCallback<String> dataCallback) {
        this.callback = dataCallback;
    }
    @Override
    protected String doInBackground(Void... params) {
        StringBuilder responseBuilder = new StringBuilder();
        try {
            URL url = new URL("http://jeremypi.duckdns.org/me/id");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            sb.append(URLEncoder.encode("userToken", "UTF-8"));
            sb.append("=");
            Log.d("AppUserId:token", AccessToken.getCurrentAccessToken().getToken());
            sb.append(URLEncoder.encode(AccessToken.getCurrentAccessToken().getToken(), "UTF-8"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            Log.d("AppUserId:code", Integer.toString( connection.getResponseCode() ));
            if(connection.getResponseCode() == 201) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input;
                while ((input = reader.readLine()) != null) {
                    responseBuilder.append(input);
                }
                final String jsonString = responseBuilder.toString();
                Log.d("AppUserId:jsonString", jsonString);
                return new Gson().fromJson(jsonString, String.class);
            }
        } catch (Exception e) {
            Log.e("AppUserId:e", Log.getStackTraceString( e ));
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if(result == null) {
            Log.d("AppUserId:id", "Null id");
            callback.onError(null);
        } else {
            Log.d("AppUserId:id", result);
            callback.onSuccess( result );
        }
    }
}
