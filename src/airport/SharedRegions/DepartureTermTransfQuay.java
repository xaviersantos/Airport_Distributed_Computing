package airport.SharedRegions;

import airport.MainProgram.Logger;
import airport.MainProgram.LoggerInterface;

/**
 *TODO
 *
 * @author Xavier Santos
 */

public class DepartureTermTransfQuay {
    /**
     * When the bus is empty, drive the bus to the arrival terminal
     */
    public void goToArrivalTerminal(){
        //TODO
    }

    /**
     * BusDriver drops the passengers at DepartureTerminalEntrance and
     * change the bus state from DRIVING_FORWARD to PARKING_AT_THE_DEPARTURE_TERMINAL
     */
    public void parkTheBusAndLetPassOff(){
        //TODO
    }

    /**
     * Passengers leaves the bus and the last one wakes the BusDriver
     */
    public void leaveTheBus(){
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
