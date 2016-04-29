package com.upwork.magnus.api.core;

import com.upwork.magnus.api.persistence.PersistenceHelper;
import com.upwork.magnus.entity.AirlineEntity;
import com.upwork.magnus.entity.AirportEntity;
import com.upwork.magnus.entity.FlightEntity;
import com.upwork.magnus.entity.FlightInstanceEntity;
import com.upwork.magnus.model.DataResponse;
import com.upwork.magnus.model.FlightError;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.EntityManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by muhammada on 2016-04-28.
 */
public class DataService implements BaseService {

    private final PersistenceHelper ph;
    private final Util util;
    private String airportsDataPath;
    private String flightsDataPath;
    private FlightError fe;
    private String defaultAirline = "MULANO Airline";

    private String AIRPORTDATA_FILE_NAME = "airports.json";
    private String FLIGHTDATA_FILE_NAME = "flights.json";

    public DataService(EntityManager em, ClassLoader classLoader) {
        airportsDataPath = classLoader.getResource(AIRPORTDATA_FILE_NAME).getPath();
        flightsDataPath = classLoader.getResource(FLIGHTDATA_FILE_NAME).getPath();

        ph = new PersistenceHelper(em);
        util = new Util();
    }

    @Override
    public boolean validate() {
        if (flightsDataPath == null || flightsDataPath.isEmpty()) {
            fe = new FlightError(404, 11, "Flights test data not found");
        }
        if (airportsDataPath == null || airportsDataPath.isEmpty()) {
            fe = new FlightError(404, 11, "Airports test data not found");
        }

        return true;
    }

    @Override
    public void process() {
        try {
            AirlineEntity ae = loadAirline();
            loadAirports();
            loadFlights(ae);
        } catch (Exception e) {
            fe = new FlightError(10, 500, "Error loading test data");
        }
    }

    private void loadFlights(AirlineEntity ae) throws IOException {
        String jsonData = util.readJson(flightsDataPath);

        JSONObject obj = new JSONObject(jsonData);
        JSONArray flights = obj.getJSONArray("flights");

        for (int index = 0; index < flights.length(); index++) {
            JSONObject flight = flights.getJSONObject(index);
            FlightEntity fe = loadAndSaveFlight(ae, flight);
            loadAndSaveFlightInstance(fe, flight);
        }
    }

    private void loadAndSaveFlightInstance(FlightEntity fe, JSONObject flight) {
        String repeatType = flight.getString("repeatType");
        int repeatInterval = flight.getInt("repeatInterval");
        int noOfRepeats = flight.getInt("noOfRepeats");

        LocalDateTime date = LocalDateTime.parse(flight.getString("date"));


        for (int i = 0; i < noOfRepeats; i++) {
            try {
                FlightInstanceEntity fi = new FlightInstanceEntity();
                fi.setAvailableSeats(flight.getInt("seats"));
                fi.setDate(Timestamp.valueOf(date));
                fi.setPrice(flight.getBigDecimal("price"));
                fi.setOriginAirport(ph.getAirportsByIATACode(flight.getString("origin")).get(0));
                fi.setDestinationAirport(ph.getAirportsByIATACode(flight.getString("destination")).get(0));
                fi.setFlight(fe);
                ph.persist(fi);
                if (repeatType.equalsIgnoreCase("days")) {
                    date = date.plusDays(repeatInterval);
                }
            } catch (Exception e) {
                System.err.printf("Error saving FlightInstance. Exception message : %s", e.getMessage());
            }
        }
    }


    private FlightEntity loadAndSaveFlight(AirlineEntity ae, JSONObject flight) {
        FlightEntity fe = new FlightEntity();
        fe.setFlightNumber(flight.getString("flightNumber"));
        fe.setSeats(flight.getInt("seats"));
        fe.setFlightTime(flight.getInt("flightTime"));
        fe.setAirline(ae);
        FlightEntity existingEntity = ph.getFlightEntity(fe.getFlightNumber(), fe.getFlightTime());
        if (existingEntity == null) {
            ph.persist(fe);
        } else {
            fe.setFlightId(existingEntity.getFlightId());
        }
        return fe;
    }

    private void loadAirports() throws IOException {
        String jsonData = util.readJson(airportsDataPath);

        JSONObject obj = new JSONObject(jsonData);
        JSONArray airports = obj.getJSONArray("airports");

        for (int index = 0; index < airports.length(); index++) {
            JSONObject jsonAirport = airports.getJSONObject(index);
            AirportEntity ae = new AirportEntity();
            try {
                ae.setIataCode(jsonAirport.getString("code"));
                ae.setCity(jsonAirport.getString("city"));
                ae.setCountry(jsonAirport.getString("country"));
                ae.setName(jsonAirport.getString("name"));
                ae.setTimeZone(jsonAirport.getString("timezone"));
                ph.persist(ae);
            } catch (Exception e) {
                System.out.println("Skipping " + ae.getIataCode() + " because of exception " + e.getMessage());
            }
        }
    }

    private AirlineEntity loadAirline() {
        AirlineEntity ae = ph.getAirlineEntity(defaultAirline);
        if (ae == null) {
            ae.setName(defaultAirline);
            ph.persist(ae);
        }
        return ae;
    }

    @Override
    public Response response() {
        if (fe != null) {
            return Response.status(fe.getHttpError())
                    .entity(fe)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else {
            return Response.status(Response.Status.OK)
                    .entity(new DataResponse("Test data loaded successfully"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

    }

}
