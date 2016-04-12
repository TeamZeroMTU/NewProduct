package com.teamzeromtu.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Tasks.GetMatches;
import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;

import java.util.ArrayList;

public class Messaging extends AppCompatActivity {
    ArrayList<User> matchList;

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

       // matchList = new ArrayList<User>();

        Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {

                TextView text = (TextView) view;
                if (text.getText().equals("Home")) {
                    toHome();
                }
                if (text.getText().equals("Profile")) {
                    toProfile();
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });



        StudyrApplication app = (StudyrApplication)getApplication();
        GetMatches userMatches = new GetMatches(app, new MatchGetter());

        NetworkTaskManager manager = app.taskManager;
        manager.execute(userMatches);



       // if (matchList!=null)
         //   matches.setAdapter(new MessagingAdapter(matchList));
    }


    // Methods move to different layouts
    public void toHome()
    {
        Intent change = new Intent(this, Home.class);
        startActivity(change);
        finish();
    }

    public void toProfile()
    {
        Intent change = new Intent(this, ProfileReadWrite.class);
        StudyrApplication app = (StudyrApplication)getApplication();
        change.putExtra(ProfileReadWrite.profileId, app.userId);
        startActivity(change);
        finish();
    }

    public void viewMessage(String name, String userID)
    {
        Intent message = new Intent(this, MessageForm.class);
        message.putExtra("name",name);
        message.putExtra("id",userID);
        startActivity(message);
    }

    // Overrides the phones back button to go back to the home page
    @Override
    public void onBackPressed() {
        Intent back = new Intent(this,Home.class);
        startActivity(back);
        finish();
    }


    private class MessagingAdapter extends BaseAdapter
    {
        ArrayList<User> messages;
        public MessagingAdapter (ArrayList<User> a)
        {
            messages = a;
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public String getItem(int position) {
            return messages.get(position).getName();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();


            View mesView = getLayoutInflater().inflate(R.layout.messege_display,null);
            holder.time = (TextView) mesView.findViewById(R.id.time);
            holder.person = (TextView) mesView.findViewById(R.id.person);
            holder.mesNum = (TextView) mesView.findViewById(R.id.messageNum);
            holder.time.setText("11:27");
            holder.person.setText(messages.get(position).getName());
            holder.mesNum.setText("  37");

            mesView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    viewMessage(messages.get(position).getName(),messages.get(position).getUserID());
                }
            });

            return mesView;
        }
    }

    private class Holder
    {
        TextView person;
        TextView time;
        TextView mesNum;
    }
}

