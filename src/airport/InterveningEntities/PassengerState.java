package airport.InterveningEntities;

/**
 * Enum with the possible states of the spectator.
 * @author Xavier Santos
 */

public enum PassengerState {
    /**
     * Transition state (initial state)
     */
    AT_THE_DISEMBARKING_ZONE,

    /**
     * Blocking state with eventual transition
     * The passenger is waken up by the operations carryItToAppropriateStore and tryToCollectABag
     * of the porter when he places on the conveyor belt a bag she owns, the former, or when he
     * signals that there are no more pieces of luggage in the plane hold, the latter, and makes a tran-
     * sition when either she has in her possession all the bags she owns, or was signaled that there
     * are no more bags in the plane hold
     */
    AT_THE_LUGGAGE_COLLECTION_POINT,

    /**
     * Transition state
     */
    AT_THE_BAGGAGE_RECLAIM_OFFICE,

    /**
     * Blocking state with eventual transition (final state)
     * the passenger is waken up by the operations goHome or prepareNextLeg of the last passen-
     * ger of each flight to exit the arrival terminal or to enter the departure terminal
     */
    EXITING_THE_ARRIVAL_TERMINAL,

    /**
     * Blocking state
     * before blocking, the passenger wakes up the bus driver, if her place in the waiting queue equals
     * the bus capacity, and is waken up by the operation announcingBusBoarding of the driver to
     * mimic her entry in the bus
     */
    AT_THE_ARRIVAL_TRANSFER_TERMINAL,

    /**
     * Blocking state
     * the passenger is waken up by the operation parkTheBusAndLetPassOff of the driver
     */
    TERMINAL_TRANSFER,

    /**
     * Transition state
     */
    AT_THE_DEPARTURE_TRANSFER_TERMINAL,

    /**
     * Blocking state with eventual transition (final state)
     * the passenger is waken up by the operations goHome or prepareNextLeg of the last passen-
     * ger of each flight to exit the arrival terminal or to enter the departure terminal
     */
    ENTERING_THE_DEPARTURE_TERMINAL;
}
