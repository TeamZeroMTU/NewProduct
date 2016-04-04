package com.teamzeromtu.studyr.Exceptions;

/**
 * Created by jbdaley on 4/1/16.
 */
public class InvalidUserException extends Exception {
    public InvalidUserException(String exc)
    {
        super(exc);
    }
    public String getMessage()
    {
        return super.getMessage();
    }
}
