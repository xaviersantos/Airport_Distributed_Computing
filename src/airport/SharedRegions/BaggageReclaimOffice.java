package airport.SharedRegions;

import airport.MainProgram.Logger;
import airport.MainProgram.LoggerInterface;

/**
 *TODO
 *
 * @author Xavier Santos
 */

public class BaggageReclaimOffice {
    /**
     * Report the missing bags and go home afterwards.
     */
    public void reportMissingBags(){
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
