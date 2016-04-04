package com.teamzeromtu.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.widget.ProfilePictureView;
import com.teamzeromtu.studyr.Data.Course;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Tasks.GetUser;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.R.*;
import static android.R.layout.*;

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

        Intent intent = getIntent();
        String id = intent.getStringExtra("Profile_id");
        loadProfile(id);
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

    private void loadProfile(final String id) {
        class ProfileSetter implements FacebookCallback<User> {
            @Override
            public void onSuccess(User user) {
                TextView nameView = (TextView) findViewById(R.id.schoolView);
                nameView.setText(user.getSchool());

                ArrayList<Course> crs = user.getCourses();
                ListView crsList = (ListView)findViewById(R.id.courses);
                crsList.setAdapter(new ArrayAdapter(Profile.this, layout.test_list_item,courseList(crs)));
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        }
        GetUser getter = new GetUser(id, new ProfileSetter());
        getter.execute();
    }

    public ArrayList<String> courseList(ArrayList<Course> crs)
    {
        ArrayList<String> courses = new ArrayList<String>();
        for(int i = 0; i<crs.size(); i++)
            courses.add(crs.get(i).getName());
        return courses;
    }

}
