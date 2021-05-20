package airport.SharedRegions;

import airport.InterveningEntities.Passenger;
import airport.InterveningEntities.PassengerSituation;
import airport.InterveningEntities.PassengerState;
import airport.MainProgram.GeneralRepoOfInfo;

/**
 * Area where the passengers leave the airport by taking another plane.
 *
 * @author Xavier Santos
 */

public class DepartureTerminalEntrance {

    /**
     * Passenger enters the departure terminal and wakes the next passenger.
     */
    public synchronized void prepareNextLeg() {
        Passenger passenger = ((Passenger) Thread.currentThread());
        passenger.setPassengerState(PassengerState.ENTERING_THE_DEPARTURE_TERMINAL);
        passenger.setPassengerSituation(PassengerSituation.IN_TRANSIT);
        repo.updatePassengerState(passenger.getPassengerId(),
                PassengerState.ENTERING_THE_DEPARTURE_TERMINAL, PassengerSituation.IN_TRANSIT);

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
