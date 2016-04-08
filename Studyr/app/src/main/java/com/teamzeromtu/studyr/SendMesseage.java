package com.teamzeromtu.studyr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
        onBackPressed();
    }

}
