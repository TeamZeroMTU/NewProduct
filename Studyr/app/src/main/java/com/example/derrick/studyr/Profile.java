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

import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        Spinner dropdown = (Spinner) findViewById(R.id.spinnerp);
        if (dropdown != null)
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
   }

    public void toMessaging()
    {
        Intent change = new Intent(this, Messaging.class);
        startActivity(change);
    }
}
