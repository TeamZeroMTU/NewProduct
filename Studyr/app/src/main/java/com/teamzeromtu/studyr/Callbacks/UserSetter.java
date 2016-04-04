package com.teamzeromtu.studyr.Callbacks;

import com.teamzeromtu.studyr.Data.User;

/**
 * Created by jbdaley on 3/22/16.
 */
public class UserSetter implements HttpRequestCallback<User> {
    User user;
    public UserSetter(User user) {
        this.user = user;
    }
    @Override
    public void onSuccess(User user) {
        this.user.setSchool(user.getSchool());
        this.user.setUserID(user.getUserID());
        this.user.setCourses(user.getCourses());
        this.user.setName(user.getName());
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(Exception error) {

    }
}
