package com.upwork.magnus.api;

import com.upwork.magnus.api.core.FlightService;
import com.upwork.magnus.api.core.RequestProcessor;
import com.upwork.magnus.api.core.ReservationService;
import com.upwork.magnus.api.persistence.LocalEntityManagerFactory;
import com.upwork.magnus.model.FlightDetail;
import com.upwork.magnus.model.ReservationRequest;
import com.upwork.magnus.model.ReservationResponse;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by ali on 2016-04-21.
 */
@Path("/api")
public class Api {
    @GET
    @Path("/flights/{from}/{date}/{tickets}")
    @Produces("application/json")
    public Response getTicketsByOrigin(@PathParam("from") String from, @PathParam("date") String date, @PathParam("tickets") String tickets){
        EntityManager em = LocalEntityManagerFactory.createEntityManager();
        FlightService fs = new FlightService(em, from, date, tickets);
        RequestProcessor<FlightService, FlightDetail> rp = new RequestProcessor<>();
        return rp.process(fs);
    }

    @GET
    @Path("/flights/{from}/{to}/{date}/{tickets}")
    public Response getTicketsByOriginAndDestination(@PathParam("from") String from, @PathParam("to") String to, @PathParam("date") String date, @PathParam("tickets") String tickets){
        EntityManager em = LocalEntityManagerFactory.createEntityManager();
        FlightService fs = new FlightService(em, from, to, date, tickets);
        RequestProcessor<FlightService, FlightDetail> rp = new RequestProcessor<>();
        return rp.process(fs);
    }

    @POST
    @Path("/reservation/{flightId}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response makeReservation(@PathParam("flightId") String flightId, ReservationRequest reservationRequest){
        System.out.println(reservationRequest.toString());
        EntityManager em = LocalEntityManagerFactory.createEntityManager();
        ReservationService rs = new ReservationService(em, reservationRequest);
        RequestProcessor<ReservationService, ReservationResponse> rp = new RequestProcessor<>();
        return rp.process(rs);
    }
}
