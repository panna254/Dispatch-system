package service;

import graph.CityGraph;
import graph.Dijkstra;
import model.Driver;
import model.RideRequest;
import java.util.List;
import java.util.Map;

public class DispatchService {
    private CityGraph cityGraph;
    private Dijkstra dijkstra;
    private RideRequestService rideRequestService;
    private DriverService driverService;
    private TripService tripService;

    // constructor to save them

    public DispatchService(
            CityGraph cityGraph,
            Dijkstra dijkstra,
            RideRequestService rideRequestService,
            DriverService driverService,
            TripService tripService) {
        this.cityGraph = cityGraph;
        this.dijkstra = dijkstra;
        this.rideRequestService = rideRequestService;
        this.driverService = driverService;
        this.tripService = tripService;
    }

    public void dispatchPendingRequests() {
        // the next request peek
        RideRequest request = rideRequestService.peekNextRequest();
        if (request == null) {
            System.out.println("Dispatch Engine: No pending requests");
            return;
        }

        // check available drivers
        List<Driver> availableDrivers = driverService.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            System.out.println("Dispatch Engine: No drivers available at the moment");
            return;
        }

        // Getting the distances using the Djikstra algorithm
        String pickupLocation = request.getRider().getPickupLocation();
        Map<String, Integer> distances = dijkstra.calculateShortestPaths(cityGraph, pickupLocation);

        // Nearest neighbour search
        Driver bestDriver = null;
        int minDistance = Integer.MAX_VALUE;

        for (Driver driver : availableDrivers) {
            String driverLoc = driver.getCurrentLocation();

            // if node is reachable in the graph
            if (distances.containsKey(driverLoc)) {
                int dist = distances.get(driverLoc);

                // keep track of minimum distance found
                if (dist < minDistance) {
                    minDistance = dist;
                    bestDriver = driver;
                }
            }
        }

        // State transition
        if (bestDriver != null) {
            // match found
            rideRequestService.pollNextRequest(); // remove from the queue
            tripService.createTrip(request, bestDriver); // active trip in queue
            driverService.updateDriverStatus(bestDriver.getId(), false); // driver is busy

            System.out.println("------------------------------------------------");
            System.out.println("MATCH SUCCESSFUL!");
            System.out.println("Request ID: " + request.getId());
            System.out.println("Assigned Driver: " + bestDriver.getId());
            System.out.println("Pickup Distance: " + minDistance + " units");
            System.out.println("------------------------------------------------");
        } else {
            // Case where drivers exist but none have a path to the rider
            System.out.println(
                    "Dispatch Engine: Alert! Drivers available but no path found to Rider at " + pickupLocation);
        }
    }
}
