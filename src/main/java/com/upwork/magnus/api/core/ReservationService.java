package com.upwork.magnus.api.core;

import com.upwork.magnus.api.persistence.PersistenceHelper;
import com.upwork.magnus.entity.AirportEntity;
import com.upwork.magnus.entity.FlightInstanceEntity;
import com.upwork.magnus.entity.ReservationEntity;
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
    private ReservationEntity re;

    public ReservationService(EntityManager em, ReservationRequest reservationRequest){
        this.em = em;
        this.reservationRequest = reservationRequest;
    }
    public boolean validate(){
        return true;
    }

    public void process(){
        PersistenceHelper ph = new PersistenceHelper(em);
        re = ph.persistReservation(reservationRequest);
    }

    public Response response() {
        if (fe != null){
            return Response.status(fe.getHttpError())
                    .entity(fe)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else {
            return Response.status(Response.Status.OK)
                    .entity(convertDomainToModel())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

    }

    //A generic conversion framework should be created if this project
    //goes beyond 2 services.
    private ReservationResponse convertDomainToModel() {
        ReservationResponse response = new ReservationResponse();
        response.setFlightID(reservationRequest.getFlightId());

        FlightInstanceEntity flightInstance = re.getFlightInstance();
        AirportEntity originAirport = flightInstance.getOriginAirport();
        response.setOrigin(originAirport.getName() + " (" +originAirport.getIataCode()+")");

        AirportEntity destinationAirport = flightInstance.getDestinationAirport();
        response.setDestination(destinationAirport.getName() + " (" +destinationAirport.getIataCode()+")");

        response.setDate(Util.sqlTimestampToOffsetDateTime(flightInstance.getDate(), flightInstance.getTime(), originAirport.getTimeZone()).toString());
        response.setFlightTime(flightInstance.getFlight().getFlightTime());
        response.setNumberOfSeats(reservationRequest.getNumberOfSeats());
        response.setReserveeName(reservationRequest.getReserveeName());
        response.setPassengers(reservationRequest.getPassengers());

        return response;
    }
}
