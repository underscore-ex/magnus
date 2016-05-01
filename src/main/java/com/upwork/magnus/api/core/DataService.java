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
import java.sql.Date;
import java.sql.Time;
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

//        airportsDataPath = "C:/Users/Magnus/Dropbox (Schantz)/Privat/Skole/Git/magnus-master/target/api/WEB-INF/classes/flights.json";
//        flightsDataPath = "C:/Users/Magnus/Dropbox (Schantz)/Privat/Skole/Git/magnus-master/target/api/WEB-INF/classes/airports.json";

        System.out.printf("AirportsDataPath [%s] flightsDataPath [%s]", airportsDataPath, flightsDataPath);
        System.out.println();
        ph = new PersistenceHelper(em);
        util = new Util();
    }

    @Override
    public boolean validate() {
        if (flightsDataPath == null || flightsDataPath.isEmpty()) {
            System.out.printf("flightsData path is invalid");
            System.out.println();
            fe = new FlightError(404, 11, "Flights test data not found");
        }
        if (airportsDataPath == null || airportsDataPath.isEmpty()) {
            System.out.printf("airportsData path is invalid");
            System.out.println();
            fe = new FlightError(404, 11, "Airports test data not found");
        }
        System.out.printf("Validated");
        System.out.println();
        return true;
    }

    @Override
    public void process() {
        try {
            System.out.printf("Loading airline data");
            System.out.println();
            AirlineEntity ae = loadAirline();

            System.out.printf("Loading airports data");
            System.out.println();
            loadAirports();

            System.out.printf("Loading flights");
            System.out.println();
            loadFlights(ae);
        } catch (Exception e) {
            e.printStackTrace();
            fe = new FlightError(10, 500, "Error loading test data");
        }
    }

    private void loadFlights(AirlineEntity ae) throws IOException {
        String jsonData = util.readJson(flightsDataPath);
        System.out.printf("flights data loaded successfully from %s", flightsDataPath);
        System.out.println();

        JSONObject obj = new JSONObject(jsonData);
        JSONArray flights = obj.getJSONArray("flights");
        System.out.printf("%d flights loaded from file. Will be persisting them now", flights.length());
        System.out.println();

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

        System.out.printf("flight_instances will be created for %s", fe.toString());
        System.out.printf("repeatType: %s, repeatInterval: %d, noOfRepeats: %d ", repeatType, repeatInterval, noOfRepeats);
        System.out.println();

        LocalDateTime date = LocalDateTime.parse(flight.getString("date"));
        System.out.printf("First flight will be created for %s", date.toString());
        System.out.println();

        int count = 0;

        for (int i = 0; i < noOfRepeats; i++) {
            try {
                FlightInstanceEntity fi = new FlightInstanceEntity();
                fi.setAvailableSeats(flight.getInt("seats"));
                fi.setDate(Date.valueOf(date.toLocalDate()));
                fi.setTime(Time.valueOf(date.toLocalTime()));
                fi.setPrice(flight.getBigDecimal("price"));
                fi.setOriginAirport(ph.getAirportsByIATACode(flight.getString("origin")).get(0));
                fi.setDestinationAirport(ph.getAirportsByIATACode(flight.getString("destination")).get(0));
                fi.setFlight(fe);
                ph.persist(fi);
                if (repeatType.equalsIgnoreCase("days")) {
                    date = date.plusDays(repeatInterval);
                }
                count++;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("Error saving FlightInstance. Exception message : %s", e.getMessage());
            }
        }
        System.out.printf("%d flight_instances were created", count);
        System.out.println();
    }


    private FlightEntity loadAndSaveFlight(AirlineEntity ae, JSONObject flight) {
        System.out.printf("Persisting flight");
        System.out.println();
        FlightEntity fe = new FlightEntity();
        fe.setFlightNumber(flight.getString("flightNumber"));
        fe.setSeats(flight.getInt("seats"));
        fe.setFlightTime(flight.getInt("flightTime"));
        fe.setAirline(ae);
        FlightEntity existingEntity = ph.getFlightEntity(fe.getFlightNumber(), fe.getFlightTime());
        if (existingEntity == null) {
            System.out.printf("%s doesn't exist. Will be persiting now", fe.toString());
            System.out.println();
            ph.persist(fe);
        } else {
            System.out.printf("%s already existing with id %d ", fe.toString(), existingEntity.getFlightId());
            System.out.println();
            fe.setFlightId(existingEntity.getFlightId());
        }
        return fe;
    }

    private void loadAirports() throws IOException {
        String jsonData = util.readJson(airportsDataPath);
        System.out.printf("Airports data loaded successfully from %s", airportsDataPath);
        System.out.println();
        int count = 0;

        JSONObject obj = new JSONObject(jsonData);
        JSONArray airports = obj.getJSONArray("airports");
        System.out.printf("%d no of airports found in data. Will be persisting them now", airports.length());
        System.out.println();

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
                count++;
            } catch (Exception e) {
                System.out.printf("Error persisting airport");
                System.out.println();
                e.printStackTrace();
                System.out.println("Skipping " + ae.getIataCode() + " because of exception " + e.getMessage());
            }
        }
        System.out.printf("%d airports have been persisted", count);
        System.out.println();
    }

    private AirlineEntity loadAirline() {
        AirlineEntity ae = ph.getAirlineEntity(defaultAirline);
        if (ae == null) {
            System.out.printf("Default airline %s doesn't exist. Creating a new one", defaultAirline);
            System.out.println();
            ae = new AirlineEntity();
            ae.setName(defaultAirline);
            ph.persist(ae);
            System.out.printf("Airline %s persisted", ae.toString());
            System.out.println();
        }
        return ae;
    }

    @Override
    public Response response() {
        if (fe != null) {
            System.out.printf("There seems to be an error. Returning %d response with message %s", fe.getHttpError(), fe.toString());
            System.out.println();
            return Response.status(fe.getHttpError())
                    .entity(fe)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else {
            System.out.printf("Successful response generated with status code");
            System.out.println();
            return Response.status(Response.Status.OK)
                    .entity(new DataResponse("Test data loaded successfully"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

    }

}
