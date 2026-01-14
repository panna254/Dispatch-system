package service;

import model.RideRequest;

public class RideRequestService {

    /**
     * Peeks at the next request in the queue without removing it.
     * @return The next RideRequest, or null if queue is empty.
     */
    public RideRequest peekNextRequest() {
        // STUB: Member 3 will implement this (Queue/PriorityQueue logic).
        System.out.println("[STUB] RideRequestService.peekNextRequest called");
        return null; 
    }

    /**
     * Removes and returns the next request from the queue.
     * @return The next RideRequest
     */
    public RideRequest pollNextRequest() {
        // STUB: Member 3 will implement this.
        System.out.println("[STUB] RideRequestService.pollNextRequest called");
        return null;
    }
}
// THIS IS MEMBER 1(MUTUA) THAT COMMENTED THIS CODE. IT HAS SOME AMBIGUITY AND I DECIDED TO LEAVE IT THAT WAY FOR ERICK TO SORT IT AFTER MERGING
//package service;

// import model.RideRequest;
// import java.util.LinkedList;
// import java.util.Queue;


// public class RideRequestService {

//     // Queue to store all ride requests
//     private Queue<RideRequest> rideQueue;

//     // Constructor initializes the queue
//     public RideRequestService() {
//         rideQueue = new LinkedList<>();
//     }

//        public void addRequest(RideRequest request) {
//         rideQueue.add(request);
//     }
