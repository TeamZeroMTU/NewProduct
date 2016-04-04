package com.teamzeromtu.studyr.Callbacks;

/**
 * Created by jbdaley on 4/1/16.
 */
public interface HttpRequestCallback<RESULT> {
    public void onSuccess(RESULT result);

    public void onCancel();

    public void onError(Exception e);
}
