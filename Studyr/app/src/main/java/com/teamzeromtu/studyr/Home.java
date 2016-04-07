package com.teamzeromtu.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Spinner dropdown = (Spinner) findViewById(R.id.spinner);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                // Creates the listener for the spinner to respond to selected items
                @Override
                public void onItemSelected(AdapterView parent, View view, int position, long id) {

                    TextView text = (TextView) view;
                    if (text.getText().equals("Profile"))
                        toProfile();
                    if (text.getText().equals("Messaging"))
                        toMessaging();
                }

                @Override
                public void onNothingSelected(AdapterView parent) {

                }
            });
    }

    // Methods are used to transition to the different layouts
    public void toProfile()
    {
        Intent change = new Intent(this, ProfileReadWrite.class);
        StudyrApplication app = (StudyrApplication)getApplication();
        change.putExtra(ProfileReadWrite.profileId, app.userId);
        startActivity(change);
        finish();
    }

    public void toMessaging()
    {
        Intent change = new Intent(this, Messaging.class);
        StudyrApplication app = (StudyrApplication)getApplication();
        change.putExtra(Messaging.MesId, app.userId);
        startActivity(change);
        finish();
    }

    public void toMatchmaking(View view)
    {
        Intent change = new Intent(this, Matchmaking.class);
        startActivity(change);
        finish();
    }

}
