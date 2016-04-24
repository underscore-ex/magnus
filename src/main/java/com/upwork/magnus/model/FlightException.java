package com.upwork.magnus.model;

import java.io.Serializable;

/**
 * Created by ali on 2016-04-21.
 */
public class FlightException implements Serializable{
    private int httpError;
    private int errorCode;
    private String message;

    public FlightException(int httpError, int errorCode, String message) {
        this.httpError = httpError;
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getHttpError() {
        return httpError;
    }

    public String getMessage() {
        return message;
    }

    public int getErrorCode() { return errorCode;}
}
