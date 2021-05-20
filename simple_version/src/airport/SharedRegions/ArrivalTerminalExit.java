package airport.SharedRegions;

import airport.InterveningEntities.Passenger;
import airport.InterveningEntities.PassengerSituation;
import airport.InterveningEntities.PassengerState;
import airport.MainProgram.GeneralRepoOfInfo;

/**
 * Area where the passengers leave the airport and end their journey.
 *
 * @author Xavier Santos
 */

public class ArrivalTerminalExit {
    /**
     * Passenger leaves.
     * Wake up next passenger EXITING_THE_ARRIVAL_TERMINAL.
     */
    public synchronized void goHome() {
        Passenger passenger = ((Passenger) Thread.currentThread());
        passenger.setPassengerSituation(PassengerSituation.ARRIVED_FINAL_DESTINATION);
        passenger.setPassengerState(PassengerState.EXITING_THE_ARRIVAL_TERMINAL);
        repo.updatePassengerState(passenger.getPassengerId(), passenger.getPassengerState(), passenger.getPassengerSituation());

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
