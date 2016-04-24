package com.upwork.magnus.api.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.upwork.magnus.api.Util;
import com.upwork.magnus.api.persistence.PersistenceHelper;
import com.upwork.magnus.entity.AirportEntity;
import com.upwork.magnus.model.FlightException;

import javax.persistence.EntityManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by ali on 2016-04-21.
 */
public class FlightService implements BaseService {
    private final EntityManager em;
    private String iataCode = null;

    private FlightException fe = null;

    public FlightService(EntityManager em) {
        this.em = em;
    }

    public boolean validate(){
        if (iataCode == null && iataCode.trim().isEmpty()){
            fe = new FlightException(Response.Status.BAD_REQUEST.getStatusCode(),3,"Empty IATACode");
            return false;
        } else {
            PersistenceHelper ph = new PersistenceHelper(em);
            List<AirportEntity> airportsByIATACode = ph.getAirportsByIATACode(iataCode);
            if (airportsByIATACode == null || airportsByIATACode.size() == 0){
                fe = new FlightException(Response.Status.NOT_FOUND.getStatusCode(),1,"Couldn't find IATA Code "+iataCode);
            }
        }
        return false;
    }

    public void process(){

    }

    public Response response() throws JsonProcessingException {
        if (fe != null){
//            String json = Util.getJsonString(fe);
            return Response.status(fe.getHttpError())
                    .entity(fe)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        return null;
    }

    public void setIataCode(String iataCode){
        this.iataCode = iataCode;
    }

    public String getIataCode(){
        return this.iataCode;
    }
}
