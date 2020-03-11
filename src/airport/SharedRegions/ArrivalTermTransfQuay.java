package airport.SharedRegions;

import airport.MainProgram.Logger;
import airport.MainProgram.LoggerInterface;

/**
 *TODO
 *
 * @author Xavier Santos
 */

public class ArrivalTermTransfQuay {
    /**
     * Returns true if the work day is over.
     * @return work day ended
     */
    public boolean hasWorkDayEnded(){
        //TODO
        return false;
    }

    /**
     * Wakes the passengers waiting and the bus driver.
     * If the Passenger's place in the waiting queue equals the bus capacity
     * the Passenger stays
     */
    public void announcingBusBoarding(){
        //TODO
    }

    /**
     * When the bus is filled or there are no more passengers waiting,
     * drive the bus to the departure terminal
     */
    public void goToDepartureTerminal(){
        //TODO
    }

    /**
     * Change the bus state from DRIVING_BACKWARD to PARKING_AT_THE_ARRIVAL_TERMINAL
     */
    public void parkTheBus(){
        //TODO
    }

    /**
     * While there are empty seats, the passengers will board the bus
     */
    public void enterTheBus(){
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
