# DSA Project

A Java-based ride-sharing application demonstrating data structures and algorithms concepts.

## Project Structure

```
src/
├── main/
│   ├── app/
│   │   └── Main.java
│   │
│   ├── graph/
│   │   ├── CityGraph.java
│   │   └── Dijkstra.java
│   │
│   ├── model/
│   │   ├── Driver.java
│   │   ├── Rider.java
│   │   ├── RideRequest.java
│   │   ├── Trip.java
│   │   └── TripStatus.java
│   │
│   ├── service/
│   │   ├── DriverService.java
│   │   ├── RideRequestService.java
│   │   ├── DispatchService.java
│   │   ├── TripService.java
│   │   └── ReportService.java
│   │
│   └── util/
│       └── IdGenerator.java
```

## Features

- Graph-based city navigation
- Shortest path calculation using Dijkstra's algorithm
- Driver and rider management
- Ride request and dispatch system
- Trip tracking and reporting

## Getting Started

### Prerequisites

- Java JDK 8 or higher

### Running the Application

```bash
javac src/main/app/Main.java
java -cp src src.main.app.Main
```

## License

This project is for educational purposes.
