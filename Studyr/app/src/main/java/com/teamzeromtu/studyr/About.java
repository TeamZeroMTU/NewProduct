package com.teamzeromtu.studyr;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Created by jbdaley on 4/12/16.
 */
public class About extends StudyrActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        TextView icons = (TextView)findViewById(R.id.attributationIcons);
        icons.setText("\"Studyr Icons\" by");

        TextView by = (TextView)findViewById(R.id.attributionLink);
        by.setClickable(true);
        by.setMovementMethod(LinkMovementMethod.getInstance());
        by.setText(
                Html.fromHtml("<a href=\"http://romannurik.github.io/AndroidAssetStudio/icons-launcher.html\">Android Asset Studio</a> "));

        TextView licensed = (TextView)findViewById(R.id.textView3);
        licensed.setText("is licensed under");

        TextView licenseLink = (TextView)findViewById(R.id.textView4);
        licenseLink.setClickable(true);
        licenseLink.setMovementMethod(LinkMovementMethod.getInstance());
        licenseLink.setText(
                Html.fromHtml("<a href=\"http://creativecommons.org/licenses/by/3.0/\">CC BY 3.0</a> "));
    }
}
