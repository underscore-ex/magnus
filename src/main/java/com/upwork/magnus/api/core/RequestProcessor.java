package com.upwork.magnus.api.core;

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
        } catch (FlightException fe){
            return buildErrorResponse(fe);
        } catch (Exception e){
            FlightException fe = new FlightException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 10, e.getMessage());
            return buildErrorResponse(fe);
        }
    }

    public Response buildErrorResponse(FlightException fe){
        return Response.status(fe.getHttpError()).entity(fe).build();
    }
}
