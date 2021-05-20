package airport.SharedRegions;

import airport.InterveningEntities.Passenger;
import airport.InterveningEntities.PassengerState;
import airport.MainProgram.GeneralRepoOfInfo;

/**
 * Area where passengers go if not all their bags could be collected and report them as missing.
 * Afterwards the go to the arrival terminal exit and leave the airport.
 *
 * @author Xavier Santos
 */

public class BaggageReclaimOffice {
    /**
     * Number of bags that were reported lost.
     */
    private int lostBags;

    /**
     * Report the missing bags and go home afterwards.
     */
    public synchronized void reportMissingBags() {
        Passenger passenger = ((Passenger) Thread.currentThread());
        passenger.setPassengerState(PassengerState.AT_THE_BAGGAGE_RECLAIM_OFFICE);
        repo.updatePassengerState(passenger.getPassengerId(),
                passenger.getPassengerState(), passenger.getPassengerSituation());

        lostBags = passenger.getTotalBags() - passenger.getCurrentBags();
        repo.updateBaggageLost(lostBags);
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
