package com.teamzeromtu.studyr;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.teamzeromtu.studyr.Data.Message;
import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;
import com.teamzeromtu.studyr.ViewAdapters.MessageAdapter;
import com.teamzeromtu.studyr.ViewAdapters.MessageController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageForm extends StudyrActivity {
    EditText editText;
    ListView list;
    public String matchName;
    private MessageController controller;
    private MessageAdapter adapter;
    private WebSocket ws;
    private NetworkTaskManager wsManager;

    private void createWebSocket() throws IOException {
        // Create a web socket factory and set 5000 milliseconds as a timeout
        // value for socket connection.
        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(15000);

        // Create a web socket. The timeout value set above is used.
        ws = factory
                .createSocket("ws://" + ((StudyrApplication)getApplication()).backendBaseURL() + "/chat")
                .addListener(new WebSocketAdapter() {
                    @Override
                    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                        Log.i("Websocket", "onDisconnnected");
                        onBackPressed();
                    }

                    @Override
                    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                        Log.i("Websocket", "onError");
                    }

                    private TypeToken messageColletion = new TypeToken<ArrayList<Message>>() {
                    };
                    private TypeAdapter<ArrayList<Message>> messageAdapter = new Gson().getAdapter(messageColletion);

                    @Override
                    public void onTextMessage(WebSocket websocket, String
                            message) throws IOException {
                        Log.i("Websocket", "onTextMessage: " + message);
                        final ArrayList<Message> msgs = messageAdapter.fromJson(message);
                        controller.addAll(msgs);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                list.setSelection(list.getCount());
                            }
                        });
                    }

                    @Override
                    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                        Log.i("Websocket", "onConnected");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                controller.clear();
                            }
                        });
                        sendSocketMessage(
                                ((StudyrApplication) getApplication()).user.getUserID(),
                                AccessToken.getCurrentAccessToken().getToken(),
                                (String) getIntent().getExtras().get("id"));
                    }
                });
    }

    private void connectWebSocket() {
        wsManager.execute(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    ws = ws.recreate().connect();
                } catch (WebSocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private void disconnectWebSocket() {
        wsManager.execute(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ws.disconnect();
                return null;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_form);
        wsManager = new NetworkTaskManager();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);

        matchName = (String) getIntent().getExtras().get("name");
        myToolbar.setTitle(matchName);
        setSupportActionBar(myToolbar);

        editText = (EditText) findViewById(R.id.editText);
        list = (ListView) findViewById(R.id.listView);
        StudyrApplication app = (StudyrApplication) getApplication();
        adapter = new MessageAdapter(MessageForm.this, R.layout.messege_overview_display, app.user.getUserID());
        list.setAdapter(adapter);
        controller = adapter.getController();
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendMessage();
                    handled = true;
                }
                hideInput(v);
                resetFocus();
                return handled;
            }
        });
        try {
            createWebSocket();
        } catch (IOException e) {
            e.printStackTrace();
            onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        Log.i("Websocket", "onStart()");
        super.onStart();
        connectWebSocket();
        resetFocus();
    }

    @Override
    protected void onStop() {
        Log.i("Websocket", "onStop()");
        super.onStop();
        disconnectWebSocket();
    }
    private void sendMessage() {
        sendSocketMessage( editText.getText().toString() );
        editText.setText("");
        resetFocus();
    }

    private void sendSocketMessage(String... str) {
        wsManager.execute(new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                for(String s: params) {
                    ws.sendText( s );
                }
                return null;
            }
        }, str);
    }

    private void resetFocus() {
        findViewById(R.id.activity_message_form_layout).requestFocus();
    }

    private void hideInput(View view) {
        hideInput(view.getApplicationWindowToken());
    }
    private void hideInput(IBinder binder) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
