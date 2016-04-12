package com.teamzeromtu.studyr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by jbdaley on 4/12/16.
 */
public class StudyrActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logged_in_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.profileEditAction:
            {
                Intent change = new Intent(this, ProfileReadWrite.class);
                StudyrApplication app = (StudyrApplication)getApplication();
                change.putExtra(ProfileReadWrite.profileId, app.userId);
                startActivity(change);
                finish();
                return true;
            }
            case R.id.messagingAction:
            {
                Intent change = new Intent(this, Messaging.class);
                startActivity(change);
                finish();
                return true;
            }
            case R.id.matchmakingAction:
            {
                Intent change = new Intent(this, Matchmaking.class);
                startActivity(change);
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
