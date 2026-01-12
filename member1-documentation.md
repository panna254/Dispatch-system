DOCUMENTATION
________________
The class Edge is a representation of a road/path in the city graph.
An edge connects two locations which we refer to as nodes. Has an attribute weight/ distance. 
packages are like a folder that organises the JAVA classes into logical groups. 
Once created the values cannot change-> immutable
Used by the city graph to store the road connections. 
 
 package model;

 * Represents a weighted, directed edge (road) in the city graph.
 * 
 * KEY PROPERTIES:
 * - Immutable: Once created, values cannot be changed
 * - Validated: Constructor ensures all constraints are met
 * - Directed: Represents a one-way connection from source to destination
 * 
 * CONSTRAINTS:
 * - Source and destination must be non-negative (>= 0)
 * - Source and destination must be different (no self-loops)
 * - Weight must be strictly positive (> 0)
 * 
 * USAGE BY OTHER MODULES:
 * - CityGraph uses Edge objects to store road connections
 * - Dijkstra reads edge weights during shortest path computation
 * - Other members use getters to access edge properties
 * 
 * @author [Your Name] - Member 1
 *
public class Edge {
    // ========== FIELDS ==========
    
    private final int source;       // Starting node ID
    private final int destination;  // Ending node ID
    private final double weight;    // Cost to travel (distance/time)
    
    // ========== CONSTRUCTOR ==========
    
    /**
     * Creates a new weighted edge between two nodes.
     * 
     * @param source      the starting node ID (must be >= 0)
     * @param destination the ending node ID (must be >= 0, must be != source)
     * @param weight      the cost to travel this edge (must be > 0)
     * @throws IllegalArgumentException if any constraint is violated
     *
    public Edge(int source, int destination, double weight) {
        // Validate source (must be non-negative)
        if (source < 0) {
            throw new IllegalArgumentException("Source must be non-negative, got: " + source);
        }
        
        // Validate destination (must be non-negative)
        if (destination < 0) {
            throw new IllegalArgumentException("Destination must be non-negative, got: " + destination);
        }
        
        // Validate source != destination (no self-loops)
        if (source == destination) {
            throw new IllegalArgumentException("Source and destination cannot be the same, got: " + source);
        }
        
        // Validate weight (must be positive)
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be positive, got: " + weight);
        }
        
        // Initialize fields (all validations passed)
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }
    
    // ========== GETTERS ==========
    
    /**
     * Returns the source node ID.
     * 
     * @return the starting node of this edge
     *
    public int getSource() {
        return source;
    }
    
    /**
     * Returns the destination node ID.
     * 
     * @return the ending node of this edge
     *
    public int getDestination() {
        return destination;
    }
    
    /**
     * Returns the weight (cost) of this edge.
     * 
     * @return the weight/cost of traveling this edge
     *
    public double getWeight() {
        return weight;
    }
    
    // ========== UTILITY METHODS ==========
    
    /**
     * Returns a string representation of this edge.
     * Useful for debugging and logging.
     * 
     * @return a string in the format "Edge[source -> destination, weight=w]"
     *
    @Override
    public String toString() {
        return "Edge[" + source + " -> " + destination + ", weight=" + weight + "]";
    }
}
 
 */




DIJKSTRA ALGORITHM
Finds the shortest way to reach a node

How Dijkstra Works
 (Simple Analogy)
Imagine you're at a party trying to find the quickest way to reach someone:

Start with yourself (distance = 0)
Talk to your immediate friends (neighbors)

"If I go through you, how far is it to reach X?"


Always pick the closest unvisited person next
Update distances if you find a shorter path
Repeat until you've reached everyone (or your target)

IMPLEMENTATION
┌─────────────────────────────────────────┐
│ CORE: Clean, efficient implementation  │
│ - Uses int nodes (0, 1, 2, ...)       │
│ - Uses double distances (5.0, 8.5)    │
│ - Focused on algorithm correctness     │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│ ADAPTER: Integration layer              │
│ - Converts String → int                │
│ - Calls core Dijkstra                  │
│ - Converts double → Integer            │
│ - Returns Map<String, Integer>         │
└─────────────────────────────────────────┘


# Member 1: Graph & Shortest Path Module
**Technical Documentation**

## Overview

This module provides the core graph infrastructure and shortest path computation for the Ride-Sharing Dispatch System. It consists of three main components:

1. **Edge.java** - Represents weighted roads between locations
2. **CityGraph.java** - Stores the city map using adjacency list
3. **Dijkstra.java** - Computes shortest paths between locations

---

## Component 1: Edge.java

### Purpose
Represents a directed, weighted edge (road) in the city graph.

### Key Features
- **Immutable**: Once created, values cannot change
- **Validated**: Constructor ensures all constraints are met
- **Type-safe**: Uses primitives for performance

### Data Fields
```java
private final int source;       // Starting node (0-based)
private final int destination;  // Ending node (0-based)
private final double weight;    // Distance/time (always positive)
```

### Constructor Validation
| Rule | Validation | Error Message |
|------|------------|---------------|
| Non-negative source | `source >= 0` | "Source must be non-negative" |
| Non-negative destination | `destination >= 0` | "Destination must be non-negative" |
| No self-loops | `source != destination` | "Source and destination cannot be same" |
| Positive weight | `weight > 0` | "Weight must be positive" |

### Public Methods
```java
public int getSource()           // Returns starting node
public int getDestination()      // Returns ending node
public double getWeight()        // Returns edge weight
public String toString()         // Returns "Edge[0 -> 1, weight=5.0]"
```

### Usage Example
```java
Edge road = new Edge(0, 5, 12.5);
int from = road.getSource();        // 0
int to = road.getDestination();     // 5
double distance = road.getWeight(); // 12.5
```

### Design Decisions
- **Why immutable?** Thread-safe, prevents accidental modification
- **Why int nodes?** Efficient array indexing, type-safe
- **Why double weights?** Supports fractional distances (12.5 km)

---

## Component 2: CityGraph.java

### Purpose
Stores the complete city road network using an adjacency list representation.

### Key Features
- **Adjacency list** for memory efficiency (sparse graphs)
- **Fixed node count** (immutable structure)
- **Defensive copying** prevents external modification

### Data Structure
```java
private final List<List<Edge>> adjList;  // Adjacency list
private final int numNodes;              // Number of locations
```

**Visual Representation:**
```
adjList = [
    [Edge(0→1, 5), Edge(0→3, 8)],  // Node 0's outgoing edges
    [Edge(1→2, 3), Edge(1→4, 2)],  // Node 1's outgoing edges
    [],                             // Node 2 has no outgoing edges
    [Edge(3→4, 7)],                 // Node 3's outgoing edges
    [Edge(4→2, 1)]                  // Node 4's outgoing edges
]
```

### Constructor
```java
public CityGraph(int numNodes)
```
- **Validates**: `1 <= numNodes <= 100`
- **Initializes**: Creates empty edge list for each node
- **Time Complexity**: O(V) where V = number of nodes

### Public Methods

#### addEdge()
```java
public void addEdge(int source, int destination, double weight)
```
- **Purpose**: Adds a directed road to the graph
- **Validation**: Checks node IDs are valid (0 to numNodes-1)
- **Time Complexity**: O(1) amortized
- **Example**:
```java
  graph.addEdge(0, 5, 10.0);  // Road from node 0 to 5, distance 10
```

#### getNeighbors()
```java
public List<Edge> getNeighbors(int node)
```
- **Purpose**: Returns all outgoing edges from a node
- **Returns**: Defensive copy (caller can't modify internal structure)
- **Time Complexity**: O(degree) where degree = number of outgoing edges
- **Example**:
```java
  List<Edge> roads = graph.getNeighbors(0);  // All roads leaving node 0
  for (Edge road : roads) {
      int destination = road.getDestination();
      double distance = road.getWeight();
  }
```

#### getNumNodes()
```java
public int getNumNodes()
```
- **Purpose**: Returns total number of nodes in graph
- **Time Complexity**: O(1)

### Design Decisions

#### Why Adjacency List Over Adjacency Matrix?

| Factor | Adjacency Matrix | Adjacency List | Winner |
|--------|------------------|----------------|--------|
| **Memory** (10 nodes, 30 edges) | 10×10 = 100 cells | ~30 edges | **List** ✅ |
| **Get neighbors** | O(V) - scan all | O(degree) | **List** ✅ |
| **Check edge exists** | O(1) instant | O(degree) | Matrix |
| **Typical use case** | Dense graphs | Sparse graphs | **List** ✅ |

**Verdict**: Adjacency list is optimal for sparse road networks (each location connects to 2-5 roads, not all other locations).

#### Why Defensive Copy in getNeighbors()?

**Without defensive copy:**
```java
List<Edge> neighbors = graph.getNeighbors(0);
neighbors.clear();  // ❌ DESTROYS THE GRAPH!
```

**With defensive copy:**
```java
List<Edge> neighbors = graph.getNeighbors(0);
neighbors.clear();  // ✅ Only affects the copy, graph is safe
```

---

## Component 3: Dijkstra.java

### Purpose
Computes shortest paths from a source node to all other nodes using Dijkstra's algorithm.

### Algorithm Overview

**Dijkstra's Algorithm:**
1. Start with source at distance 0, all others at infinity
2. Always process the closest unvisited node next (via PriorityQueue)
3. For each neighbor, check if going through current node is shorter
4. Update distances and add to queue if shorter path found
5. Repeat until all reachable nodes processed

**Why It Works:**
- **Greedy choice**: Always expand from closest node
- **Optimal substructure**: Shortest path contains shortest sub-paths
- **No negative weights**: Guarantees once a node is visited, its distance is final

### Core Method
```java
public static double[] dijkstra(CityGraph graph, int source)
```

**Parameters:**
- `graph`: The city road network
- `source`: Starting location (0-based node ID)

**Returns:**
- `double[]` where `result[i]` = shortest distance from source to node i
- `Double.MAX_VALUE` indicates unreachable node

**Time Complexity:** O((V + E) log V)
- V = number of nodes
- E = number of edges
- log V from PriorityQueue operations

**Space Complexity:** O(V)
- distances array: O(V)
- visited array: O(V)
- priority queue: O(V)

### Example Usage
```java
CityGraph graph = new CityGraph(5);
graph.addEdge(0, 1, 5.0);
graph.addEdge(0, 3, 8.0);
graph.addEdge(1, 2, 3.0);
graph.addEdge(1, 4, 2.0);
graph.addEdge(3, 4, 7.0);
graph.addEdge(4, 2, 1.0);

double[] distances = Dijkstra.dijkstra(graph, 0);

// Results:
// distances[0] = 0.0   (source to itself)
// distances[1] = 5.0   (via direct edge 0→1)
// distances[2] = 8.0   (via path 0→1→4→2)
// distances[3] = 8.0   (via direct edge 0→3)
// distances[4] = 7.0   (via path 0→1→4)
```

### Adapter Method (For Team Integration)
```java
public static Map<String, Integer> calculateShortestPaths(CityGraph graph, String sourceNode)
```

**Purpose**: Converts between String node IDs (required by Member 4) and efficient int-based implementation.

**Node Mapping:**
- "A" ↔ 0
- "B" ↔ 1
- "C" ↔ 2
- etc.

**Conversions:**
1. String → int: `sourceInt = sourceNode.charAt(0) - 'A'`
2. Call core dijkstra with int
3. int → String: `nodeId = String.valueOf((char)('A' + i))`
4. double → Integer: `(int) Math.round(distance)`

**Example:**
```java
Map<String, Integer> distances = Dijkstra.calculateShortestPaths(graph, "A");
int distToC = distances.get("C");  // Distance from A to C
```

### Helper Class: NodeDistance
```java
private static class NodeDistance implements Comparable<NodeDistance>
```

**Purpose**: Stores (node, distance) pairs for PriorityQueue.

**Why Comparable?**
- PriorityQueue needs to order by distance
- `compareTo()` ensures min-heap behavior (smallest distance first)

Fields:

int node;           // Node ID
double distance;    // Distance from source

# Complexity Analysis

# Time Complexity Summary

| Operation | Edge | CityGraph | Dijkstra |
|-----------|------|-----------|----------|
| Constructor | O(1) | O(V) | N/A |
| addEdge | N/A | O(1) | N/A |
| getNeighbors | N/A | O(degree) | N/A |
| dijkstra | N/A | N/A | O((V+E) log V) |

# Space Complexity Summary

| Component | Space |
|-----------|-------|
| Edge | O(1) per edge |
| CityGraph | O(V + E) total |
| Dijkstra | O(V) auxiliary space |

For project scope (10-20 nodes, ~40 edges):
- Memory: ~2-5 KB
- dijkstra() runs in: ~1 millisecond

---

# Integration with Other Modules

# How Member 2 (Driver Service) Uses This

// Get shortest distance from driver to rider
double[] distances = Dijkstra.dijkstra(graph, driverLocation);
double distanceToRider = distances[riderLocation];

// Compare multiple drivers
for (Driver driver : availableDrivers) {
    double[] dists = Dijkstra.dijkstra(graph, driver.getLocation());
    if (dists[riderLocation] < shortestDist) {
        bestDriver = driver;
        shortestDist = dists[riderLocation];
    }
}
```

# How Member 4 (Dispatch) Uses This

// Using adapter method with String IDs
Map<String, Integer> distances = Dijkstra.calculateShortestPaths(graph, "A");
int distToB = distances.get("B");
```

---

## Testing & Validation

### Test Graph
```
     5        3
  0 ---→ 1 ---→ 2
  |      ↓       ↑
  8      2       1
  ↓      ↓       |
  3 ---→ 4 ------
     7
```

# Expected Results (from node 0)

Node 0: 0.0   (source)
Node 1: 5.0   (direct: 0→1)
Node 2: 8.0   (path: 0→1→4→2)
Node 3: 8.0   (direct: 0→3)
Node 4: 7.0   (path: 0→1→4)
```

# Edge Cases Handled
1. Unreachable nodes: Returns `Double.MAX_VALUE`
2. Negative weights: Rejected by Edge constructor
3. Self-loops: Rejected by Edge constructor
4. Invalid node IDs: Throws `IllegalArgumentException`
5. Empty graph: Works correctly (all nodes unreachable except source)


## Exam Defense: Key Questions & Answers

### Q1: Why adjacency list over matrix?
**A:** Our graph is sparse (10-20 nodes, ~3 edges per node). Matrix wastes 85% of memory storing "no edge" indicators. Adjacency list uses O(V+E) space vs matrix's O(V²), and neighbor lookup is O(degree) vs O(V).

### Q2: What's the time complexity of Dijkstra?
**A:** O((V + E) log V) where V=nodes, E=edges. The log V comes from PriorityQueue operations (add/remove). For our graph (20 nodes, 60 edges), this is ~400 operations.

### Q3: Why use PriorityQueue instead of regular queue?
**A:** Regular queue (BFS) doesn't consider edge weights. PriorityQueue ensures we always process the closest unvisited node next, which guarantees optimal paths. Without it, we'd process nodes in wrong order and get incorrect distances.

### Q4: Why is Edge immutable?
**A:** Three reasons: (1) Thread safety - multiple threads can read simultaneously, (2) Prevents bugs - can't accidentally modify a road's distance, (3) Reflects reality - once a road exists with distance X, that fact doesn't change.

### Q5: What if graph has negative weights?
**A:** Dijkstra fails with negative weights because it assumes once a node is visited, its distance is final. We prevent this by rejecting negative weights in Edge constructor. For negative weights, use Bellman-Ford algorithm instead.

### Q6: How does defensive copying protect the graph?
**A:** `getNeighbors()` returns `new ArrayList<>(adjList.get(node))` instead of the actual list. If caller modifies the returned list (add/remove/clear), they only affect their copy, not our internal structure. Without this, caller could corrupt the graph.

### Q7: Why double for weights instead of int?
**A:** Real distances are often fractional (12.5 km, 7.8 minutes). Using int would lose precision. We convert to Integer only in the adapter method for Member 4's integration.

### Q8: What's the visited array for?
**A:** Prevents reprocessing nodes. PriorityQueue may contain multiple entries for same node (with different distances). We only process each node once - the first time (which has shortest distance due to min-heap property).

---

# Design Patterns Used

1. Encapsulation: Private fields, public methods
2. Immutability: Edge class is immutable
3. Defensive Copying: getNeighbors() returns copy
4. Adapter Pattern: calculateShortestPaths() adapts int-based core to String-based interface
5. Separation of Concerns: Edge, Graph, Algorithm are separate
6. Fail-Fast: Validate inputs immediately in constructors

---


Author: Member 1  



// /**
//  * Simple test method to verify Dijkstra works correctly.
//  */
// public static void main(String[] args) {
//     // Create a small test graph
//     CityGraph graph = new CityGraph(5);
    
//     // Add edges:
//     //     5        3
//     //  0 ---→ 1 ---→ 2
//     //  |      ↓       ↑
//     //  8      2       1
//     //  ↓      ↓       |
//     //  3 ---→ 4 ------
//     //     7
    
//     graph.addEdge(0, 1, 5.0);
//     graph.addEdge(0, 3, 8.0);
//     graph.addEdge(1, 2, 3.0);
//     graph.addEdge(1, 4, 2.0);
//     graph.addEdge(3, 4, 7.0);
//     graph.addEdge(4, 2, 1.0);
    
//     // Run Dijkstra from node 0
//     double[] distances = dijkstra(graph, 0);
    
//     // Print results
//     System.out.println("Shortest distances from node 0:");
//     for (int i = 0; i < distances.length; i++) {
//         if (distances[i] == Double.MAX_VALUE) {
//             System.out.println("Node " + i + ": UNREACHABLE");
//         } else {
//             System.out.println("Node " + i + ": " + distances[i]);
//         }
//     }
// }
