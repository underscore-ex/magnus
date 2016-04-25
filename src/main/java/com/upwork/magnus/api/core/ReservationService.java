package com.upwork.magnus.api.core;

import com.upwork.magnus.model.FlightError;
import com.upwork.magnus.model.ReservationResponse;
import com.upwork.magnus.model.ReservationRequest;

import javax.persistence.EntityManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by ali on 2016-04-21.
 */
public class ReservationService implements BaseService {

    private final ReservationRequest reservationRequest;
    private final EntityManager em;
    private FlightError fe;
    private ReservationResponse reservationResponse;

    public ReservationService(EntityManager em, ReservationRequest reservationRequest){
        this.em = em;
        this.reservationRequest = reservationRequest;
    }
    public boolean validate(){
        return true;
    }

    public void process(){

    }

    public Response response() {
        if (fe != null){
            return Response.status(fe.getHttpError())
                    .entity(fe)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else {
            return Response.status(Response.Status.OK)
                    .entity(reservationResponse)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

    }
}
