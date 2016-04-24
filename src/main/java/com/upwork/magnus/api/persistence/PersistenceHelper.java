package com.upwork.magnus.api.persistence;

import com.upwork.magnus.entity.AirlineEntity;
import com.upwork.magnus.entity.AirportEntity;

import javax.persistence.EntityManager;
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

}
