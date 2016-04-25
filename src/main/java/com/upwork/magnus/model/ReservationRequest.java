package com.upwork.magnus.model;

import java.util.Arrays;

/**
 * Created by ali on 2016-04-24.
 */
public class ReservationRequest {
    private int flightId;
    private int numberOfSeats;
    private String reserveeName;
    private String reserveePhone;
    private String reserveeEmail;
    private Passenger[] passengers;

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getReserveeName() {
        return reserveeName;
    }

    public void setReserveeName(String reserveeName) {
        this.reserveeName = reserveeName;
    }

    public String getReserveePhone() {
        return reserveePhone;
    }

    public void setReserveePhone(String reserveePhone) {
        this.reserveePhone = reserveePhone;
    }

    public String getReserveeEmail() {
        return reserveeEmail;
    }

    public void setReserveeEmail(String reserveeEmail) {
        this.reserveeEmail = reserveeEmail;
    }

    public Passenger[] getPassengers() {
        return passengers;
    }

    public void setPassengers(Passenger[] passengers) {
        this.passengers = passengers;
    }

    @Override
    public String toString() {
        return "ReservationRequest{" +
                "flightId=" + flightId +
                ", numberOfSeats=" + numberOfSeats +
                ", reserveeName='" + reserveeName + '\'' +
                ", reserveePhone='" + reserveePhone + '\'' +
                ", reserveeEmail='" + reserveeEmail + '\'' +
                ", passengers=" + Arrays.toString(passengers) +
                '}';
    }
}
