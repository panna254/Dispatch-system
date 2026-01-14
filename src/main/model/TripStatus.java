package model;

/*
*Represents the lifecycle of a trip .
*State transition rules:
*-REQUESTED->IN_PROGRESS or CANCELLED
*-IN_PROGRESS->COMPLETED
*-CANCELLED AND COMPLETED are terminal states
 */

public enum TripStatus {
    REQUESTED,  //Trip created awaiting for driver to start 
    PROGRESS,   //Driver has picked up a passenger ,currently driving
    COMPLETED,  //Trip successfully finished 
    CANCELLED;  //Trip cancelled before completion

    /** 
     * Checks if the status is terminal (no further transition is allowed).
     * Terminal states:COMPLETED ,CANCELLED
     * @return true if status is COMPLETED or CANCELLED,false otherwise
     */
    public boolean isTerminal(){
        return this == COMPLETED || this == CANCELLED;
    }

}