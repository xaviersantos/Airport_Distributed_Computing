package airport.InterveningEntities;

/**
 * Enum with the possible states of the spectator.
 * @author Xavier Santos
 */

public enum PorterState {
    /**
     * Blocking state (initial / final state)
     * The porter is waken up by the operation whatShouldIDo of the last of the passengers to
     * reach the arrival lounge
     */
    WAITING_FOR_A_PLANE_TO_LAND,

    /**
     * Transition state
     */
    AT_THE_PLANES_HOLD,

    /**
     * Transition state.
     */
    AT_THE_LUGGAGE_BELT_CONVEYOR,

    /**
     * Transition state
     */
    AT_THE_STOREROOM;

}

