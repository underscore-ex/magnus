package com.upwork.magnus.api.core;

import com.upwork.magnus.model.FlightError;
import javax.ws.rs.core.Response;

/**
 * Created by ali on 2016-04-21.
 */
public class RequestProcessor<T extends BaseService> {
    public Response process(T service) {
        try {
            if (service.validate()) {
                service.process();
            }
            return service.response();
        } catch (Exception e) {
            e.printStackTrace();
            FlightError fe = new FlightError(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 10, e.getMessage());
            return buildErrorResponse(fe);
        }
    }

    private Response buildErrorResponse(FlightError fe) {
        String json = "{"
                + "\"httpError\": " + fe.getHttpError() + ","
                + "\"errorCode\": " + fe.getErrorCode() + ","
                + "\"message\": \"" + fe.getMessage() + "\""
                + "}";
        return Response.status(fe.getHttpError()).entity(json).build();
    }
}
