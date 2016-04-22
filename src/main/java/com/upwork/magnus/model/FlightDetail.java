package com.upwork.magnus.model;

/**
 * Created by ali on 2016-04-21.
 */
public class FlightDetail {
    private String airline;
    private Flights[] flights;

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public Flights[] getFlights() {
        return flights;
    }

    public void setFlights(Flights[] flights) {
        this.flights = flights;
    }
}
