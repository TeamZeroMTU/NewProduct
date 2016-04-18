package com.teamzeromtu.studyr.ViewAdapters;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamzeromtu.studyr.Data.Message;
import com.teamzeromtu.studyr.MessageForm;
import com.teamzeromtu.studyr.R;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    Context context;
    String userID;
    LayoutInflater inflater;

    public MessageAdapter(Context context, @LayoutRes int resource, @NonNull List<Message> objects, String ID) {
        super(context, resource, objects);
        this.context = context;
        this.userID = ID;
        inflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("MessageAdapter", "getView()");
        // Get the data item for this position
        Message message = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if(userID.equals(message.getSender())) {
                convertView = inflater.inflate(R.layout.messege_item_display_right, parent, false);
            } else {
            convertView = inflater.inflate(R.layout.messege_item_display_left, parent, false);

            // TextView person = (TextView) convertView.findViewById(R.id.person);
        }

        // Lookup view for data population
        //TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView person = (TextView) convertView.findViewById(R.id.person);
        TextView messageNum = (TextView) convertView.findViewById(R.id.messageNum);
        if(position > 0) {
            Message prevMessage = getItem(position - 1);
            if(prevMessage != null && prevMessage.getSender() != null &&
                    prevMessage.getSender().equals( message.getSender() )) {
                person.setVisibility(View.GONE);
            }
        }

        // Populate the data into the template view using the data object
        Log.d("MessageAdapter", "testing");
        String senderName = "";
        if(userID.equals(message.getSender())) {
            senderName = "You";
        }
        else {
            String temp = ((MessageForm)context).matchName;
            String arr[] = temp.split(" ", 2);
            senderName = arr[0];
        }
        //time.setText(message.getTime().toString());
        person.setText(senderName);
        //personProxy.setText(senderName);
        if(message.getText().trim().isEmpty()) {
            messageNum.setVisibility(View.GONE);
        }
        messageNum.setText(message.getText());
        //schoolView.setBackgroundColor((position == adapterInterface.selection) ? Color.BLUE : Color.TRANSPARENT);
        // Return the completed view to render on screen
        return convertView;
    }
}
