package service;

import model.Driver;
import java.util.ArrayList;
import java.util.List;

public class DriverService {

    /**
     * Returns a list of all drivers currently marked as AVAILABLE.
     */
    public List<Driver> getAvailableDrivers() {
        // STUB: Member 2 will implement this.
        System.out.println("[STUB] DriverService.getAvailableDrivers called");
        return new ArrayList<>(); // Return empty list for now
    }

    /**
     * Updates the status of a specific driver.
     * @param driverId The ID of the driver
     * @param available True if available, False if busy
     */
    public void updateDriverStatus(String driverId, boolean available) {
        // STUB: Member 2 will implement this.
        System.out.println("[STUB] DriverService.updateDriverStatus called for: " + driverId);
    }
}