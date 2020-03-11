package airport.SharedRegions;

import airport.MainProgram.LoggerInterface;

/**
 * Stores the baggage of passengers in transit.
 *
 * @author Xavier Santos
 */

public class TmpStorageArea {
    /**
     * Logger class for debugging.
     */
    private LoggerInterface logger;

    /**
     * Set the current logger
     * @param logger Logger to be used for the entity
     */
    public synchronized void setLogger(LoggerInterface logger){
        this.logger = logger;
    }
}
