package com.teamzeromtu.studyr.Callbacks;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.teamzeromtu.studyr.Data.Message;
import com.teamzeromtu.studyr.R;

import java.util.ArrayList;

/**
 * Created by Manda on 4/5/2016.
 */
public class MessageFormSetter implements HttpRequestCallback<ArrayList<Message>> {
    ListView list;
    String userId;
    Context context;
    public MessageFormSetter(Context context, ListView theList, String uid) {
        list = theList;
        userId = uid;
        this.context = context;
    }

    @Override
    public void onSuccess(ArrayList<Message> result) {
        //MessageAdapter view = new MessageAdapter();
        MessageAdapter messages = new MessageAdapter(context, R.layout.messege_display, result, userId);
        list.setAdapter(messages);
        list.setStackFromBottom(true);


    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(Exception e) {

    }
}
