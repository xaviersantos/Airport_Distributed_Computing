package airport.InterveningEntities;

import airport.MainProgram.SimulationParameters;
import airport.SharedRegions.ArrivalLounge;
import airport.SharedRegions.BaggageCollectionPoint;
import airport.SharedRegions.TmpStorageArea;

/**
 * Porter thread:
 * Implements the life-cycle of the porter and stores the variables referent to
 * the porter and his current state.
 *
 * @author Xavier Santos
 */
public class Porter extends Thread{
    /**
     * The bag carried by the porter.
     */
    private int carriedBag;

    /**
     * Flags if the bag he's carrying belongs to a passenger with a transfer flight.
     */
    private boolean bagInTransit;

    /**
     * The number of the flight the porter is currently attending to.
     */
    private int currentFlightNumber;

    /**
     * Current porter state.
     */
    private PorterState state;

    /**
     * Instance of the Arrival Lounge.
     */
    private final ArrivalLounge arrivalLounge;

    /**
     * Instance of the Baggage Collection Point.
     */
    private final BaggageCollectionPoint baggageCollectionPoint;

    /**
     * Instance of the Temporary Storage Area.
     */
    private final TmpStorageArea tmpStorageArea;

    /**
     * Get the number of the last flight to have arrived.
     *
     * @return flight number
     */
    public int getCurrentFlightNumber() { return currentFlightNumber; }

    /**
     * Set to true if the bag belongs to a passenger in transit
     *
     * @param bagInTransit boolean
     */
    public void setBagInTransit(boolean bagInTransit){
        this.bagInTransit = bagInTransit;
    }

    /**
     * Sets the currently carried bag by the porter.
     *
     * @param bag id of the bag's owner
     */
    public void collectBag(int bag){
        this.carriedBag = bag;
    }

    /**
     * Set the carried bag to zero and returns the bag that was being carried.
     *
     * @return id of the bag's owner
     */
    public int dropBag(){
        int droppedBag = this.carriedBag;
        this.carriedBag = 0;
        return droppedBag;
    }

    /**
     * Get the porter state
     *
     * @return porter state
     */
    public PorterState getPorterState() {
        return state;
    }

    /**
     * Set the porter state
     *
     * @param state porter state
     */
    public void setPorterState(PorterState state) {
        this.state = state;
    }

    /**
     * Porter constructor
     *
     * @param state initial state of the porter
     * @param arrivalLounge instance of arrival lounge
     * @param baggageCollectionPoint instance of baggage collection point
     * @param tmpStorageArea instance of temporary storage area
     */
    public Porter(PorterState state, ArrivalLounge arrivalLounge, BaggageCollectionPoint baggageCollectionPoint,
                  TmpStorageArea tmpStorageArea) {

        this.state = state;
        this.arrivalLounge = arrivalLounge;
        this.baggageCollectionPoint = baggageCollectionPoint;
        this.tmpStorageArea = tmpStorageArea;
    }

    /**
     * Implements the life-cycle of the porter.
     */
    @Override
    public void run(){

        for(int landingNumber = 1; landingNumber <= SimulationParameters.N_LANDINGS; landingNumber++) {

            this.currentFlightNumber = landingNumber;

            //Porter waits for a plane to land
            arrivalLounge.takeARest();

            while (this.state != PorterState.WAITING_FOR_A_PLANE_TO_LAND) {
                //Porter tries to collect a bag from the baggage collection point
                arrivalLounge.tryToCollectABag();

                if (carriedBag != 0) {
                    //Porter takes the bag to either the BaggageCollectionPoint or the TmpStorageArea
                    if (!bagInTransit)
                        baggageCollectionPoint.carryItToAppropriateStore();
                    else
                        tmpStorageArea.carryItToAppropriateStore();
                }
                arrivalLounge.noMoreBagsToCollect();
            }
        }
    }
}