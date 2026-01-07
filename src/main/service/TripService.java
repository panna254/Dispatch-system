package service;

import model.RideRequest;
import model.Driver;
import model.Trip;

public class TripService {

    /**
     * Creates a new trip, assigns the driver, and logs it as ACTIVE.
     */
    public Trip createTrip(RideRequest request, Driver driver) {
        // STUB: Member 5 will implement this (Trip lifecycle).
        System.out.println("[STUB] TripService.createTrip called for Rider " + request + " and Driver " + driver);
        return new Trip(); // Return dummy trip
    }
}