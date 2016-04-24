package com.upwork.magnus.entity;

import javax.persistence.*;

/**
 * Created by ali on 2016-04-23.
 */
@Entity
@Table(name = "airline", schema = "", catalog = "magnus")
public class AirlineEntity {
    @Id
    private int airlineId;
    private String name;

    @Column(name = "airlineId", nullable = false, insertable = true, updatable = true)
    public int getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(int airlineId) {
        this.airlineId = airlineId;
    }

    @Basic
    @Column(name = "name", nullable = false, insertable = true, updatable = true, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AirlineEntity that = (AirlineEntity) o;

        if (airlineId != that.airlineId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = airlineId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
