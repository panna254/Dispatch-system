package network;

import graph.CityGraph;
import graph.Dijkstra;
import model.*;
import service.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 8080;
    private static CityGraph graph;
    private static DriverService driverService;
    private static RideRequestService requestService;
    private static TripService tripService;
    private static DispatchService dispatchService;
    private static int requestCounter = 1;
    private static int riderCounter = 1;
    
    public static void main(String[] args) {
        initializeSystem();
        startServer();
    }
    
    private static void initializeSystem() {
        System.out.println("========================================");
        System.out.println("  RIDE DISPATCH SERVER - INITIALIZING  ");
        System.out.println("========================================\n");
        
        graph = new CityGraph(5);
        graph.addEdge(0, 1, 10.0);
        graph.addEdge(1, 2, 15.0);
        graph.addEdge(0, 2, 30.0);
        graph.addEdge(2, 3, 5.0);
        graph.addEdge(1, 3, 20.0);
        graph.addEdge(3, 4, 12.0);
        graph.addEdge(0, 4, 50.0);
        
        driverService = new DriverService();
        requestService = new RideRequestService();
        tripService = new TripService();
        dispatchService = new DispatchService(
            graph, requestService, driverService, tripService
        );
        
        driverService.addDriver(new Driver("D001", "John Smith", "A"));
        driverService.addDriver(new Driver("D002", "Jane Doe", "C"));
        driverService.addDriver(new Driver("D003", "Mike Johnson", "E"));
        
        System.out.println("City graph created: Locations A, B, C, D, E");
        System.out.println("3 drivers registered and ready\n");
    }
    
    private static void startServer() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            System.out.println("Waiting for clients to connect...\n");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }
    
    static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);
                    String response = handleRequest(message);
                    out.println(response);
                }
            } catch (IOException e) {
                System.out.println("Client disconnected");
            } finally {
                cleanup();
            }
        }
        
        private synchronized String handleRequest(String message) {
            String[] parts = Protocol.parseMessage(message);
            String command = Protocol.getCommand(parts);
            
            try {
                switch (command) {
                    case Protocol.REQUEST_RIDE:
                        return handleRequestRide(parts);
                    case Protocol.VIEW_PENDING_REQUESTS:
                        return Protocol.success("Pending: " + requestService.getPendingRequestCount());
                    case Protocol.VIEW_ALL_DRIVERS:
                        return handleViewDrivers();
                    case Protocol.SET_DRIVER_STATUS:
                        return handleSetDriverStatus(parts);
                    case Protocol.VIEW_AVAILABLE_DRIVERS:
                        return handleViewAvailable();
                    case Protocol.DISPATCH_REQUESTS:
                        return handleDispatch();
                    case Protocol.VIEW_STATISTICS:
                        return handleViewStats();
                    case Protocol.ADD_DRIVER:
                        return handleAddDriver(parts);
                    default:
                        return Protocol.error("Unknown command");
                }
            } catch (Exception e) {
                return Protocol.error(e.getMessage());
            }
        }
        
        private String handleRequestRide(String[] parts) {
            String name = parts[1];
            String pickup = parts[2];
            String destination = parts[3];
            
            String riderId = "R" + String.format("%03d", riderCounter++);
            String requestId = "REQ" + String.format("%03d", requestCounter++);
            
            Rider rider = new Rider(name, riderId, pickup);
            RideRequest request = new RideRequest(requestId, rider, destination);
            requestService.addRequest(request);
            
            return Protocol.success("Ride created! ID: " + requestId + " | " + pickup + " -> " + destination);
        }
        
        private String handleViewDrivers() {
            List<Driver> drivers = driverService.getAllDrivers();
            StringBuilder sb = new StringBuilder("ALL DRIVERS:\\n");
            for (Driver d : drivers) {
                sb.append(d.getId()).append(" - ")
                  .append(d.getName()).append(" - ")
                  .append(d.getCurrentLocation()).append(" - ")
                  .append(d.isAvailable() ? "Available" : "Busy").append("\\n");
            }
            return Protocol.success(sb.toString());
        }
        
        private String handleSetDriverStatus(String[] parts) {
            String driverId = parts[1];
            boolean available = Boolean.parseBoolean(parts[2]);
            driverService.updateDriverStatus(driverId, available);
            return Protocol.success("Driver " + driverId + " -> " + (available ? "Available" : "Busy"));
        }
        
        private String handleViewAvailable() {
            List<Driver> available = driverService.getAvailableDrivers();
            if (available.isEmpty()) {
                return Protocol.success("No available drivers");
            }
            StringBuilder sb = new StringBuilder("AVAILABLE:\\n");
            for (Driver d : available) {
                sb.append(d.getId()).append(" at ").append(d.getCurrentLocation()).append("\\n");
            }
            return Protocol.success(sb.toString());
        }
        
        private String handleDispatch() {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream old = System.out;
            System.setOut(ps);
            dispatchService.dispatchPendingRequests();
            System.out.flush();
            System.setOut(old);
            String output = baos.toString().replace("\n", "\\n");
            return Protocol.success("DISPATCH:\\n" + output);
        }
        
        private String handleViewStats() {
            return Protocol.success(String.format(
                "STATS:\\nPending: %d\\nDrivers: %d\\nAvailable: %d\\nTrips: %d",
                requestService.getPendingRequestCount(),
                driverService.getAllDrivers().size(),
                driverService.getAvailableDrivers().size(),
                tripService.getTripCount()
            ));
        }
        
        private String handleAddDriver(String[] parts) {
            String name = parts[1];
            String location = parts[2];
            int count = driverService.getAllDrivers().size() + 1;
            String driverId = "D" + String.format("%03d", count);
            driverService.addDriver(new Driver(driverId, name, location));
            return Protocol.success("Driver " + driverId + " added!");
        }
        
        private void cleanup() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.println("Cleanup error: " + e.getMessage());
            }
        }
    }
}
