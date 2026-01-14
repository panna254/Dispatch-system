package service;

import model.RideRequest;
import model.Driver;
import model.Trip;
import model.TripStatus;

import java.time.LocalDateTime;

public class TripService {

    /**
     * Creates a new trip, assigns the driver, and marks it as ACTIVE.
     */
    public Trip createTrip(RideRequest request, Driver driver) {

        // Create a new Trip object
        Trip trip = new Trip();

        // Assign ride request and driver
        trip.setRideRequest(request);
        trip.setDriver(driver);

        // Set trip status to ACTIVE
        trip.setStatus(TripStatus.ACTIVE);

        // Set trip start time
        trip.setStartTime(LocalDateTime.now());

        // Log trip creation
        System.out.println("Trip created successfully.");
        System.out.println("Rider: " + request);
        System.out.println("Driver: " + driver);
        System.out.println("Status: ACTIVE");

        return trip;
    }
}

