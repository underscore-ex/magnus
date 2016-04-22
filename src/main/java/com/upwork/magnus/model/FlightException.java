package com.upwork.magnus.model;

/**
 * Created by ali on 2016-04-21.
 */
public class FlightException extends Exception {
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
    @Override
    public String getMessage() {
        return message;
    }
}
