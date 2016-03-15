package com.teamzeromtu.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
                }

                @Override
                public void onNothingSelected(AdapterView parent) {

                }
            });

        // Finds the list view and sets a listener
        ListView matches = (ListView) findViewById(R.id.messages);
        matches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView) view;
                String name = (String) txt.getText();
                viewMessage(name);
            }
        });
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
        Intent change = new Intent(this, Profile.class);
        startActivity(change);
        finish();
    }

    public void viewMessage(String s)
    {
        Intent message = new Intent(this, SendMesseage.class);
        message.putExtra("name",s);
        startActivity(message);
    }

    // Overrides the phones back button to go back to the home page
    @Override
    public void onBackPressed() {
        Intent back = new Intent(this,Home.class);
        startActivity(back);
        finish();
    }
}

