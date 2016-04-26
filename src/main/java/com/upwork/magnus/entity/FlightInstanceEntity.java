package com.upwork.magnus.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ali on 2016-04-23.
 */
@Entity
@Table(name = "flight_instance", schema = "", catalog = "magnus")
public class FlightInstanceEntity {
    @Id
    private int flightInstanceId;
    private Timestamp date;
    private int availableSeats;
    private BigDecimal price;

    @Column(name = "flightInstanceId", nullable = false, insertable = true, updatable = true)
    public int getFlightInstanceId() {
        return flightInstanceId;
    }

    public void setFlightInstanceId(int flightInstanceId) {
        this.flightInstanceId = flightInstanceId;
    }

    @Basic
    @Column(name = "date", nullable = false, insertable = true, updatable = true)
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Basic
    @Column(name = "availableSeats", nullable = false, insertable = true, updatable = true)
    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Basic
    @Column(name = "price", nullable = false, insertable = true, updatable = true, precision = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="flightId")
    private FlightEntity flight;
    public void setFlight(FlightEntity flight){this.flight = flight;}
    public FlightEntity getFlight(){return this.flight;}

    @OneToMany(fetch=FetchType.LAZY)
    @JoinColumn(name="reservationId")
    private List<ReservationEntity> reservation;
    public void setReservation(List<ReservationEntity> reservation){this.reservation = reservation;}
    public List<ReservationEntity> getReservation(){return this.reservation;}

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="originAirport", referencedColumnName = "airportId")
    public AirportEntity originAirport;
    public void setOriginAirport (AirportEntity originAirport){this.originAirport = originAirport;}
    public AirportEntity getOriginAirport(){return this.originAirport;}

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="destinationAirport", referencedColumnName = "airportId")
    public AirportEntity destinationAirport;
    public void setDestinationAirport(AirportEntity destinationAirport){this.destinationAirport = destinationAirport;}
    public AirportEntity getDestinationAirport(){return this.destinationAirport;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlightInstanceEntity that = (FlightInstanceEntity) o;

        if (flightInstanceId != that.flightInstanceId) return false;
        if (availableSeats != that.availableSeats) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = flightInstanceId;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + availableSeats;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
