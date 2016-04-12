package com.teamzeromtu.studyr.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Data.Course;
import com.teamzeromtu.studyr.Data.Message;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Exceptions.InvalidUserException;
import com.teamzeromtu.studyr.R;

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
public class GetMessages extends AsyncTask<Void, Void, ArrayList<Message>> {//public class GetUser extends AsyncTask<Void, Void, User> {

    private String id;
    private String otherId;
    private HttpRequestCallback<ArrayList<Message>> callback;

    public GetMessages(String newID,String otherGuysID, HttpRequestCallback<ArrayList<Message>> dataCallback) {
        id = newID;
        otherId = otherGuysID;
        callback = dataCallback;
    }
    public GetMessages(String newID, HttpRequestCallback<ArrayList<Message>> dataCallback) {
        id = newID;
        otherId = "10204805470411699";
        callback = dataCallback;
    }
    @Override
    protected ArrayList<Message> doInBackground(Void... params) {
        StringBuilder responseBuilder = new StringBuilder();
        try {
            Log.d("ExMsgSrvr:id", id);
            URL url = new URL("http://jeremypi.duckdns.org/u/" + id + "/getmessages");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            sb.append(URLEncoder.encode("recid", "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(otherId, "UTF-8"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            int response = connection.getResponseCode();
            if (response == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input;
                while ((input = reader.readLine()) != null) {
                    responseBuilder.append(input);
                }
                final String jsonString = responseBuilder.toString();
                Log.d("AppUserId:jsonString", jsonString);
                ArrayList<Message> newMessages = new Gson().fromJson(jsonString, new TypeToken<ArrayList<Message>>(){}.getType());
                return newMessages;
                //return true;
            }
        } catch (Exception e) {
            Log.e("ExMsgSrvr:e", Log.getStackTraceString( e ));
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Message> result) {
        if(result == null) {
            Log.d("AppUserId:id", "Null id");
            callback.onError(new InvalidUserException("") );
        } else {
            //Log.d("AppUserId:id", result);
            callback.onSuccess( result );
        }
    }
}
