package airport.InterveningEntities;

import airport.SharedRegions.*;

/**
 * Passenger thread:
 * Implements the life-cycle of the passenger and stores the variables referent to
 * the passenger and his current state.
 *
 * @author Xavier Santos
 */

public class Passenger extends Thread {
    /**
     * Current passenger id.
     */
    private final int id;

    /**
     * Current passenger bags.
     */
    private int[] bags;

    /**
     * Current passenger state.
     */
    private Passenger state;

    /**
     * What to do at the arrival lounge.
     */
    private Passenger.Decide decision;

    /**
     * Instance of the Arrival Lounge.
     */
    private final ArrivalLounge arrivalLounge;

    /**
     * Instance of the Baggage Collection Point.
     */
    private final BaggageCollectionPoint baggageCollectionPoint;

    /**
     * Instance of the Baggage Reclaim Office.
     */
    private final BaggageReclaimOffice baggageReclaimOffice;

    /**
     * Instance of the Departure Terminal Transfer Quay.
     */
    private final DepartureTermTransfQuay departureTermTransfQuay;

    /**
     * Instance of the Departure Terminal Entrance.
     */
    private final DepartureTerminalEntrance departureTerminalEntrance;

    /**
     * Instance of the Arrival Terminal Exit.
     */
    private final ArrivalTerminalExit arrivalTerminalExit;

    /**
     * Instance of the Arrival Terminal Transfer Quay.
     */
    private final ArrivalTermTransfQuay arrivalTermTransfQuay;

    /**
     * Get the passenger state
     *
     * @return passenger state
     */
    public Passenger getPassengerState() {
        return state;
    }

    /**
     * Set the passenger state
     *
     * @param state passenger state
     */
    public void setPassengerState(Passenger state) {
        this.state = state;
    }

    /**
     * Passenger constructor
     * @param id spectator id
     * @param arrivalLounge instance of arrival lounge
     * @param baggageCollectionPoint instance of baggage collection point
     * @param baggageReclaimOffice instance of baggage reclaim office
     * @param departureTermTransfQuay
     * @param departureTerminalEntrance instance of departure terminal entrance
     * @param arrivalTerminalExit instance of arrival terminal exit
     * @param arrivalTermTransfQuay
     */
    public Passenger(int id, ArrivalLounge arrivalLounge, BaggageCollectionPoint baggageCollectionPoint,
                     BaggageReclaimOffice baggageReclaimOffice, DepartureTermTransfQuay departureTermTransfQuay,
                     DepartureTerminalEntrance departureTerminalEntrance, ArrivalTerminalExit arrivalTerminalExit,
                     ArrivalTermTransfQuay arrivalTermTransfQuay) {
        this.id = id;
        this.arrivalLounge = arrivalLounge;
        this.baggageCollectionPoint = baggageCollectionPoint;
        this.baggageReclaimOffice = baggageReclaimOffice;
        this.departureTermTransfQuay = departureTermTransfQuay;
        this.departureTerminalEntrance = departureTerminalEntrance;
        this.arrivalTerminalExit = arrivalTerminalExit;
        this.arrivalTermTransfQuay = arrivalTermTransfQuay;
    }

    /**
     * Implements the life-cycle of the passenger.
     */
    @Override
    public void run() {
        /**
         * Decide what to do.
         */
        arrivalLounge.whatShouldIDo();

        /**
         * Act based on that decision.
         */
        if (this.decision == Decide.goHome) {
            /**
             * Leave the airport through the arrival terminal exit.
             */
            arrivalTerminalExit.goHome();
        }
        else if(this.decision == Decide.goCollectBag){
            baggageCollectionPoint.goCollectBag();

            //IF HE IS MISSING ONE OF HIS BAGS
                baggageReclaimOffice.reportMissingBags();

            arrivalTerminalExit.goHome();
        }
        else if(this.decision == Decide.takeABus){
            /**
             * Go to ArrivalTransfQuay and wait in a queue for a bus.
             */
            arrivalLounge.takeABus();

            /**
             * When there is a free space on the bus enter and wait for it to reach the destination.
             */
            arrivalTermTransfQuay.enterTheBus();

            /**
             * After reaching the destination leave the bus at the DepartureTermTransfQuay.
             */
            departureTermTransfQuay.leaveTheBus();

            /**
             * Exit the terminal and take the next flight
             */
            departureTerminalEntrance.prepareNextLeg();
        }
    }

    /**
     * Enum with the possible decisions during whatShouldIDo phase.
     */
    enum Decide { goCollectBag, goHome, takeABus }
}