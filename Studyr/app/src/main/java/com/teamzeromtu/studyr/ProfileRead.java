package com.teamzeromtu.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Data.Course;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Tasks.GetUser;
import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;
import com.teamzeromtu.studyr.ViewAdapters.CourseArrayController;

import java.util.ArrayList;

public class ProfileRead extends AppCompatActivity {
    class UserSetter implements HttpRequestCallback<User> {
        @Override
        public void onSuccess(User user) {
            Log.d("ProfileRead", "Success");
            mUser = user;
            final String schoolStr = user.getSchool();
            if (schoolStr != null) {
                schoolField.setText(schoolStr);
            }

            TextView nameView = (TextView) findViewById(R.id.nameText);
            final String name = mUser.getName();
            if(name != null) {
                nameView.setText(name);
            }

            ArrayList<Course> crs = user.getCourses();

            userCoursesAdapter = new CourseArrayController(ProfileRead.this, crs);
            userCourses.setAdapter(userCoursesAdapter.getAdapter());
        }

        @Override
        public void onCancel() {
            Log.d("ProfileRead", "Cancel");
        }

        @Override
        public void onError(Exception error) {
            Log.d("ProfileRead", "Error");
        }
    }
    public static final String profileId = "ProfileRead:Id";

    private User mUser;

    private TextView schoolField;
    private ListView userCourses;

    private CourseArrayController userCoursesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ProfileRead", "onCreate");
        setContentView(R.layout.activity_profile_read);

        mUser = null;

        Intent intent = getIntent();
        String id = intent.getStringExtra(profileId);

        com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
        profilePictureView.setProfileId( id );

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

        schoolField = (TextView) findViewById(R.id.schoolView);

        userCourses = (ListView)findViewById(R.id.courses);

        loadProfile(id);
    }

    public void toHome() {
        Intent change = new Intent(this, Home.class);
        startActivity(change);
        finish();
    }

    public void toMessaging() {
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
        StudyrApplication app = (StudyrApplication)getApplication();
        final GetUser getProfile = new GetUser(id, new UserSetter());

        NetworkTaskManager manager = app.taskManager;
        manager.execute( getProfile );
    }

    public ArrayList<String> courseList(ArrayList<Course> crs) {
        ArrayList<String> courses = new ArrayList<String>();
        for (int i = 0; i < crs.size(); i++)
            courses.add(crs.get(i).getName());
        return courses;
    }
}
