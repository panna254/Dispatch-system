package model;


public class Edge {
    private final int source;
    private final int destination;
    private final double weight;

public Edge(int source, int destination, double weight) {
    
    if (source <0){
        throw new IllegalArgumentException("Source must be non-negative, got: " + source);

    }

        if (destination <0){
        throw new IllegalArgumentException("Destination must be non-negative, got: " + destination);

    }
        if (source == destination){
        throw new IllegalArgumentException("Source and destination cannot be the same");

    }
        if (weight <=0){
        throw new IllegalArgumentException("Weight must be positive"+weight);

    }
    this.source = source;
    this.destination = destination;
    this.weight = weight;
}

public int getSource(){
    return source;
}

public int getDestination(){
    return destination;
}
public double getWeight(){
    return weight;
}

/*Utility*/
@Override
public String toString() {
    return "Edge[" + source + " -> " + destination + ", weight=" + weight + "]";
}

}






