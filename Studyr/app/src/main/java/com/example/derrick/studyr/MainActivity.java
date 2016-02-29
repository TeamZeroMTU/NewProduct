package com.example.derrick.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.*;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setContentView(R.layout.profile_page);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        if(dropdown != null) {
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView parent, View view, int position, long id) {

                    TextView text = (TextView) view;
                    if(text.getText().equals("Profile"))
                        setContentView(R.layout.profile_page);
                    //if(text.getText().equals("Home"))
                    //    setContentView(R.layout.temp_home);
                    if(text.getText().equals("Messaging"))
                        setContentView(R.layout.message_manager);
                  //  Toast.makeText(MainActivity.this,"quit being a little bitch " + text.getText(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView parent) {
                   // setContentView(R.layout.profile_page);

                }
            });
        }


    }

    public void changePage(View view)
    {
        Intent change = new Intent(this, Home.class);
       // setContentView(R.layout.activity_home);
        startActivity(change);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
