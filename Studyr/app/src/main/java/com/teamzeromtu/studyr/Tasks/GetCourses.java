package com.teamzeromtu.studyr.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Data.Course;

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
 * Created by jbdaley on 3/22/16.
 */
public class GetCourses extends AsyncTask<Void, Void, ArrayList<Course>> {

    private String school;
    private HttpRequestCallback<ArrayList<Course>> callback;
    public GetCourses(String school, HttpRequestCallback<ArrayList<Course>> dataCallback) {
        this.school = school;
        this.callback = dataCallback;
    }
    @Override
    protected ArrayList<Course> doInBackground(Void... params) {
        StringBuilder responseBuilder = new StringBuilder();
        try {
            Log.d("GetCourses:id", school);
            URL url = new URL("http://jeremypi.duckdns.org/s/" + school + "/courses");
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
            int code = connection.getResponseCode();
            if(code >= 200 && code < 300) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input;
                while ((input = reader.readLine()) != null) {
                    responseBuilder.append(input);
                }
                final String jsonString = responseBuilder.toString();
                Log.d("GetCourses:jsonString", jsonString);
                return new Gson().fromJson(jsonString, new TypeToken<ArrayList<Course>>(){}.getType());
            }
        } catch (Exception e) {
            Log.e("GetCourses:e", Log.getStackTraceString( e ));
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Course> result) {
        if(result == null) {
            Log.d("GetCourses:user", "Null courses");
            callback.onError(null);
        } else {
            Log.d("GetCourses:", "size: " + result.size());
            callback.onSuccess( result );
        }
    }
}
