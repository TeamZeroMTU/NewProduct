package com.teamzeromtu.studyr.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.google.gson.Gson;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.R;

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
public class SendNewMessage extends AsyncTask<Void, Void, Boolean> {//public class GetUser extends AsyncTask<Void, Void, User> {

    private String id;
    private String newMessage;
    private ListView listView;
    private String theirID;

    public SendNewMessage(String newID, String msg, ListView list, String otherGuysId) {
        newMessage = msg;
        listView = list;
        id = newID;
        this.theirID = otherGuysId;
    }
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String otherGuysID = theirID;
            Log.d("ExMsgSrvr:id", id);
            URL url = new URL("http://jeremypi.duckdns.org/u/" + id + "/sendmessage");///u/:id/getmessages
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            sb.append(URLEncoder.encode("text", "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(newMessage, "UTF-8"));
            sb.append("&");
            sb.append(URLEncoder.encode("recid", "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(otherGuysID, "UTF-8"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            int response = connection.getResponseCode();
            if (response == 200) {
                return true;
            }
        } catch (Exception e) {
            Log.e("ExMsgSrvr:e", Log.getStackTraceString( e ));
        }
        /*try {
            Log.d("ExMsgSrvr:id", id);
            URL url = new URL("http://jeremypi.duckdns.org/u/" + id + "/getmessages");///u/:id/getmessages
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
            sb.append(URLEncoder.encode("5", "UTF-8"));
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            os.close();
            connection.connect();
            int response = connection.getResponseCode();
            if(response == 201) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String input;
                while ((input = reader.readLine()) != null) {
                    responseBuilder.append(input);
                }
                final String jsonString = responseBuilder.toString();
                Log.d("ExMsgSrvr:jsonString", jsonString);
                return new Gson().fromJson(jsonString, User.class);
            }
        } catch (Exception e) {
            Log.e("ExMsgSrvr:e", Log.getStackTraceString( e ));
        }*/
        //listView.ap
        //String[] bob = new String[4];
        //ArrayAdapter adapter = new ArrayAdapter<String>(this , listView,bob);
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result) {
            return;
        }
    }
}
