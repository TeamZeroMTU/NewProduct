package com.teamzeromtu.studyr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import com.teamzeromtu.studyr.Callbacks.MessageFormSetter;
import com.teamzeromtu.studyr.Tasks.GetMessages;
import com.teamzeromtu.studyr.Tasks.SendNewMessage;

public class MessageForm extends AppCompatActivity {
    EditText editText;
    ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_form);

        String name = (String)getIntent().getExtras().get("name");

        TextView nameTitle = (TextView) findViewById(R.id.PersonName);
        nameTitle.setText(name);
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
                return handled;
            }
        });
        StudyrApplication app = (StudyrApplication)getApplication();
        GetMessages msgStart = new GetMessages(app.userId, new MessageFormSetter(list, app.userId));
        msgStart.execute();
    }
    public void sendMessage() {
        String newMessage = editText.getText().toString();
        editText.setText("");
        StudyrApplication app = (StudyrApplication)getApplication();
        SendNewMessage msgHandler = new SendNewMessage(app.userId, newMessage, list);

        msgHandler.execute();
    }


    // Method for when the button in the layout is clicked
    public void back(View view)
    {
        onBackPressed();
    }

}
