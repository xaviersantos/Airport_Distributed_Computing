package airport.InterveningEntities;

/**
 * Enum with the possible states of the spectator.
 *
 * @author Xavier Santos
 */

public enum PorterState {
    /**
     * Blocking state (initial / final state)
     * The porter is waken up by the operation whatShouldIDo of the last of the passengers to
     * reach the arrival lounge
     */
    WAITING_FOR_A_PLANE_TO_LAND("WPL"),

    /**
     * Transition state
     */
    AT_THE_PLANES_HOLD("PHO"),

    /**
     * Transition state.
     */
    AT_THE_LUGGAGE_BELT_CONVEYOR("LBC"),

    /**
     * Transition state
     */
    AT_THE_STOREROOM("STR");

    private final String tag;

    PorterState(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return this.tag;
    }
}

