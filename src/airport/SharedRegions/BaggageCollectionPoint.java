package airport.SharedRegions;

import airport.MainProgram.LoggerInterface;

/**
 *TODO
 *
 * @author Xavier Santos
 */

public class BaggageCollectionPoint {
    /**
     * Passenger tries to collect one of his bas until he has all of them.
     */
    public void goCollectBag(){
        //TODO
    }

    /**
     * Porter waits for all the passengers to leave the lounge.
     */
    public void takeARest(){
        //TODO
    }

    /**
     * Porter wakes from resting and tries to collect one bag.
     * When the last bag is collected signal the passengers waiting to go to the BaggageReclaimOffice
     */
    public void tryToCollectABag(){
        //TODO
    }

    /**
     * Porter carries one bag to BaggageCollectionPoint or TmpStorageArea.
     * Passenger is waken when he places on the conveyor belt a bag he owns.
     */
    public void carryItToAppropriateStore(){
        //TODO
    }

    /**
     * Porter has no bags to collect and can go back to rest state.
     */
    public void noMoreBagsToCollect(){
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
