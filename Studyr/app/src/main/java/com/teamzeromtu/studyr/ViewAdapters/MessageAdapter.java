package com.teamzeromtu.studyr.ViewAdapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teamzeromtu.studyr.Data.Message;
import com.teamzeromtu.studyr.MessageForm;
import com.teamzeromtu.studyr.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private String userID;
    private LayoutInflater inflater;
    protected List<Message> messages = new ArrayList<>();
    protected List<Message> condensedMessages = new ArrayList<>();
    private MessageController controller = new MessageController(this);

    public MessageAdapter(Context context, @LayoutRes int resource, String ID) {
        this.context = context;
        this.userID = ID;
        inflater = LayoutInflater.from(context);
    }

    public MessageController getController() { return controller; }

    @Override
    public void notifyDataSetChanged() {
        Log.i("Adapter", "notifyDataSetChanged()");
        condenseMessages();
        super.notifyDataSetChanged();
    }

    private void condenseMessages() {
        Log.i("Adapter", "condenseMessages");
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message lhs, Message rhs) {
                return lhs.getTime().compareTo(rhs.getTime());
            }
        });
        condensedMessages.clear();
        Message currentMessage = null;

        Iterator<Message> itr = messages.iterator();
        while(itr.hasNext()) {
            Message msg = itr.next();
            if(currentMessage == null) {
                currentMessage = msg;
            } else {
                if(currentMessage.getSender().equals( msg.getSender() )) {
                    currentMessage.setText( currentMessage.getText() + '\n' + msg.getText());
                    currentMessage.setTime(msg.getTime());
                } else {
                    condensedMessages.add(currentMessage);
                    currentMessage = msg;
                }
            }
        }
        if(currentMessage != null) {
            condensedMessages.add(currentMessage);
        }
    }

    @Override
    public int getCount() {
        return condensedMessages.size();
    }

    @Override
    public Message getItem(int position) {
        return condensedMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0; // I'm not sure if we need a row concept.
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

        // Populate the data into the template view using the data object
        Log.d("MessageAdapter", "testing");
        String senderName = "";
        if(userID.equals(message.getSender())) {
            senderName = "You";
        } else {
            String temp = ((MessageForm)context).matchName;
            String arr[] = temp.split(" ", 2);
            senderName = arr[0];
        }
        //time.setText(message.getTime().toString());
        person.setText(senderName);
        //personProxy.setText(senderName);
//        if(message.getText().trim().isEmpty()) {
//            messageNum.setVisibility(View.GONE);
//        }
        messageNum.setText(message.getText());
        //schoolView.setBackgroundColor((position == adapterInterface.selection) ? Color.BLUE : Color.TRANSPARENT);
        // Return the completed view to render on screen
        return convertView;
    }
}
