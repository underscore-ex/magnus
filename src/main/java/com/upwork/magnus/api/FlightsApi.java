package com.upwork.magnus.api;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by ali on 2016-04-21.
 */
@Path("/api")
public class FlightsApi {
    @GET
    @Path("/flights/{from}/{date}/{tickets}")
    public Response getTicketsByOrigin(@PathParam("from") String from, @PathParam("date") String date, @PathParam("tickets") String tickets){
        String output = from + " - " + date + " - " + tickets;
        return Response.status(Response.Status.OK).entity(output).build();
    }

    @GET
    @Path("/flights/{from}/{to}/{date}/{tickets}")
    public Response getTicketsByOriginAndDestination(@PathParam("from") String from, @PathParam("to") String to, @PathParam("date") String date, @PathParam("tickets") String tickets){
        return Response.status(Response.Status.OK).entity("getTicketsByOriginAndDestination: ").build();
    }

    @POST
    @Path("/reservation/{flightId}")
    @Produces("application/json")
    public Response makeReservation(@PathParam("flightId") String flightId){
        return Response.status(Response.Status.OK).entity("Reservation: " + flightId ).build();
    }
}
