package com.teamzeromtu.studyr;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Callbacks.NoOp;
import com.teamzeromtu.studyr.Data.Course;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Tasks.GetSimilar;
import com.teamzeromtu.studyr.Tasks.MatchUser;
import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;

import java.util.ArrayList;

public class Matchmaking extends AppCompatActivity {
    class MatchSetter implements HttpRequestCallback<ArrayList<User>> {
        @Override
        public void onSuccess(ArrayList<User> matches) {
            Log.d("ProfileReadWrite", "Success");

            matchedUsers = matches;
            updateUserIdx( 0 );
        }

        @Override
        public void onCancel() {
            Log.d("ProfileReadWrite", "Cancel");
        }

        @Override
        public void onError(Exception error) {
            Log.d("ProfileReadWrite", "Error");
        }
    }
    public static final String profileUser = "ProfileRead:Id";

    private ArrayList<User> matchedUsers;
    private int userIdx;

    private TextView schoolField;
    private ListView userCourses;

    private Button acceptButton;
    private Button rejectButton;

    private ArrayAdapter<String> userCoursesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ProfileRead", "onCreate");

        setContentView(R.layout.activity_matchmaking_matches);

        Spinner dropdown = (Spinner)findViewById(R.id.spinnerp);
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

        schoolField = (TextView)findViewById(R.id.schoolView);
        userCourses = (ListView)findViewById(R.id.courses);

        userCoursesAdapter = new ArrayAdapter<String>(this, R.layout.item_course, R.id.schoolName);
        userCourses.setAdapter(userCoursesAdapter);

        acceptButton = (Button)findViewById(R.id.acceptMatch);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MatchUser task = new MatchUser((StudyrApplication)getApplication(), matchedUsers.get(userIdx).getUserID(), new NoOp<User>());
                Matchmaking.this.updateUserIdx( Matchmaking.this.userIdx + 1 );
            }
        });
        acceptButton.setVisibility(View.GONE);
        rejectButton = (Button)findViewById(R.id.rejectMatch);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matchmaking.this.updateUserIdx( Matchmaking.this.userIdx + 1 );
            }
        });
        rejectButton.setVisibility(View.GONE);

        loadMatches();
    }

    private void loadMatches() {
        StudyrApplication app = (StudyrApplication)getApplication();
        final AsyncTask getProfile = new GetSimilar(app, new MatchSetter());

        NetworkTaskManager manager = app.taskManager;
        manager.execute( getProfile );
    }

    private void updateUserIdx(int newVal) {
        acceptButton.setVisibility(View.GONE);
        rejectButton.setVisibility(View.GONE);
        userIdx = newVal;

        try {
            final User profile = matchedUsers.get(userIdx);
            final String schoolStr = profile.getSchool();
            if (schoolStr != null) {
                schoolField.setText(schoolStr);
            } else {
                schoolField.setText("");
            }

            final ArrayList<Course> profileCourses = profile.getCourses();
            userCoursesAdapter.clear();
            userCoursesAdapter.addAll(courseList(profileCourses));

            ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
            profilePictureView.setProfileId(profile.getUserID());

            TextView nameView = (TextView) findViewById(R.id.nameText);
            nameView.setText(profile.getName());

            acceptButton.setVisibility(View.VISIBLE);
            rejectButton.setVisibility(View.VISIBLE);
        } catch(IndexOutOfBoundsException e) {
            setContentView(R.layout.activity_matchmaking_no_matches);

            Spinner dropdown = (Spinner)findViewById(R.id.spinnerp);
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
    }

    public void toHome() {
        Log.d("Matchmaking", "toHome()");
        Intent change = new Intent(this, Home.class);
        startActivity(change);
        finish();
    }

    public void toMessaging() {
        Intent change = new Intent(this, Messaging.class);
        startActivity(change);
        finish();
    }

    public ArrayList<String> courseList(ArrayList<Course> crs) {
        ArrayList<String> courses = new ArrayList<String>();
        for (int i = 0; i < crs.size(); i++)
            courses.add(crs.get(i).getName());
        return courses;
    }
}
