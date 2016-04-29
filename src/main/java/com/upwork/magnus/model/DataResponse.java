package com.upwork.magnus.model;

import java.io.Serializable;

/**
 * Created by muhammada on 2016-04-28.
 */
public class DataResponse implements Serializable{
    private String message;

    public DataResponse(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
