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
     * Total number of bags at the start of his journey.
     */
    private final int totalBags;

    /**
     * Current passenger bags.
     */
    private int currentBags;

    /**
     * Current passenger state.
     */
    private PassengerState state;

    /**
     * Current situation of passenger - TRT (in transit) / FDT (has this airport as her final destination)
     */
    private PassengerSituation situation;

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
    public PassengerState getPassengerState() {
        return state;
    }

    /**
     * Set the passenger state
     *
     * @param state passenger state
     */
    public void setPassengerState(PassengerState state) {
        this.state = state;
    }

    /**
     * Get the passenger situation
     *
     * @return passenger situation
     */
    public PassengerSituation getPassengerSituation() {
        return situation;
    }

    /**
     * Set the passenger situation
     *
     * @param situation passenger situation
     */
    public void setPassengerSituation(PassengerSituation situation) {
        this.situation = situation;
    }

    /**
     * Get passenger id
     *
     * @return id
     */
    public int getPassengerId() {
        return id;
    }

    /**
     * Get the total number of bags the passenger had at the start of his journey
     *
     * @return number of bags
     */
    public int getTotalBags() {
        return totalBags;
    }

    /**
     * Get the number the bags the passenger has at this moment
     *
     * @return number of bags
     */
    public int getCurrentBags() {
        return currentBags;
    }

    /**
     * Increments by one the number of bags in the passenger's possession.
     */
    public void collectBag() {
        currentBags++;
    }

    /**
     * Passenger constructor
     *
     * @param id                        spectator id
     * @param totalBags                 total number of bags at the start of his journey
     * @param arrivalLounge             instance of arrival lounge
     * @param baggageCollectionPoint    instance of baggage collection point
     * @param baggageReclaimOffice      instance of baggage reclaim office
     * @param departureTermTransfQuay   instance of departure terminal transference quay
     * @param departureTerminalEntrance instance of departure terminal entrance
     * @param arrivalTerminalExit       instance of arrival terminal exit
     * @param arrivalTermTransfQuay     instance of arrival terminal transference quay
     */
    public Passenger(int id, int totalBags, ArrivalLounge arrivalLounge,
                     BaggageCollectionPoint baggageCollectionPoint,
                     BaggageReclaimOffice baggageReclaimOffice, DepartureTermTransfQuay departureTermTransfQuay,
                     DepartureTerminalEntrance departureTerminalEntrance, ArrivalTerminalExit arrivalTerminalExit,
                     ArrivalTermTransfQuay arrivalTermTransfQuay) {

        this.id = id;
        this.totalBags = totalBags;
        this.currentBags = 0;
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

        //Decide what to do
        arrivalLounge.whatShouldIDo();

        //Act based on that decision
        if (this.situation == PassengerSituation.ARRIVED_FINAL_DESTINATION) {
            if(this.totalBags == 0) {
                //Leave the airport through the arrival terminal exit
                arrivalTerminalExit.goHome();
            }
            else{
                baggageCollectionPoint.goCollectBag();

                //IF HE IS MISSING ONE OF HIS BAGS
                if (this.currentBags != this.totalBags) {
                    //Go to the BaggageReclaimOffice and report a missing bag
                    baggageReclaimOffice.reportMissingBags();
                }
            }
            arrivalTerminalExit.goHome();

        } else if (this.situation == PassengerSituation.IN_TRANSIT) {
            //Go to ArrivalTransfQuay and wait in a queue for a bus
            arrivalTermTransfQuay.takeABus();

            //When there is a free space on the bus enter and wait for it to reach the destination
            arrivalTermTransfQuay.enterTheBus();

            //After reaching the destination leave the bus at the DepartureTermTransfQuay
            departureTermTransfQuay.leaveTheBus();

            //Exit the terminal and take the next flight
            departureTerminalEntrance.prepareNextLeg();
        }
    }
}