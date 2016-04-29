package com.upwork.magnus.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by ali on 2016-04-23.
 */
@Entity
@Table(name = "airport", schema = "", catalog = "magnus")
public class AirportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "airportId")
    private int airportId;
    private String iataCode;
    private String timeZone;
    private String name;
    private String country;
    private String city;


    @Column(name = "airportId", nullable = false, insertable = true, updatable = true)
    public int getAirportId() {
        return airportId;
    }

    public void setAirportId(int airportId) {
        this.airportId = airportId;
    }

    @Basic
    @Column(name = "IATACode", nullable = false, insertable = true, updatable = true, length = 45)
    public String getIataCode() {
        return iataCode;
    }

    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    @Basic
    @Column(name = "timeZone", nullable = false, insertable = true, updatable = true, length = 45)
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "country", nullable = false, insertable = true, updatable = true, length = 45)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "city", nullable = false, insertable = true, updatable = true, length = 45)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

//    @OneToMany(fetch=FetchType.LAZY)
//    @JoinColumn(name="flightId")
//    private List<FlightEntity> flight;
//    public void setFlight(List<FlightEntity> flight){this.flight = flight;}
//    public List<FlightEntity> getFlight(){return this.flight;}
//


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AirportEntity that = (AirportEntity) o;

        if (airportId != that.airportId) return false;
        if (iataCode != null ? !iataCode.equals(that.iataCode) : that.iataCode != null) return false;
        if (timeZone != null ? !timeZone.equals(that.timeZone) : that.timeZone != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        return !(city != null ? !city.equals(that.city) : that.city != null);

    }

    @Override
    public int hashCode() {
        int result = airportId;
        result = 31 * result + (iataCode != null ? iataCode.hashCode() : 0);
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }
}
