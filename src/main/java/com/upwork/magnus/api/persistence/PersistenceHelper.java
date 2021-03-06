package com.upwork.magnus.api.persistence;

import com.upwork.magnus.entity.*;
import com.upwork.magnus.model.Passenger;
import com.upwork.magnus.model.ReservationRequest;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * Created by ali on 2016-04-23.
 */
public class PersistenceHelper {
    private EntityManager em = null;

    public PersistenceHelper(EntityManager em) {
        this.em = em;
    }

    public List<AirportEntity> getAirportsByIATACode(String iataCode){
        return em.createQuery("SELECT a FROM AirportEntity a WHERE a.iataCode = :iataCode")
                .setParameter("iataCode", iataCode)
                .getResultList();
    }

    public List<FlightInstanceEntity> getFlightInstance (String fromIataCode, Date date, int tickets){
        return em.createQuery(
                "SELECT f FROM FlightInstanceEntity f " +
                        "JOIN f.originAirport o " +
                        "WHERE o.iataCode = :iataCode AND f.date = :date AND f.availableSeats >= :tickets"
        )
                .setParameter("date", date)
                .setParameter("iataCode", fromIataCode)
                .setParameter("tickets", tickets)
                .getResultList();
    }

    public List<FlightInstanceEntity> getFlightInstance (String fromIataCode, String toIataCode, Date date, int tickets){
        String query = "SELECT f FROM FlightInstanceEntity f " +
                "JOIN f.destinationAirport d " +
                "JOIN f.originAirport a " +
                "WHERE d.iataCode = :toIataCode " +
                "AND a.iataCode = :fromIataCode " +
                "AND f.date = :date " +
                "AND f.availableSeats >= :tickets";
        List resultList = em.createQuery(query)
                .setParameter("toIataCode", toIataCode)
                .setParameter("fromIataCode", fromIataCode)
                .setParameter("date", date)
                .setParameter("tickets", tickets)
                .getResultList();
        return resultList;
    }

    public void persist (Object o){
        em.getTransaction().begin();
        em.persist(o);
        em.flush();
        em.getTransaction().commit();
    }

    public void persistPassenger(Passenger[] passengers) {
        for (Passenger p : passengers){
            em.getTransaction().begin();
            PassengerEntity pe = new PassengerEntity();
            pe.setFirstName(p.getFirstName());
            pe.setLastName(p.getLastName());
            persist(pe);
            em.getTransaction().commit();
        }
    }

    //Ugly implementation. Should be changed to follow parent/child persistence mechanism
    public ReservationEntity persistReservation(ReservationRequest reservationRequest) {
        ReservationEntity re = new ReservationEntity();

        re.setFlightInstance(getFlightInstanceEntity(reservationRequest.getFlightId()));
        re.setTotalPrice(new BigDecimal(re.getFlightInstance().getPrice().intValue() * reservationRequest.getNumberOfSeats()));
        em.getTransaction().begin();
        em.persist(re);
        em.flush();
        em.getTransaction().commit();

        em.getTransaction().begin();
        for (Passenger p : reservationRequest.getPassengers()){
            PassengerEntity pe = new PassengerEntity();
            pe.setFirstName(p.getFirstName());
            pe.setLastName(p.getLastName());
            pe.setReservation(re);
            em.persist(pe);
            em.flush();
        }
        em.getTransaction().commit();

        return re;
    }

    private FlightInstanceEntity getFlightInstanceEntity(int pk){
        return em.find(FlightInstanceEntity.class, pk);
    }

    public FlightEntity getFlightEntity (String flightNumber, int flightTime){
        List result = em.createQuery("SELECT f from FlightEntity f " +
                "WHERE f.flightNumber = :flightNumber " +
                "AND f.flightTime = :flightTime")
                .setParameter("flightNumber", flightNumber)
                .setParameter("flightTime", flightTime)
                .getResultList();
        if (result != null && result.size() >= 1){
            return (FlightEntity)result.get(0);
        }
        return null;
    }

    public AirlineEntity getAirlineEntity (int pk){
        return em.find(AirlineEntity.class, pk);
    }

    public AirlineEntity getAirlineEntity(String defaultAirline) {
        List result = em.createQuery("SELECT a FROM AirlineEntity a " +
                "WHERE a.name = :airlineName")
                .setParameter("airlineName", defaultAirline)
                .getResultList();
        if (result != null && result.size() >= 1){
            return (AirlineEntity)result.get(0);
        }
        return null;
    }
}
