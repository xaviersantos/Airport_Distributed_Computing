package airport.SharedRegions;

import airport.InterveningEntities.Porter;
import airport.InterveningEntities.PorterState;
import airport.MainProgram.GeneralRepoOfInfo;

import java.util.ArrayList;

/**
 * Stores the baggage of passengers in transit.
 *
 * @author Xavier Santos
 */

public class TmpStorageArea {
    /**
     * Bags currently at the temporary storage area.
     */
    private ArrayList<Integer> bagsInStore = new ArrayList<>();

    /**
     * Get number of bags.
     *
     * @return number of bags
     */
    public int getNumberOfBags() {
        return bagsInStore.size();
    }

    /**
     * Porter carries one bag to TmpStorageArea.
     * Passenger is waken when he places on the conveyor belt a bag he owns.
     */
    public synchronized void carryItToAppropriateStore() {
        Porter porter = ((Porter) Thread.currentThread());
        porter.setPorterState(PorterState.AT_THE_STOREROOM);
        repo.updatePorterState(PorterState.AT_THE_STOREROOM);

        //Porter loses the bag with 30% chance
        boolean lostBag = Math.random() <= 0.3;

        if (lostBag) {
            porter.dropBag();
            repo.updateBaggageLost(1);
        } else
            bagsInStore.add(porter.dropBag());

        repo.updateBaggageInStore(bagsInStore.size());
    }

    /**
     * General repository of information.
     */
    private GeneralRepoOfInfo repo;

    /**
     * Set the current repo
     *
     * @param repo Logger to be used for the entity
     */
    public synchronized void setLogger(GeneralRepoOfInfo repo) {
        this.repo = repo;
    }
}
