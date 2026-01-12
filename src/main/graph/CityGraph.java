package graph;

import model.Edge;
import java.util.List;
import java.util.ArrayList;

public class CityGraph {

    /** AL representation of the graph.
      Index i contains a list of all edges originating from node i. */

    private final List<List<Edge>> adjList;

        /**
      Total number of nodes in the graph.
      Fixed at creation - nodes cannot be added or removed later. final
     */

    private final int numNodes;

    public CityGraph(int numNodes){
        if(numNodes <=0){
            throw new IllegalArgumentException("Number of nodes must be positive, got: " + numNodes);
        }

        if(numNodes >100){
            throw new IllegalArgumentException("Graph should have at most 100 nodes");
        }

        this.numNodes = numNodes;
        this.adjList = new ArrayList<>();

        for (int i = 0; i < numNodes; i++) {
       adjList.add(new ArrayList<>());
   }
    }

    public void addEdge(int source, int destination, double weight){
        //Validation checks
        if (source < 0 || source >= numNodes) {
            throw new IllegalArgumentException("Invalid source: " + source + " (must be 0-" + (numNodes-1) + ")");
        }

        if (destination < 0 || destination >= numNodes) {
            throw new IllegalArgumentException("Invalid destination: " + destination + " (must be 0-" + (numNodes-1) + ")");
        }
        //creating and Edge object

        Edge edge = new Edge(source, destination, weight);

        //adding it to corresponding correct node's list
        adjList.get(source).add(edge);
    }

    public List<Edge> getNeighbors(int node) {
    // Validate node
    if (node < 0 || node >= numNodes) {
        throw new IllegalArgumentException("Invalid node: " + node + " (must be 0-" + (numNodes-1) + ")");
    }
    
    // Return defensive copy
    return new ArrayList<>(adjList.get(node));
}

/* Returns the number of nodes in the graph.

 */
public int getNumNodes() {
    return numNodes;
}
}
