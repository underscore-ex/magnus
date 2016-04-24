package com.upwork.magnus.api.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.upwork.magnus.model.FlightException;

import javax.ws.rs.core.Response;

/**
 * Created by ali on 2016-04-21.
 */
public class RequestProcessor<T extends BaseService, O> {
    public Response process (T service){
        try {
            if (service.validate()) {
                service.process();
            }
            return service.response();
        } catch (JsonProcessingException jpe){
            FlightException fe = new FlightException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 10, jpe.getMessage());
            return buildErrorResponse(fe);
        }catch (Exception e){
            FlightException fe = new FlightException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 10, e.getMessage());
            return buildErrorResponse(fe);
        }
    }

    private Response buildErrorResponse(FlightException fe){
        String json = "{"
                + "\"httpError\": "+fe.getHttpError()+","
                + "\"errorCode\": "+fe.getErrorCode()+","
                + "\"message\": \""+fe.getMessage()+"\""
        +"}";
        return Response.status(fe.getHttpError()).entity(json).build();
    }
}
