package com.teamzeromtu.studyr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.teamzeromtu.studyr.Data.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Messaging extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView parent, View view, int position, long id) {

                    TextView text = (TextView) view;
                    if (text.getText().equals("Home")) {
                        toHome();
                    }
                    if (text.getText().equals("Profile")) {
                        toProfile();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView parent) {

                }
            });

        // Finds the list view and sets a listener
        ListView matches = (ListView) findViewById(R.id.messages);
        ArrayList<String> tst = new ArrayList<>();
        tst.add("ThisGuy");
        tst.add("ThisOtherGuy");
        tst.add("TheLastGuy");
        matches.setAdapter(new MessagingAdapter(tst));
    }

    // Methods move to different layouts
    public void toHome()
    {
        Intent change = new Intent(this, Home.class);
        startActivity(change);
        finish();
    }

    public void toProfile()
    {
        Intent change = new Intent(this, ProfileReadWrite.class);
        startActivity(change);
        finish();
    }

    public void viewMessage(String s)
    {
        Intent message = new Intent(this, SendMesseage.class);
        message.putExtra("name",s);
        startActivity(message);
    }

    // Overrides the phones back button to go back to the home page
    @Override
    public void onBackPressed() {
        Intent back = new Intent(this,Home.class);
        startActivity(back);
        finish();
    }


    private class MessagingAdapter extends BaseAdapter
    {
        ArrayList<String> messages;
        public MessagingAdapter (ArrayList a)
        {
            messages = a;
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public String getItem(int position) {
            return messages.get(position);//.getName();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();


            View mesView = getLayoutInflater().inflate(R.layout.messege_display,null);
            holder.time = (TextView) mesView.findViewById(R.id.time);
            holder.person = (TextView) mesView.findViewById(R.id.person);
            holder.mesNum = (TextView) mesView.findViewById(R.id.messageNum);
            holder.time.setText("11:27");
            holder.person.setText(messages.get(position));
            holder.mesNum.setText("  37");

            mesView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    viewMessage(messages.get(position));
                }
            });

            return mesView;
        }
    }

    private class Holder
    {
        TextView person;
        TextView time;
        TextView mesNum;

    }

}

