package airport.InterveningEntities;

/**
 * Enum with the possible states of the spectator.
 *
 * @author Xavier Santos
 */

public enum PassengerSituation {
    /**
     * The passenger is in transit.
     */
    IN_TRANSIT("TRT"),

    /**
     * The passenger has arrived his final destination.
     */
    ARRIVED_FINAL_DESTINATION("FDT");

    private final String tag;

    PassengerSituation(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return this.tag;
    }
}
