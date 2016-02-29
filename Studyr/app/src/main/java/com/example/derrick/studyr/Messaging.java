package com.example.derrick.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

public class Messaging extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

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
                    //  Toast.makeText(MainActivity.this,"quit being a little bitch " + text.getText(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView parent) {
                    // setContentView(R.layout.profile_page);

                }
            });

    }

    public void toHome()
    {
        Intent change = new Intent(this, Home.class);
       // setContentView(R.layout.activity_home);
        startActivity(change);
    }

    public void toProfile()
    {
        Intent change = new Intent(this, Profile.class);
       // setContentView(R.layout.activity_profile);
        startActivity(change);
    }
}

