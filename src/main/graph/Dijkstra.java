package graph;

import model.Edge;
import java.util.*;

public class Dijkstra {
    
    /**
     * Helper class to store node-distance pairs for the priority queue.
     * Comparable interface allows PriorityQueue to order by distance.
     */
    private static class NodeDistance implements Comparable<NodeDistance> {
        int node;
        double distance;
        
        NodeDistance(int node, double distance) {
            this.node = node;
            this.distance = distance;
        }
        
        @Override
        public int compareTo(NodeDistance other) {
            return Double.compare(this.distance, other.distance);
        }
    }
    
//      Core Dijkstra method 
//      SIGNATURE
//      Computes shortest distances from source node to all other nodes using Dijkstra's algorithm.

public static double[] dijkstra(CityGraph graph, int source) {
    int numNodes = graph.getNumNodes();

    // Checks
    if (source < 0 || source >= numNodes) {
        throw new IllegalArgumentException("Invalid source: " + source);
    }

    //initializing all values

    double[] distances = new double[numNodes];
    Arrays.fill(distances, Double.MAX_VALUE);
    distances[source] = 0.0;


    //PRIORITY queue which returns the closest unvisited node

    PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
    pq.offer(new NodeDistance(source, 0.0));

    //main loop

    // Main loop: process nodes in order of increasing distance
    while (!pq.isEmpty()) {
    // Get the closest unvisited node
        NodeDistance current = pq.poll();
        int currentNode = current.node;
        double currentDist = current.distance;
    
    // Skip if node already visited
        if (visited[currentNode]) {
            continue;
         }
    
    // Mark as visited
    visited[currentNode] = true;
    
    // Explore all neighbors
    List<Edge> neighbors = graph.getNeighbors(currentNode);
    for (Edge edge : neighbors) {
        int neighbor = edge.getDestination();
        double edgeWeight = edge.getWeight();
        
        // Calculate new distance through current node
        double newDist = currentDist + edgeWeight;
        
        // If this path is shorter, update and add to queue
        if (newDist < distances[neighbor]) {
            distances[neighbor] = newDist;
            pq.offer(new NodeDistance(neighbor, newDist));
        }
    }
}

// PART 3: //answer
return distances;
    
}

public static Map<String, Integer> calculateShortestPaths(CityGraph graph, String sourceNode) {
    // Step 1: Convert String node ID to int
    // Assumes: "A" = 0, "B" = 1, "C" = 2, etc.
    if (sourceNode == null || sourceNode.length() != 1) {
        throw new IllegalArgumentException("Node ID must be a single character, got: " + sourceNode);
    }
    
    char nodeChar = sourceNode.toUpperCase().charAt(0);
    int sourceInt = nodeChar - 'A';  // 'A' -> 0, 'B' -> 1, 'C' -> 2
    
    // Validate converted source
    if (sourceInt < 0 || sourceInt >= graph.getNumNodes()) {
        throw new IllegalArgumentException("Invalid node: " + sourceNode + 
            " (must be A-" + (char)('A' + graph.getNumNodes() - 1) + ")");
    }
    
    // Step 2: Call core Dijkstra,java
    double[] distances = dijkstra(graph, sourceInt);
    
    // Step 3: Convert results back to Map<String, Integer>
    Map<String, Integer> result = new HashMap<>();
    for (int i = 0; i < distances.length; i++) {
        String nodeId = String.valueOf((char)('A' + i));  // 0 -> "A", 1 -> "B"
        
        if (distances[i] == Double.MAX_VALUE) {
            // Unreachable nodes: use Integer.MAX_VALUE
            result.put(nodeId, Integer.MAX_VALUE);
        } else {
            // Round to nearest integer
            result.put(nodeId, (int) Math.round(distances[i]));
        }
    }
    
    return result;
}
}


