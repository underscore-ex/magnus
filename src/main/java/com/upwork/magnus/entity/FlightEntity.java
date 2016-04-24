package com.upwork.magnus.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ali on 2016-04-23.
 */
@Entity
@Table(name = "flight", schema = "", catalog = "magnus")
public class FlightEntity {
    @Id
    private int flightId;
    private int flightNumber;
    private int seats;
    private Timestamp flightTime;


    @Column(name = "flightId", nullable = false, insertable = true, updatable = true)
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    @Basic
    @Column(name = "flightNumber", nullable = false, insertable = true, updatable = true)
    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    @Basic
    @Column(name = "seats", nullable = false, insertable = true, updatable = true)
    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Basic
    @Column(name = "flightTime", nullable = false, insertable = true, updatable = true)
    public Timestamp getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(Timestamp flightTime) {
        this.flightTime = flightTime;
    }

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="airlineId")
    private AirlineEntity airline;
    public void setAirline(AirlineEntity airline){this.airline = airline;}
    public AirlineEntity getAirline(){return this.airline;}

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="flightInstanceId")
    private List<FlightInstanceEntity> flightInstanceEntity;
    public void setFlightInstanceEntity(List<FlightInstanceEntity> flightInstanceEntity){this.flightInstanceEntity = flightInstanceEntity;}
    public List<FlightInstanceEntity> getFlightInstanceEntity(){return flightInstanceEntity;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlightEntity that = (FlightEntity) o;

        if (flightId != that.flightId) return false;
        if (flightNumber != that.flightNumber) return false;
        if (seats != that.seats) return false;
        if (flightTime != null ? !flightTime.equals(that.flightTime) : that.flightTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = flightId;
        result = 31 * result + flightNumber;
        result = 31 * result + seats;
        result = 31 * result + (flightTime != null ? flightTime.hashCode() : 0);
        return result;
    }
}
