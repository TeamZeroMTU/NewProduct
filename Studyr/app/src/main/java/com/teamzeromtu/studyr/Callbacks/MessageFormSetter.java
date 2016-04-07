package com.teamzeromtu.studyr.Callbacks;

import android.widget.ListView;

import com.teamzeromtu.studyr.Data.Message;

import java.util.ArrayList;

/**
 * Created by Manda on 4/5/2016.
 */
public class MessageFormSetter implements HttpRequestCallback<ArrayList<Message>> {
    ListView list;
    String userId;
    public MessageFormSetter(ListView theList, String uid) {
        list = theList;
        userId = uid;
    }

    @Override
    public void onSuccess(ArrayList<Message> result) {
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(Exception e) {

    }
}
