package com.upwork.magnus.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by ali on 2016-04-23.
 */
@Entity
@Table(name = "reservation", schema = "", catalog = "magnus")
public class ReservationEntity {
    private int reservationId;
    private BigDecimal totalPrice;

    @Id
    @Column(name = "reservationId", nullable = false, insertable = true, updatable = true)
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    @Basic
    @Column(name = "totalPrice", nullable = false, insertable = true, updatable = true, precision = 2)
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationEntity that = (ReservationEntity) o;

        if (reservationId != that.reservationId) return false;
        if (totalPrice != null ? !totalPrice.equals(that.totalPrice) : that.totalPrice != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = reservationId;
        result = 31 * result + (totalPrice != null ? totalPrice.hashCode() : 0);
        return result;
    }
}
