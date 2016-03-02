package com.example.derrick.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Matchmaking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchmaking);

    }

    // Overrides the phones back button correctly move back to the home page
    @Override
    public void onBackPressed() {
        Intent back = new Intent(this,Home.class);
        startActivity(back);
        finish();
    }

}
