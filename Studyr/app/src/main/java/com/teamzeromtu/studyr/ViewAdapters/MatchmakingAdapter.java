package com.teamzeromtu.studyr.ViewAdapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.teamzeromtu.studyr.Data.Course;
import com.teamzeromtu.studyr.Data.User;
import com.teamzeromtu.studyr.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by jbdaley on 4/13/16.
 */
public class MatchmakingAdapter extends BaseAdapter
{
    Activity activity;
    List<User> matches;
    Set<String> names;
    LayoutInflater inflater;
    public MatchmakingAdapter(Activity activity)
    {
        this.activity = activity;
        this.matches = new LinkedList<>();
        this.names = new TreeSet<>();
        inflater = activity.getLayoutInflater();
    }

    public synchronized void addMatches(Collection<User> users) {
        boolean changed = false;
        for(User u: users) {
            String name = u.getName();
            if(!names.contains(name)) {
                names.add(name);
                matches.add( u );
                changed = true;
            }
         }
        if(changed) {
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return matches.size();
    }

    @Override
    public User getItem(int position) {
        return matches.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0; // Do we need a row concept?
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("Fling", "getView(" + position + ") matches size: " + matches.size());
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.content_matchmaking_match_item, parent, false);
        }

        TextView schoolField = (TextView) convertView.findViewById(R.id.schoolView);

        final User profile = matches.get(position);
        final String schoolStr = profile.getSchool();
        if (schoolStr != null) {
            schoolField.setText(schoolStr);
        } else {
            schoolField.setText("");
        }

        ListView courses = (ListView) convertView.findViewById(R.id.courses);
        ArrayAdapter<String> userCoursesAdapter = new ArrayAdapter<String>(activity, R.layout.item_course, R.id.schoolName);
        courses.setAdapter(userCoursesAdapter);

        final ArrayList<Course> profileCourses = profile.getCourses();
        userCoursesAdapter.addAll(courseList(profileCourses));

        ProfilePictureView profilePictureView = (ProfilePictureView) convertView.findViewById(R.id.profilePicture);
        profilePictureView.setProfileId(profile.getUserID());

        TextView nameView = (TextView) convertView.findViewById(R.id.nameText);
        nameView.setText(profile.getName());
        return convertView;
    }

    public synchronized void pop() {
        matches.remove(0);
        notifyDataSetChanged();
    }

    private ArrayList<String> courseList(ArrayList<Course> crs) {
        ArrayList<String> courses = new ArrayList<String>();
        for (int i = 0; i < crs.size(); i++)
            courses.add(crs.get(i).getName());
        return courses;
    }
}
