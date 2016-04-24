package com.upwork.magnus.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upwork.magnus.model.FlightException;

/**
 * Created by ali on 2016-04-23.
 */
public class Util {
    //I should write a JacksonProvider so that this class can be skipped
    public static String getJsonString(FlightException fe) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(fe);
    }
}
