package com.upwork.magnus.api.persistence;

import com.upwork.magnus.entity.AirportEntity;
import com.upwork.magnus.entity.FlightInstanceEntity;
import com.upwork.magnus.entity.PassengerEntity;
import com.upwork.magnus.entity.ReservationEntity;
import com.upwork.magnus.model.Passenger;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
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

    public List<AirportEntity> getAirportsFlights(String fromIataCode, Timestamp ts){
        String query = "SELECT a FROM AirportEntity a " +
                "JOIN a.flight f " +
                "JOIN f.airline ae WHERE a.iataCode = :iataCode AND f.flightTime = :flightTime";
        return em.createQuery(query)
                .setParameter("iataCode", fromIataCode)
                .setParameter("flightTime", ts)
                .getResultList();
    }

    public List<FlightInstanceEntity> getFlightInstance (String fromIataCode, Timestamp date, int tickets){
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

    public List<FlightInstanceEntity> getFlightInstance (String fromIataCode, String toIataCode, Timestamp date, int tickets){
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
        em.persist(o);
        em.flush();
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
}
