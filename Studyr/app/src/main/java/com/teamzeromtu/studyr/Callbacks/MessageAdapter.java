package com.teamzeromtu.studyr.Callbacks;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamzeromtu.studyr.Data.Course;
import com.teamzeromtu.studyr.Data.Message;
import com.teamzeromtu.studyr.MessageForm;
import com.teamzeromtu.studyr.R;
import com.teamzeromtu.studyr.StudyrApplication;
import com.teamzeromtu.studyr.ViewAdapters.CourseArrayController;

import java.util.List;

class MessageAdapter extends ArrayAdapter<Message> {
    Context context;
    String userID;

    public MessageAdapter(Context context, @LayoutRes int resource, @NonNull List<Message> objects, String ID) {
        super(context, resource, objects);
        this.context = context;
        this.userID = ID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("MessageAdapter", "getView()");
        // Get the data item for this position
        Message message = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.messege_display, parent, false);
        }
        // Lookup view for data population
        //TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView person = (TextView) convertView.findViewById(R.id.person);
        TextView messageNum = (TextView) convertView.findViewById(R.id.messageNum);
        // Populate the data into the template view using the data object
        Log.d("MessageAdapter", "testing");
        String senderName = "";
        if(userID.equals(message.getSender())) {
            senderName = "you";
        }
        else {
            senderName = ((MessageForm)context).matchName;
        }
        //time.setText(message.getTime().toString());
        person.setText(senderName);
        messageNum.setText(message.getText());
        //schoolView.setBackgroundColor((position == adapterInterface.selection) ? Color.BLUE : Color.TRANSPARENT);
        // Return the completed view to render on screen
        return convertView;
    }
}
