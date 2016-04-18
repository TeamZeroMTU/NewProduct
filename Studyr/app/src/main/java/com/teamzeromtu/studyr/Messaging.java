package com.teamzeromtu.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Data.Message;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Tasks.GetMatches;
import com.teamzeromtu.studyr.Tasks.GetMessages;
import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Messaging extends StudyrActivity {
    ArrayList<User> matchList;
    Map<User, ArrayList<Message>> messageMap = new HashMap<>();
    StudyrApplication app;

    class MatchGetter implements HttpRequestCallback<ArrayList<User>> {
        @Override
        public void onSuccess(ArrayList<User> matchList) {
            Messaging.this.matchList = matchList;

            final MessagingAdapter adapter = new MessagingAdapter();

            ListView matches = (ListView) findViewById(R.id.messages);
            matches.setAdapter( adapter );

            for(final User user: Messaging.this.matchList) {
                GetMessages getMes = new GetMessages(app.userId, user.getUserID(), new HttpRequestCallback<ArrayList<Message>>() {
                    @Override
                    public void onSuccess(ArrayList<Message> messages) {
                        Messaging.this.messageMap.put(user, messages);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                NetworkTaskManager manager = app.taskManager;
                manager.execute(getMes);
            }

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
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        private LayoutInflater inflater;
        public MessagingAdapter() {
            inflater = getLayoutInflater();
        }
        @Override
        public int getCount() {
            return Messaging.this.matchList.size();
        }

        @Override
        public User getItem(int position) {
            return Messaging.this.matchList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.d("Messaging", "getView(" + position + ")");

            View mesView = convertView;
            if(mesView == null) {
                mesView = inflater.inflate(R.layout.messege_overview_display, null);
            }

            final User user = getItem(position);

            ProfilePictureView picture = (ProfilePictureView) mesView.findViewById(R.id.profilePicture);
            picture.setProfileId(user.getUserID());

            ArrayList<Message> messages = Messaging.this.messageMap.get( user );
            if(messages != null) {
                int nMessages = messages.size();


                TextView person = (TextView) mesView.findViewById(R.id.person);
                TextView mesNum = (TextView) mesView.findViewById(R.id.messageNum);

                person.setText(user.getName());
                mesNum.setText(Integer.toString( nMessages ));

                mesView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        viewMessage(user.getName(), user.getUserID());
                    }
                });
            }

            return mesView;
        }
    }
}

