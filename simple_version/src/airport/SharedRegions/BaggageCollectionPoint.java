package airport.SharedRegions;

import airport.InterveningEntities.*;
import airport.MainProgram.GeneralRepoOfInfo;
import genclass.GenericIO;

import java.util.ArrayList;

/**
 * Area where the porter deposits the baggage belonging to the passengers who have finalized their trip,
 * who collect it and go home or report any lost baggage at the baggage reclaim office.
 *
 * @author Xavier Santos
 */

public class BaggageCollectionPoint {

    /**
     * Bags currently at the baggage collection point.
     */
    private ArrayList<Integer> bagsInBelt = new ArrayList<>();

    /**
     * Get number of bags.
     *
     * @return number of bags
     */
    public int getNumberOfBags() {
        return bagsInBelt.size();
    }

    /**
     * Passenger tries to collect one of his bags until he has all of them or finds one missing.
     */
    public synchronized void goCollectBag() {
        Passenger passenger = ((Passenger) Thread.currentThread());
        passenger.setPassengerState(PassengerState.AT_THE_LUGGAGE_COLLECTION_POINT);
        repo.updatePassengerState(passenger.getPassengerId(),
                passenger.getPassengerState(), passenger.getPassengerSituation());

        while (repo.getBaggageInPlane() != 0 && passenger.getTotalBags() != passenger.getCurrentBags()) {
            try {
                wait();
            } catch (InterruptedException e) {
                GenericIO.writelnString("goCollectBag - Thread of passenger was interrupted.");
                System.exit(1);
            }

            for (int i = 0; i < bagsInBelt.size(); i++) {
                if (passenger.getPassengerId() == bagsInBelt.get(i)) {
                    passenger.collectBag();
                    bagsInBelt.remove(i);
                    repo.updateBaggageInBelt(bagsInBelt.size());
                    repo.updatePassengerBags(passenger.getPassengerId(), passenger.getCurrentBags());
                }
            }
        }
        notifyAll();
    }

    /**
     * Porter carries one bag to BaggageCollectionPoint.
     */
    public synchronized void carryItToAppropriateStore() {
        Porter porter = ((Porter) Thread.currentThread());
        porter.setPorterState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);

        //Porter loses the bag with 30% chance
        boolean lostBag = Math.random() <= 0.3;
        int bagID = porter.dropBag();

        if (!lostBag)
            bagsInBelt.add(bagID);

        repo.updateBaggageInBelt(bagsInBelt.size());
        repo.updatePorterState(PorterState.AT_THE_LUGGAGE_BELT_CONVEYOR);

        //if (repo.getPassengerSituation(bagID) == PassengerSituation.ARRIVED_FINAL_DESTINATION || repo.getBaggageInPlane() == 0)
        notifyAll();
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
