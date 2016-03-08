package com.example.derrick.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();

        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
        profilePictureView.setProfileId( profile.getId() );

        TextView nameView = (TextView) findViewById(R.id.nameText);
        nameView.setText( profile.getName() );

        Spinner dropdown = (Spinner) findViewById(R.id.spinnerp);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                TextView text = (TextView) view;
                if (text.getText().equals("Home")) {
                    toHome();
                }
                if (text.getText().equals("Messaging")) {
                    toMessaging();
                }
            }

            @Override
            public void onNothingSelected(AdapterView parent) {

            }
        });

    }

    public void toHome()
    {
        Intent change = new Intent(this, Home.class);
        startActivity(change);
        finish();
    }

    public void toMessaging()
    {
        Intent change = new Intent(this, Messaging.class);
        startActivity(change);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(this, Home.class);
        startActivity(back);
        finish();
    }
}