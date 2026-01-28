package app;

import graph.CityGraph;
import graph.Dijkstra;
import model.*;
import service.*;
import java.util.Scanner;
import java.util.List;

public class Main {
    private static CityGraph graph;
    private static DriverService driverService;
    private static RideRequestService requestService;
    private static TripService tripService;
    private static DispatchService dispatchService;
    private static Scanner scanner;
    private static int riderCounter = 1;
    private static int requestCounter = 1;

    public static void main(String[] args) {
        initializeSystem();
        scanner = new Scanner(System.in);
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    customerMenu();
                    break;
                case 2:
                    driverMenu();
                    break;
                case 3:
                    adminMenu();
                    break;
                case 4:
                    running = false;
                    System.out.println("\nThank you for using Ride Dispatch System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
        
        scanner.close();
    }
    
    private static void initializeSystem() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   RIDE DISPATCH SYSTEM - INITIALIZING  ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Create city graph (nodes: A=0, B=1, C=2, D=3, E=4)
        graph = new CityGraph(5);
        
        // Add edges (creating a connected city network)
        graph.addEdge(0, 1, 10.0);  // A -> B: 10
        graph.addEdge(1, 2, 15.0);  // B -> C: 15
        graph.addEdge(0, 2, 30.0);  // A -> C: 30
        graph.addEdge(2, 3, 5.0);   // C -> D: 5
        graph.addEdge(1, 3, 20.0);  // B -> D: 20
        graph.addEdge(3, 4, 12.0);  // D -> E: 12
        graph.addEdge(0, 4, 50.0);  // A -> E: 50
        
        // Initialize services
        driverService = new DriverService();
        requestService = new RideRequestService();
        tripService = new TripService();
        dispatchService = new DispatchService(
            graph, 
            requestService, 
            driverService, 
            tripService
        );
        
        // Add sample drivers
        driverService.addDriver(new Driver("D001", "John Smith", "A"));
        driverService.addDriver(new Driver("D002", "Jane Doe", "C"));
        driverService.addDriver(new Driver("D003", "Mike Johnson", "E"));
        
        System.out.println("City graph created: Locations A, B, C, D, E");
        System.out.println("3 drivers registered and ready\n");
    }
    
    private static void displayMainMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║           MAIN MENU                    ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("  1. Customer - Request a Ride");
        System.out.println("  2. Driver - Manage Trips");
        System.out.println("  3. Admin - System Management");
        System.out.println("  4. Exit");
        System.out.print("\nEnter your choice: ");
    }
    
    // ==================== CUSTOMER MENU ====================
    private static void customerMenu() {
        boolean inCustomerMenu = true;
        
        while (inCustomerMenu) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║         CUSTOMER MENU                  ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("  1. Request a Ride");
            System.out.println("  2. View Pending Requests");
            System.out.println("  3. Back to Main Menu");
            System.out.print("\nEnter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    requestRide();
                    break;
                case 2:
                    viewPendingRequests();
                    break;
                case 3:
                    inCustomerMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void requestRide() {
        System.out.println("\n--- REQUEST A RIDE ---");
        
        scanner.nextLine(); // Clear buffer
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter pickup location (A, B, C, D, E): ");
        String pickup = scanner.nextLine().toUpperCase();
        
        System.out.print("Enter destination (A, B, C, D, E): ");
        String destination = scanner.nextLine().toUpperCase();
        
        if (!isValidLocation(pickup) || !isValidLocation(destination)) {
            System.out.println("Invalid location. Please use A, B, C, D, or E.");
            return;
        }
        
        if (pickup.equals(destination)) {
            System.out.println("Pickup and destination cannot be the same!");
            return;
        }
        
        String riderId = "R" + String.format("%03d", riderCounter++);
        String requestId = "REQ" + String.format("%03d", requestCounter++);
        
        Rider rider = new Rider(name, riderId, pickup);
        RideRequest request = new RideRequest(requestId, rider, destination);
        requestService.addRequest(request);
        
        System.out.println("\nRide request created!");
        System.out.println("   Request ID: " + requestId);
        System.out.println("   From: " + pickup + " -> To: " + destination);
        System.out.println("\nTip: Go to Admin menu to dispatch rides!");
    }
    
    private static void viewPendingRequests() {
        System.out.println("\n--- PENDING REQUESTS ---");
        System.out.println("Total pending: " + requestService.getPendingRequestCount());
        
        if (requestService.getPendingRequestCount() == 0) {
            System.out.println("No pending requests.");
        }
    }
    
    // ==================== DRIVER MENU ====================
    private static void driverMenu() {
        boolean inDriverMenu = true;
        
        while (inDriverMenu) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║          DRIVER MENU                   ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("  1. View All Drivers");
            System.out.println("  2. Set Driver Available");
            System.out.println("  3. Set Driver Busy");
            System.out.println("  4. View Available Drivers");
            System.out.println("  5. Back to Main Menu");
            System.out.print("\nEnter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    viewAllDrivers();
                    break;
                case 2:
                    setDriverStatus(true);
                    break;
                case 3:
                    setDriverStatus(false);
                    break;
                case 4:
                    viewAvailableDrivers();
                    break;
                case 5:
                    inDriverMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void viewAllDrivers() {
        System.out.println("\n--- ALL DRIVERS ---");
        List<Driver> drivers = driverService.getAllDrivers();
        
        if (drivers.isEmpty()) {
            System.out.println("No drivers registered.");
            return;
        }
        
        System.out.println("┌──────────┬─────────────────┬──────────┬───────────┐");
        System.out.println("│ ID       │ Name            │ Location │ Status    │");
        System.out.println("├──────────┼─────────────────┼──────────┼───────────┤");
        
        for (Driver driver : drivers) {
            String status = driver.isAvailable() ? "Available" : "Busy";
            System.out.printf("│ %-8s │ %-15s │ %-8s │ %-9s │%n", 
                driver.getId(), 
                driver.getId(), 
                driver.getCurrentLocation(), 
                status);
        }
        System.out.println("└──────────┴─────────────────┴──────────┴───────────┘");
    }
    
    private static void setDriverStatus(boolean available) {
        scanner.nextLine(); // Clear buffer
        System.out.print("Enter driver ID (e.g., D001): ");
        String driverId = scanner.nextLine();
        
        driverService.updateDriverStatus(driverId, available);
    }
    
    private static void viewAvailableDrivers() {
        System.out.println("\n--- AVAILABLE DRIVERS ---");
        List<Driver> available = driverService.getAvailableDrivers();
        
        if (available.isEmpty()) {
            System.out.println("No drivers available at the moment.");
            return;
        }
        
        System.out.println("Found " + available.size() + " available driver(s):");
        for (Driver driver : available) {
            System.out.println("  • " + driver.getId() + " at location " + driver.getCurrentLocation());
        }
    }
    
    // ==================== ADMIN MENU ====================
    private static void adminMenu() {
        boolean inAdminMenu = true;
        
        while (inAdminMenu) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║        ADMIN / DISPATCH MENU           ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("  1. Dispatch Pending Requests");
            System.out.println("  2. View System Statistics");
            System.out.println("  3. View City Map");
            System.out.println("  4. Add New Driver");
            System.out.println("  5. Back to Main Menu");
            System.out.print("\nEnter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    dispatchService.dispatchPendingRequests();
                    break;
                case 2:
                    viewStatistics();
                    break;
                case 3:
                    viewCityMap();
                    break;
                case 4:
                    addNewDriver();
                    break;
                case 5:
                    inAdminMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void viewStatistics() {
        System.out.println("\n--- SYSTEM STATISTICS ---");
        System.out.println("Pending Requests: " + requestService.getPendingRequestCount());
        System.out.println("Total Drivers: " + driverService.getAllDrivers().size());
        System.out.println("Available Drivers: " + driverService.getAvailableDrivers().size());
        System.out.println("Total Trips: " + tripService.getTripCount());
    }
    
    private static void viewCityMap() {
        System.out.println("\n--- CITY MAP ---");
        System.out.println("Available Locations: A, B, C, D, E");
        System.out.println("\nConnections:");
        System.out.println("  A ─(10)─ B ─(15)─ C ─(5)─ D ─(12)─ E");
        System.out.println("  │        │        │");
        System.out.println(" (30)    (20)     (50)");
        System.out.println("  │        │        │");
        System.out.println("  └────────┴────────┘");
    }
    
    private static void addNewDriver() {
        scanner.nextLine(); // Clear buffer
        
        System.out.println("\n--- ADD NEW DRIVER ---");
        System.out.print("Enter driver name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter starting location (A, B, C, D, E): ");
        String location = scanner.nextLine().toUpperCase();
        
        if (!isValidLocation(location)) {
            System.out.println("Invalid location.");
            return;
        }
        
        int driverCount = driverService.getAllDrivers().size() + 1;
        String driverId = "D" + String.format("%03d", driverCount);
        
        Driver newDriver = new Driver(driverId, name, location);
        driverService.addDriver(newDriver);
        
        System.out.println("Driver " + driverId + " (" + name + ") added successfully!");
    }
    
    // ==================== HELPER METHODS ====================
    private static boolean isValidLocation(String location) {
        return location.matches("[A-E]");
    }
    
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
