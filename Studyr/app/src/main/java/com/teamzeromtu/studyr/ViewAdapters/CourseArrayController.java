package com.teamzeromtu.studyr.ViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.teamzeromtu.studyr.Data.Course;
import com.teamzeromtu.studyr.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jbdaley on 4/4/16.
 */
public class CourseArrayController {
    private int selection = -1;
    private ArrayAdapter<Course> adapter;
    public ArrayAdapter<Course> getAdapter() {
        return adapter;
    }

    /**changeSelection() changed the selected item, or unselects if the same item is selected.
     * @param position  New selection position
     */
    public void changeSelection(int position) {
        Log.d("CourseAdapter", "Set selection: " + position);
        if(selection != position) {
            selection = position;
        } else {
            selection = -1;
        }
        adapter.notifyDataSetChanged();
    }

    public int getSelection() {
        return selection;
    }

    public void add(Course course) {
        adapter.add(course);
    }

    public void setData(Collection<? extends Course> courses) {
        adapter.clear();
        adapter.addAll( courses );
    }

    public void remove(Course course) {
        adapter.remove(course);
        final int listSize = adapter.getCount();
        selection = (selection < listSize) ? selection : listSize - 1;
    };
    public CourseArrayController(Context context, ArrayList<Course> courses) {
        this(context, courses, -1);
    }
    public CourseArrayController(Context context, @NonNull ArrayList<Course> courses, int selection) {
        this.selection = selection;
        class CourseArrayAdapterImpl extends ArrayAdapter<Course> {
            CourseArrayController adapterInterface;
            CourseArrayAdapterImpl(Context context, @LayoutRes int resource, @NonNull List<Course> objects, CourseArrayController adapterInterface) {
                super(context, resource, objects);
                this.adapterInterface = adapterInterface;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                Log.d("CourseArray", "getDropDownView()");
                // Get the data item for this position
                Course course = getItem(position);
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_course, parent, false);
                }
                // Lookup view for data population
                TextView schoolView = (TextView) convertView.findViewById(R.id.schoolName);
                // Populate the data into the template view using the data object
                String courseName = course.getName();
                if(courseName != null) {
                    Log.d("CourseArray", courseName);
                    schoolView.setText(courseName);
                }
                // Return the completed view to render on screen
                return convertView;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.d("CourseArray", "getView()");
                // Get the data item for this position
                Course course = getItem(position);
                // Check if an existing view is being reused, otherwise inflate the view
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_course, parent, false);
                }
                // Lookup view for data population
                TextView schoolView = (TextView) convertView.findViewById(R.id.schoolName);
                // Populate the data into the template view using the data object
                String courseName = course.getName();
                if(courseName != null) {
                    Log.d("CourseArray", courseName);
                    schoolView.setText(courseName);
                }
                schoolView.setBackgroundColor((position == adapterInterface.selection) ? Color.BLUE : Color.TRANSPARENT);
                // Return the completed view to render on screen
                return convertView;
            }
        }
        adapter = new CourseArrayAdapterImpl(context, R.layout.item_course, courses, this);
    }
}
