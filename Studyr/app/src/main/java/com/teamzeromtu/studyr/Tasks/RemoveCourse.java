package com.teamzeromtu.studyr.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Data.Course;
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
public class RemoveCourse extends AsyncTask<Void, Void, User> {

    private HttpRequestCallback<User> callback;
    private StudyrApplication app;
    private Course course;
    public RemoveCourse(HttpRequestCallback<User> dataCallback, StudyrApplication app, Course course) {
        this.callback = dataCallback;
        this.app = app;
        this.course = course;
    }
    @Override
    protected User doInBackground(Void... params) {
        StringBuilder responseBuilder = new StringBuilder();
        try {
            String id = app.userId;
            URL url = new URL("http://jeremypi.duckdns.org/u/" + id + "/removecourse");
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
            Log.d("RemoveCourse:token", AccessToken.getCurrentAccessToken().getToken());
            sb.append("&");
            sb.append(URLEncoder.encode("name", "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(course.getName(), "UTF-8"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            Log.d("RemoveCourse:code", Integer.toString( connection.getResponseCode() ));
            int code = connection.getResponseCode();
            if(code >= 200 && code < 300) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input;
                while ((input = reader.readLine()) != null) {
                    responseBuilder.append(input);
                }
                final String jsonString = responseBuilder.toString();
                Log.d("RemoveCourse:jsonString", jsonString);
                return new Gson().fromJson(jsonString, User.class);
            }
        } catch (Exception e) {
            Log.e("RemoveCourse:e", Log.getStackTraceString( e ));
        }
        return null;
    }

    @Override
    protected void onPostExecute(User result) {
        if(result == null) {
            Log.d("RemoveCourse", "Null id");
            callback.onError(null);
        } else {
            Log.d("RemoveCourse", "success");
            callback.onSuccess( result );
        }
    }
}
