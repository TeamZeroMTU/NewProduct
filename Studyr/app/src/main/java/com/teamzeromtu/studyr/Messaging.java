package com.teamzeromtu.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Data.Message;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Tasks.GetMatches;
import com.teamzeromtu.studyr.Tasks.GetMessages;
import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;

import java.util.ArrayList;

public class Messaging extends StudyrActivity {
    ArrayList<User> matchList;
    ArrayList<Message> messages;
    StudyrApplication app;

    class MatchGetter implements HttpRequestCallback<ArrayList<User>> {
        @Override
        public void onSuccess(ArrayList<User> user) {
            matchList = user;
            ListView matches = (ListView) findViewById(R.id.messages);
            matches.setAdapter(new MessagingAdapter(matchList));
            Log.d("Messaging", "Success");
        }
        @Override
        public void onCancel() {
            Log.d("Messaging", "Cancel");
        }

        @Override
        public void onError(Exception error) {
            Log.d("Messaging", "Error");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // matchList = new ArrayList<User>();

        app = (StudyrApplication)getApplication();
        GetMatches userMatches = new GetMatches(app, new MatchGetter());

        NetworkTaskManager manager = app.taskManager;
        manager.execute(userMatches);



        // if (matchList!=null)
        //   matches.setAdapter(new MessagingAdapter(matchList));
    }


    public void viewMessage(String name, String userID)
    {
        Intent message = new Intent(this, MessageForm.class);
        message.putExtra("name",name);
        message.putExtra("id",userID);
        startActivity(message);
    }

    private class MessagingAdapter extends BaseAdapter
    {
        ArrayList<User> matches;
        class MessageGetter implements HttpRequestCallback<ArrayList<Message>> {
            Holder mesHolder;
            public MessageGetter(Holder h)
            {
                mesHolder = h;
            }
            @Override
            public void onSuccess(ArrayList<Message> user) {
                messages = user;
                if(messages != null) {
                    mesHolder.mesNum.setText(messages.size()+"");
                }
                else {
                    mesHolder.mesNum.setText("0");
                }

                Log.d("GetMessaging", "Success");
            }
            @Override
            public void onCancel() {
                Log.d("GetMessaging", "Cancel");
            }

            @Override
            public void onError(Exception error) {
                Log.d("GetMessaging", "Error");
            }
        }
        public MessagingAdapter (ArrayList<User> a)
        {
            matches = a;
        }

        @Override
        public int getCount() {
            return matches.size();
        }

        @Override
        public String getItem(int position) {
            return matches.get(position).getName();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();


            View mesView = getLayoutInflater().inflate(R.layout.messege_display,null);
            holder.person = (TextView) mesView.findViewById(R.id.person);
            holder.mesNum = (TextView) mesView.findViewById(R.id.messageNum);

            holder.person.setText(matches.get(position).getName());

            GetMessages getMes = new GetMessages(app.userId,matches.get(position).getUserID(), new MessageGetter(holder));
            NetworkTaskManager manager = app.taskManager;
            manager.execute(getMes);


            mesView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    viewMessage(matches.get(position).getName(), matches.get(position).getUserID());
                }
            });

            return mesView;
        }
    }

    private class Holder
    {
        TextView person;
        TextView mesNum;
    }
}

