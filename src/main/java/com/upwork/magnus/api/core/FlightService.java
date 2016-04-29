package com.upwork.magnus.api.core;

import com.upwork.magnus.api.persistence.PersistenceHelper;
import com.upwork.magnus.entity.AirportEntity;
import com.upwork.magnus.entity.FlightInstanceEntity;
import com.upwork.magnus.model.FlightDetail;
import com.upwork.magnus.model.FlightError;
import com.upwork.magnus.model.Flights;

import javax.persistence.EntityManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
    private OffsetDateTime dateTime;
    private int noOfTickets;
    private String toIataCode;
    private boolean isDestinationSet;
    private FlightDetail flightDetail;

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

    public boolean validate() {
        return isValidIataCode(fromIataCode) &&
                isValidDate(date) &&
                isValideTicketNumber(tickets) &&
                isValidIataCode(toIataCode, isDestinationSet);
    }

    public void process() {
        PersistenceHelper ph = new PersistenceHelper(em);
        java.sql.Timestamp ts = Util.offsetDateTimeToSql(dateTime);
        List<FlightInstanceEntity> flightInstance = null;
        if (isDestinationSet){
            flightInstance = ph.getFlightInstance(fromIataCode, toIataCode, ts, noOfTickets);
        } else {
            flightInstance = ph.getFlightInstance(fromIataCode, ts, noOfTickets);
        }
        convert(flightInstance, noOfTickets);
    }

    private void convert(List<FlightInstanceEntity> flightInstanceEntites, int noOfTickets) {
        if (flightInstanceEntites != null && flightInstanceEntites.size() > 0) {
            flightDetail = new FlightDetail();
            Flights[] flights = new Flights[flightInstanceEntites.size()];
            flightDetail.setFlights(flights);
            int count = 0;
            for (FlightInstanceEntity fe : flightInstanceEntites) {
                Flights f = new Flights();
                f.setFlightID(fe.getFlightInstanceId());
                f.setNumberOfSeats(noOfTickets);
                f.setDate(Util.sqlTimetampeToOffsetDateTime(fe.getDate()).toString());
                f.setTotalPrice(fe.getPrice().multiply(new BigDecimal(noOfTickets)).doubleValue());
                f.setTravelTime(fe.getFlight().getFlightTime());
                f.setOrigin(fe.getOriginAirport().getIataCode());
                f.setDestination(fe.getDestinationAirport().getIataCode());
                f.setFlightNumber(fe.getFlight().getFlightNumber());
                flights[count++] = f;

                if (flightDetail.getAirline() == null){
                    flightDetail.setAirline(fe.getFlight().getAirline().getName());
                }
            }
        }
    }

    private boolean isValideTicketNumber(String tickets) {
        if (tickets == null || tickets.trim().isEmpty()) {
            fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(), 3, "Invalid number of tickets");
            return false;
        } else {
            try {
                noOfTickets = Integer.parseInt(tickets);
                if (noOfTickets <= 0) {
                    fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(), 3, "No. of tickets must be greater than 0");
                    return false;
                }
            } catch (Exception e) {
                fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(), 3, "Invalid number of tickets");
                return false;
            }
        }
        return true;
    }

    private boolean isValidDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(), 3, "Empty date");
            return false;
        } else {
            try {
                dateTime = OffsetDateTime.parse(date);
            } catch (Exception e) {
                fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(), 3, "Invalid ISO8601 date");
                return false;
            }
        }
        return true;
    }

    private boolean isValidIataCode(String iataCode, boolean validate) {
        if (validate) {
            return isValidIataCode(iataCode);
        }
        return true;
    }

    private boolean isValidIataCode(String iataCode) {
        if (iataCode == null || iataCode.trim().isEmpty()) {
            fe = new FlightError(Response.Status.BAD_REQUEST.getStatusCode(), 3, "Empty IATACode");
            return false;
        } else {
            PersistenceHelper ph = new PersistenceHelper(em);
            List<AirportEntity> airportsByIATACode = ph.getAirportsByIATACode(iataCode);
            if (airportsByIATACode == null || airportsByIATACode.size() == 0) {
                fe = new FlightError(Response.Status.NOT_FOUND.getStatusCode(), 1, "Couldn't find IATA Code " + iataCode);
                return false;
            }
        }
        return true;
    }

    public Response response() {
        if (fe != null) {
            return Response.status(fe.getHttpError())
                    .entity(fe)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else {
            return Response.status(Response.Status.OK)
                    .entity(flightDetail)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
