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