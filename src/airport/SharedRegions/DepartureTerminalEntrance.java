package airport.SharedRegions;

import airport.MainProgram.Logger;
import airport.MainProgram.LoggerInterface;

/**
 *TODO
 *
 * @author Xavier Santos
 */

public class DepartureTerminalEntrance {

    /**
     * Passenger enters the departure terminal and wakes the next passenger.
     */
    public void prepareNextLeg(){
        //TODO
    }

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
