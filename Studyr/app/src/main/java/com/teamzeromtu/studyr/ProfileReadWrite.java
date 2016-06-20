package com.teamzeromtu.studyr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.teamzeromtu.studyr.Callbacks.HttpRequestCallback;
import com.teamzeromtu.studyr.Data.Course;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.Tasks.AddCourse;
import com.teamzeromtu.studyr.Tasks.GetCourses;
import com.teamzeromtu.studyr.Tasks.GetUser;
import com.teamzeromtu.studyr.Tasks.NetworkTaskManager;
import com.teamzeromtu.studyr.Tasks.RemoveCourse;
import com.teamzeromtu.studyr.Tasks.UpdateSchool;
import com.teamzeromtu.studyr.ViewAdapters.CourseArrayController;

import java.util.ArrayList;

public class ProfileReadWrite extends StudyrActivity {
    class UserSetter implements HttpRequestCallback<User> {
        @Override
        public void onSuccess(User user) {
            Log.d("ProfileReadWrite", "Success");
            mUser = user;
            final String schoolStr = user.getSchool();
            if (schoolStr != null) {
                schoolField.setText(schoolStr);
                StudyrApplication app = (StudyrApplication)getApplication();
                GetCourses loadAvailableCourses = new GetCourses(app, schoolStr, new AvailableCourseSetter());
                NetworkTaskManager manager = app.taskManager;
                manager.execute( loadAvailableCourses );
            }

            ArrayList<Course> crs = user.getCourses();

            userCoursesAdapter = new CourseArrayController(ProfileReadWrite.this, crs);
            userCourses.setAdapter(userCoursesAdapter.getAdapter());

            schoolField.setVisibility(View.VISIBLE);
            userCourses.setVisibility(View.VISIBLE);
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
    class AvailableCourseSetter implements HttpRequestCallback<ArrayList<Course>> {
        @Override
        public void onSuccess(ArrayList<Course> courses) {
            if(courses != null) {
                availableCoursesAdapter.setData( courses );
            }
            availableCourses.setVisibility(View.VISIBLE);
            addCourse.setVisibility(View.VISIBLE);
            removeCourse.setVisibility(View.VISIBLE);
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
    public static final String profileId = "ProfileReadWrite:Id";

    private User mUser;
    private CourseArrayController availableCoursesAdapter;
    private CourseArrayController userCoursesAdapter;

    private EditText schoolField;
    private Spinner availableCourses;
    private Button addCourse;
    private Button removeCourse;
    private ListView userCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ProfileReadWrite", "onCreate");
        setContentView(R.layout.activity_profile_read_write);

        resetFocus();

        mUser = null;

        com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
        profilePictureView.setProfileId(profile.getId());

        TextView nameView = (TextView) findViewById(R.id.nameText);
        nameView.setText(profile.getName());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        schoolField = (EditText) findViewById(R.id.schoolView);
        schoolField.setVisibility(View.GONE);
        schoolField.setFocusableInTouchMode(true);

        schoolField.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Log.i("ProfileReadWrite", "captured");
                    TextView textView = (TextView) v;
                    mUser.setSchool(textView.getText().toString());
                    StudyrApplication app = (StudyrApplication)getApplication();
                    UpdateSchool updateSchool = new UpdateSchool(app, mUser, new UserSetter());
                    NetworkTaskManager manager = app.taskManager;
                    manager.execute( updateSchool );
                    resetFocus();
                    hideInput(v);
                    return true;
                }
                return false;
            }
        });

        availableCourses = (Spinner)findViewById(R.id.courseSpinner);
        availableCourses.setVisibility(View.GONE);


        userCourses = (ListView)findViewById(R.id.courses);

        availableCoursesAdapter = new CourseArrayController(this, new ArrayList<Course>());
        availableCourses.setAdapter(availableCoursesAdapter.getAdapter());

        userCourses.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userCoursesAdapter.changeSelection(position);
            }
        });

        addCourse = (Button)findViewById(R.id.addCourseButton);
        //addCourse.setVisibility(View.GONE);
        addCourse.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Course selectedCourse = (Course) availableCourses.getSelectedItem();
                    Log.d("Profile", "Adding course: " + selectedCourse.getName());
                    userCoursesAdapter.add(selectedCourse);
                    AddCourse addCourse = new AddCourse( new UserSetter(),
                                                    (StudyrApplication)getApplication(),
                                                    selectedCourse);

                    StudyrApplication app = (StudyrApplication)getApplication();
                    NetworkTaskManager manager = app.taskManager;
                    manager.execute( addCourse );
                } catch (Exception e) {
                    Log.d("Profile", "Add course: Invalid course selected");
                }
            }
        });

        removeCourse = (Button)findViewById(R.id.removeCourseButton);
        //removeCourse.setVisibility(View.GONE);
        removeCourse.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Course userSelectedCourse = (Course) userCourses.getItemAtPosition(userCoursesAdapter.getSelection());
                    Log.d("Profile", "Removing course: " + userSelectedCourse.getName());
                    StudyrApplication app = (StudyrApplication)getApplication();
                    RemoveCourse removeCourse =
                            new RemoveCourse( new UserSetter(), app, userSelectedCourse);
                    userCoursesAdapter.remove(userSelectedCourse);

                    NetworkTaskManager manager = app.taskManager;
                    manager.execute( removeCourse );
                } catch (Exception e) {
                    Log.d("Profile", "Remove course: Invalid course selection");
                }
            }
        });

        Intent intent = getIntent();
        String id = intent.getStringExtra(profileId);
        loadProfile(id);
    }

    private void loadProfile(final String id) {
        StudyrApplication app = (StudyrApplication)getApplication();
        final GetUser getProfile = new GetUser(app, id, new UserSetter());

        NetworkTaskManager manager = app.taskManager;
        manager.execute( getProfile );
    }

    public ArrayList<String> courseList(ArrayList<Course> crs) {
        ArrayList<String> courses = new ArrayList<String>();
        for (int i = 0; i < crs.size(); i++)
            courses.add(crs.get(i).getName());
        return courses;
    }

    private void resetFocus() {
        findViewById(R.id.activity_profile_read_write_layout).requestFocus();
    }

    private void hideInput(View view) {
        hideInput(view.getApplicationWindowToken());
    }
    private void hideInput(IBinder binder) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binder,InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
