package airport.InterveningEntities;

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
     * @param arrivalLounge
     * @param baggageCollectionPoint
     * @param tmpStorageArea
     */
    public Porter(ArrivalLounge arrivalLounge, BaggageCollectionPoint baggageCollectionPoint, TmpStorageArea tmpStorageArea) {
        this.arrivalLounge = arrivalLounge;
        this.baggageCollectionPoint = baggageCollectionPoint;
        this.tmpStorageArea = tmpStorageArea;
    }

    /**
     * Implements the life-cycle of the porter.
     */
    @Override
    public void run(){
        /**
         * Porter waits for a plane to land.
         */
        arrivalLounge.takeARest();

        arrivalLounge.tryToCollectABag();

        arrivalLounge.noMoreBagsToCollect();


    }
}

;