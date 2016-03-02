package com.example.derrick.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SendMesseage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_messeage);

        String name = (String)getIntent().getExtras().get("name");

        TextView nameTitle = (TextView) findViewById(R.id.PersonName);
        nameTitle.setText(name);

    }

    // Method for when the button in the layout is clicked
    public void back(View view)
    {

        Intent change = new Intent(this, Messaging.class);
        startActivity(change);
        finish();
    }

    // Override the phones back button to just move back to the messaging manager layout
    @Override
    public void onBackPressed() {
        Intent back = new Intent(this,Messaging.class);
        startActivity(back);
        finish();
    }

}
