package com.upwork.magnus.api.core;

import com.upwork.magnus.model.FlightException;

import javax.ws.rs.core.Response;

/**
 * Created by ali on 2016-04-21.
 */
public class ReservationService implements BaseService {
    public boolean validate() throws FlightException {
        return false;
    }

    public void process() throws FlightException {

    }

    public Response response() {
        return null;
    }
}
