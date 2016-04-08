/*import android.content.Context;
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
import com.teamzeromtu.studyr.ViewAdapters.CourseArrayController;

import java.util.List;

class MessageAdapter extends ArrayAdapter<Course> {
    MessageAdapter adapterInterface;
    MessageAdapter(Context context, @LayoutRes int resource, @NonNull List<Course> objects, CourseArrayController adapterInterface) {
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
*/