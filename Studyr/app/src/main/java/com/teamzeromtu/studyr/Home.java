package com.teamzeromtu.studyr;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class Home extends StudyrActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }
}
