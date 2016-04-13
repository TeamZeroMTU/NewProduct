package com.teamzeromtu.studyr;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.teamzeromtu.studyr.Callbacks.MessageFormSetter;
import com.teamzeromtu.studyr.Tasks.GetMessages;
import com.teamzeromtu.studyr.Tasks.SendNewMessage;

public class MessageForm extends AppCompatActivity {
    EditText editText;
    ListView list;
    public String matchName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_form);

        matchName = (String)getIntent().getExtras().get("name");

        TextView nameTitle = (TextView) findViewById(R.id.PersonName);
        nameTitle.setText(matchName);
        editText = (EditText) findViewById(R.id.editText);
        list = (ListView) findViewById(R.id.listView);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendMessage();
                    handled = true;
                }
                hideInput(v);
                return handled;
            }
        });
        StudyrApplication app = (StudyrApplication)getApplication();
        GetMessages msgStart = new GetMessages(app.userId, getIntent().getExtras().get("id").toString(), new MessageFormSetter(this, list, app.userId));
        msgStart.execute();
        resetFocus();
    }
    public void sendMessage() {
        String newMessage = editText.getText().toString();
        editText.setText("");
        StudyrApplication app = (StudyrApplication)getApplication();
        SendNewMessage msgHandler = new SendNewMessage(app.userId, newMessage, list, getIntent().getExtras().get("id").toString());

        msgHandler.execute();
    }


    // Method for when the button in the layout is clicked
    public void back(View view)
    {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        onBackPressed();
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
