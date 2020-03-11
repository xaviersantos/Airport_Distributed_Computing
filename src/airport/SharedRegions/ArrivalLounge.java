package airport.SharedRegions;

import airport.MainProgram.LoggerInterface;
import airport.MainProgram.SimulationParameters;
import genclass.GenericIO;

/**
 * TODO
 *
 * @author Xavier Santos
 */

public class ArrivalLounge {
    /**
     * Number of passengers in the lounge.
     */
    private int passengersOnLounge = 0;

    /**
     * Passenger decides what to do next and leaves the lounge.
     * The last passenger to leave wakes the Porter.
     */
    public synchronized void whatShouldIDo(){
        while (passengersOnLounge < SimulationParameters.N_PASSENGERS){
            try{
                wait();
            }
            catch(InterruptedException e){
                GenericIO.writelnString("whatShouldIDo - Thread of passenger was interrupted.");
                System.exit(1);
            }
        }
    }

    /**
     * Passenger takes a bus.
     */
    public void takeABus(){
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
