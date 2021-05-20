package airport.InterveningEntities;

import airport.SharedRegions.ArrivalTermTransfQuay;
import airport.SharedRegions.DepartureTermTransfQuay;

/**
 * BusDriver thread:
 * Implements the life-cycle of the porter and stores the variables referent to
 * the bus driver and his current state.
 *
 * @author Xavier Santos
 */

public class BusDriver extends Thread {

    /**
     * Current bus driver state.
     */
    private BusDriverState state;

    /**
     * Current number of passengers on the bus.
     */
    private int nPassengers;

    /**
     * Get the current number of passengers on the bus.
     *
     * @return number of passengers
     */
    public int getNPassengers() {
        return nPassengers;
    }

    /**
     * Set Get the current number of passengers on the bus.
     *
     * @param nPassengers number of passengers
     */
    public void setNPassengers(int nPassengers) {
        this.nPassengers = nPassengers;
    }

    /**
     * Instance of the Arrival Terminal Transference Quay.
     */
    private final ArrivalTermTransfQuay arrivalTermTransfQuay;

    /**
     * Instance of the Departure Terminal Transference Quay.
     */
    private final DepartureTermTransfQuay departureTermTransfQuay;

    /**
     * Get the bus driver state
     *
     * @return bus driver state
     */
    public BusDriverState getBusDriverState() {
        return state;
    }

    /**
     * Set the bus driver state
     *
     * @param state bus driver state
     */
    public void setBusDriverState(BusDriverState state) {
        this.state = state;
    }

    /**
     * BusDriver constructor
     *
     * @param arrivalTermTransfQuay   instance of arrival terminal transfer quay
     * @param departureTermTransfQuay instance of departure terminal transfer quay
     */
    public BusDriver(BusDriverState state, ArrivalTermTransfQuay arrivalTermTransfQuay, DepartureTermTransfQuay departureTermTransfQuay) {
        this.state = state;
        this.arrivalTermTransfQuay = arrivalTermTransfQuay;
        this.departureTermTransfQuay = departureTermTransfQuay;
    }

    /**
     * Implements the life-cycle of the bus driver.
     */
    @Override
    public void run() {

        while (!arrivalTermTransfQuay.hasWorkDayEnded()) {

            //Bus driver checks if the day has ended and, if not, wakes the passengers in the queue.
            arrivalTermTransfQuay.announcingBusBoarding();

            if (arrivalTermTransfQuay.hasWorkDayEnded())
                break;

            //Driver takes the bus to departure terminal after it is full or enough time has passed.
            arrivalTermTransfQuay.goToDepartureTerminal();

            //Driver waits for all passengers to leave the bus.
            departureTermTransfQuay.parkTheBusAndLetPassOff();

            //When the bus is empty go back to the arrival terminal.
            departureTermTransfQuay.goToArrivalTerminal();

            //Park the bus and wait for passengers.
            arrivalTermTransfQuay.parkTheBus();
        }
    }
}
