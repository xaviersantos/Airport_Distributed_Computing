package airport.SharedRegions;

import airport.InterveningEntities.*;
import airport.MainProgram.GeneralRepoOfInfo;
import airport.utils;
import genclass.GenericIO;

import java.util.Stack;

/**
 * Area where the passengers decide what their next move will be
 * and where the porter waits for the passengers to leave and then carries their bags to the respective places..
 *
 * @author Xavier Santos
 */

public class ArrivalLounge {

    /**
     * General repository of information.
     */
    private GeneralRepoOfInfo repo;

    /**
     * List of bags on the plane's hold from passengers on transit.
     */
    private Stack<Integer> bagsInTransit = new Stack<>();

    /**
     * List of bags on the plane's hold from passengers who arrived their final destination.
     */
    private Stack<Integer> bagsToCollect = new Stack<>();

    /**
     * Number of passengers still inside the plane.
     */
    private int passengersInPlane;

    /**
     * Sets the number of passengers still inside the plane.
     *
     * @param passengersInPlane number of passengers.
     */
    public void setPassengersInPlane(int passengersInPlane) {
        this.passengersInPlane = passengersInPlane;
    }

    /**
     * Passenger decides what to do next and leaves the lounge.
     * The last passenger to leave wakes the Porter.
     */
    public synchronized void whatShouldIDo() {
        Passenger passenger = ((Passenger) Thread.currentThread());
        passenger.setPassengerState(PassengerState.AT_THE_DISEMBARKING_ZONE);

        passengersInPlane--;

        //Randomly decide what to do.
        int decision;

        //If the passenger only collects bags if he has them
        if (passenger.getTotalBags() != 0)
            decision = utils.randInt(1, 2);
        else
            decision = utils.randInt(2, 3);

        switch (decision) {
            case 1:
                //Collect bags
                passenger.setPassengerSituation(PassengerSituation.ARRIVED_FINAL_DESTINATION);

                for (int i = 0; i < passenger.getTotalBags(); i++) {
                    bagsToCollect.push(passenger.getPassengerId());
                }

                break;

            case 2:
                //Transfer to departure terminal
                passenger.setPassengerSituation(PassengerSituation.IN_TRANSIT);

                //Store bags of stack
                for (int i = 0; i < passenger.getTotalBags(); i++) {
                    bagsInTransit.push(passenger.getPassengerId());
                }

                break;

            case 3:
                //Go home
                passenger.setPassengerSituation(PassengerSituation.ARRIVED_FINAL_DESTINATION);

                break;
        }

        repo.updatePassengerState(passenger.getPassengerId(),
                passenger.getPassengerState(), passenger.getPassengerSituation());

        if (passengersInPlane == 0)
            notifyAll();
    }

    /**
     * Porter waits for all the passengers to leave the lounge.
     */
    public synchronized void takeARest() {
        Porter porter = ((Porter) Thread.currentThread());

        while (passengersInPlane > 0 || porter.getCurrentFlightNumber() != repo.getFlightNumber()) {
            try {
                wait();
            } catch (InterruptedException e) {
                GenericIO.writelnString("takeARest - Thread of porter was interrupted.");
                System.exit(1);
            }
        }
        porter.setPorterState(PorterState.AT_THE_PLANES_HOLD);
        repo.updatePorterState(PorterState.AT_THE_PLANES_HOLD);
    }

    /**
     * Porter wakes from resting and tries to collect one bag.
     * When the last bag is collected signal the passengers waiting to go to the BaggageReclaimOffice
     */
    public synchronized void tryToCollectABag() {
        Porter porter = ((Porter) Thread.currentThread());
        porter.setPorterState(PorterState.AT_THE_PLANES_HOLD);
        repo.updatePorterState(PorterState.AT_THE_PLANES_HOLD);

        if (!bagsInTransit.isEmpty()) {
            porter.collectBag(bagsInTransit.pop());
            porter.setBagInTransit(true);
        } else if (!bagsToCollect.isEmpty()) {
            porter.collectBag(bagsToCollect.pop());
            porter.setBagInTransit(false);
        }

        if (repo.getBaggageInPlane() > 0)
            repo.decrementBaggageInPlane();
    }

    /**
     * Porter has no bags to collect and can go back to rest state.
     */
    public synchronized void noMoreBagsToCollect() {
        Porter porter = ((Porter) Thread.currentThread());
        if (repo.getBaggageInPlane() == 0) {
            porter.setPorterState(PorterState.WAITING_FOR_A_PLANE_TO_LAND);
            repo.updatePorterState(PorterState.WAITING_FOR_A_PLANE_TO_LAND);

            notifyAll();
        }
    }

    /**
     * Set the current repo
     *
     * @param repo Logger to be used for the entity
     */
    public synchronized void setLogger(GeneralRepoOfInfo repo) {
        this.repo = repo;
    }
}
