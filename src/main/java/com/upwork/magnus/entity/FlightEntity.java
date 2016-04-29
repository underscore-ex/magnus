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
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "flightId")
    private int flightId;
    private String flightNumber;
    private int seats;
    private int flightTime;


    @Column(name = "flightId", nullable = false, insertable = true, updatable = true)
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    @Basic
    @Column(name = "flightNumber", nullable = false, insertable = true, updatable = true)
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
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
    public int getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(int flightTime) {
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
        if (seats != that.seats) return false;
        if (flightTime != that.flightTime) return false;
        if (flightNumber != null ? !flightNumber.equals(that.flightNumber) : that.flightNumber != null) return false;
        if (airline != null ? !airline.equals(that.airline) : that.airline != null) return false;
        return !(flightInstanceEntity != null ? !flightInstanceEntity.equals(that.flightInstanceEntity) : that.flightInstanceEntity != null);

    }

    @Override
    public int hashCode() {
        int result = flightId;
        result = 31 * result + (flightNumber != null ? flightNumber.hashCode() : 0);
        result = 31 * result + seats;
        result = 31 * result + flightTime;
        result = 31 * result + (airline != null ? airline.hashCode() : 0);
        result = 31 * result + (flightInstanceEntity != null ? flightInstanceEntity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FlightEntity{" +
                "flightId=" + flightId +
                ", flightNumber='" + flightNumber + '\'' +
                ", seats=" + seats +
                ", flightTime=" + flightTime +
                ", airline=" + airline +
                ", flightInstanceEntity=" + flightInstanceEntity +
                '}';
    }
}
