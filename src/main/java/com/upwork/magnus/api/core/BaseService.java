package com.upwork.magnus.api.core;

import com.upwork.magnus.model.FlightException;

import javax.ws.rs.core.Response;

/**
 * Created by ali on 2016-04-21.
 */
public interface BaseService {
    boolean validate() throws FlightException;
    void process() throws FlightException;
    Response response();
}
