package com.upwork.magnus.api;

import com.upwork.magnus.api.core.DataService;
import com.upwork.magnus.api.core.FlightService;
import com.upwork.magnus.api.core.RequestProcessor;
import com.upwork.magnus.api.core.ReservationService;
import com.upwork.magnus.api.persistence.LocalEntityManagerFactory;
import com.upwork.magnus.api.persistence.PersistenceHelper;
import com.upwork.magnus.entity.AirlineEntity;
import com.upwork.magnus.entity.AirportEntity;
import com.upwork.magnus.entity.FlightEntity;
import com.upwork.magnus.entity.FlightInstanceEntity;
import com.upwork.magnus.model.DataResponse;
import com.upwork.magnus.model.FlightDetail;
import com.upwork.magnus.model.ReservationRequest;
import com.upwork.magnus.model.ReservationResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by ali on 2016-04-21.
 */
@Path("/api")
public class Api {
    @GET
    @Path("/flights/{from}/{date}/{tickets}")
    @Produces("application/json")
    public Response getTicketsByOrigin(@PathParam("from") String from, @PathParam("date") String date, @PathParam("tickets") String tickets) {
        System.out.printf("API service %s called with parameters from: %s, date: %s, tickets: %s", "/flights/{from}/{date}/{tickets}", from, date, tickets);
        System.out.println();
        EntityManager em = LocalEntityManagerFactory.createEntityManager();
        FlightService fs = new FlightService(em, from, date, tickets);
        RequestProcessor<FlightService> rp = new RequestProcessor<>();
        return rp.process(fs);
    }

    @GET
    @Path("/flights/{from}/{to}/{date}/{tickets}")
    public Response getTicketsByOriginAndDestination(@PathParam("from") String from, @PathParam("to") String to, @PathParam("date") String date, @PathParam("tickets") String tickets) {
        System.out.printf("API service %s called with parameters from: %s, to: %s, date: %s, tickets: %s", "/flights/{from}/{to}/{date}/{tickets}", from, to, date, tickets);
        System.out.println();
        EntityManager em = LocalEntityManagerFactory.createEntityManager();
        FlightService fs = new FlightService(em, from, to, date, tickets);
        RequestProcessor<FlightService> rp = new RequestProcessor<>();
        return rp.process(fs);
    }

    @POST
    @Path("/reservation/{flightId}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response makeReservation(@PathParam("flightId") String flightId, ReservationRequest reservationRequest) {
        System.out.printf("API service %s called with request body > %s", "/reservation/{flightId}", reservationRequest.toString());
        System.out.println();
        EntityManager em = LocalEntityManagerFactory.createEntityManager();
        ReservationService rs = new ReservationService(em, reservationRequest);
        RequestProcessor<ReservationService> rp = new RequestProcessor<>();
        return rp.process(rs);
    }

    @GET
    @Path("/loaddata")
    public Response loadData() throws IOException {
        System.out.printf("API service %s called", "/loaddata");
        System.out.println();
        EntityManager em = LocalEntityManagerFactory.createEntityManager();
        System.out.println("EntityManager fetched successfully");
        DataService ds = new DataService(em, Api.class.getClassLoader());
        System.out.println("DataService created successfully");
        RequestProcessor<DataService> rp = new RequestProcessor<>();
        return rp.process(ds);
    }
}
