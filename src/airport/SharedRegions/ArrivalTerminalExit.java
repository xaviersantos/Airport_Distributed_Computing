package airport.SharedRegions;

import airport.MainProgram.Logger;
import airport.MainProgram.LoggerInterface;

/**
 *TODO
 *
 * @author Xavier Santos
 */

public class ArrivalTerminalExit {
    /**
     * Passenger leaves.
     * Wake up next passenger EXITING_THE_ARRIVAL_TERMINAL.
     */
    public void goHome(){
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
