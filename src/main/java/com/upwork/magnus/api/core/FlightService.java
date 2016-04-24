package com.upwork.magnus.api.core;

import com.upwork.magnus.api.persistence.PersistenceHelper;
import com.upwork.magnus.entity.AirportEntity;
import com.upwork.magnus.model.FlightError;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by ali on 2016-04-21.
 */
public class FlightService implements BaseService {
    private final EntityManager em;
    private String fromIataCode = null;

    private FlightError fe = null;
    private String date;
    private String tickets;
    private DateTime dateTime;
    private int noOfTickets;
    private String toIataCode;
    private boolean isDestinationSet;

    public FlightService(EntityManager em, String from, String date, String tickets) {
        this.em = em;
        this.fromIataCode = from;
        this.date = date;
        this.tickets = tickets;
        isDestinationSet = false;
    }

    public FlightService(EntityManager em, String from, String to, String date, String tickets) {
        this.em = em;
        this.fromIataCode = from;
        this.date = date;
        this.tickets = tickets;
        this.toIataCode = to;
        isDestinationSet = true;
    }

    public boolean validate(){
        return isValidIataCode(fromIataCode) &&
                isValidDate(date) &&
                isValideTicketNumber(tickets) &&
                isValidIataCode(toIataCode, isDestinationSet);
    }

    public void process(){

    }

    private boolean isValideTicketNumber(String tickets){
        if (tickets != null || tickets.trim().isEmpty()){
            fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(),3,"Invalid number of tickets");
            return false;
        } else {
            try {
                noOfTickets = Integer.parseInt(tickets);
                if (noOfTickets <= 0){
                    fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(),3,"No. of tickets must be greater than 0");
                    return false;
                }
            } catch (Exception e){
                fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(),3,"Invalid number of tickets");
                return false;
            }
        }
        return true;
    }

    private boolean isValidDate (String date){
        if (date == null || date.trim().isEmpty()){
            fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(),3,"Empty date");
            return false;
        } else {
            try {
                dateTime = DateTime.parse(date);
//                DateTimeZone zone = DateTimeZone.forOffsetMillis(dateTime.getZone().getOffset(dateTime.getMillis()));
            } catch (Exception e){
                fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(), 3, "Invalid ISO8601 date");
                return false;
            }
        }
        return true;
    }
    private boolean isValidIataCode(String iataCode, boolean validate){
        if (validate){
            return isValidIataCode(iataCode);
        }
        return true;
    }

    private boolean isValidIataCode(String iataCode){
        if (iataCode == null || iataCode.trim().isEmpty()){
            fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(),3,"Empty IATACode");
            return false;
        } else {
            PersistenceHelper ph = new PersistenceHelper(em);
            List<AirportEntity> airportsByIATACode = ph.getAirportsByIATACode(iataCode);
            if (airportsByIATACode == null || airportsByIATACode.size() == 0){
                fe = new FlightError(Response.Status.NOT_FOUND.getStatusCode(),1,"Couldn't find IATA Code "+ iataCode);
                return false;
            }
        }
        return true;
    }

    public Response response(){
        if (fe != null){
            return Response.status(fe.getHttpError())
                    .entity(fe)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        return null;
    }
}
