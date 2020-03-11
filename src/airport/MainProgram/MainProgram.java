package airport.MainProgram;

import airport.InterveningEntities.*;
import airport.SharedRegions.*;
import genclass.GenericIO;

/**
 * Main program class:
 * Main function launches all threads: a Porter, Bus Driver and passengers.
 * It uses the SimulationParemeters class to get the simulation parameters.
 * The shared regions are also initialized, as well the logger and its parameters.
 *
 * @author Xavier Santos
 */

public class MainProgram {

    /**
     *  Main method.
     *
     *  @param args runtime arguments
     */
    public static void main(String[] args) {
        Passenger[] passengers = new Passenger[SimulationParameters.N_PASSENGERS];

        /**
         * Problem Initialization.
         */

        ArrivalLounge arrivalLounge = new ArrivalLounge();
        ArrivalTerminalExit arrivalTerminalExit = new ArrivalTerminalExit();
        ArrivalTermTransfQuay arrivalTermTransfQuay = new ArrivalTermTransfQuay();
        BaggageCollectionPoint baggageCollectionPoint = new BaggageCollectionPoint();
        BaggageReclaimOffice baggageReclaimOffice = new BaggageReclaimOffice();
        DepartureTerminalEntrance departureTerminalEntrance = new DepartureTerminalEntrance();
        DepartureTermTransfQuay departureTermTransfQuay = new DepartureTermTransfQuay();
        TmpStorageArea tmpStorageArea = new TmpStorageArea();

        Porter porter = new Porter(arrivalLounge, baggageCollectionPoint, tmpStorageArea);
        PorterState porterState = PorterState.WAITING_FOR_A_PLANE_TO_LAND;

        BusDriver busDriver = new BusDriver(arrivalTermTransfQuay, departureTermTransfQuay);
        BusDriverState busDriverState = BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL;

        int[] passengerIds = new int[SimulationParameters.N_PASSENGERS];
        PassengerState[] passengerStates = new PassengerState[SimulationParameters.N_PASSENGERS];

        for(int i = 0; i < passengers.length; i++){
            passengers[i] = new Passenger(i, arrivalLounge, baggageCollectionPoint, baggageReclaimOffice, departureTermTransfQuay, departureTerminalEntrance, arrivalTerminalExit, arrivalTermTransfQuay);
            passengerIds[i] = i;
            passengerStates[i] = PassengerState.AT_THE_DISEMBARKING_ZONE;
        }

        Logger logger = new Logger(passengerIds, passengerStates, porterState, busDriverState);
        logger.initLog();

        arrivalLounge.setLogger(logger);
        arrivalTerminalExit.setLogger(logger);
        arrivalTermTransfQuay.setLogger(logger);
        baggageCollectionPoint.setLogger(logger);
        baggageReclaimOffice.setLogger(logger);
        departureTerminalEntrance.setLogger(logger);
        departureTermTransfQuay.setLogger(logger);
        tmpStorageArea.setLogger(logger);

        /**
         * Start of Simulation.
         */

        porter.start();

        busDriver.start();

        for (Passenger passenger : passengers) {
            passenger.start();
        }

        /**
         * Wait for the end of simulation.
         */

        for (Passenger passenger : passengers) {
            try{
                passenger.join();
            }
            catch(InterruptedException e){
                GenericIO.writeString("Main Program - One thread of Passenger was interrupted.");
                System.exit(1);
            }
        }

        try{
            porter.join();
        }
        catch(InterruptedException e){
            GenericIO.writeString("Main Program - One thread of Porter was interrupted.");
            System.exit(1);
        }

        try{
            busDriver.join();
        }
        catch(InterruptedException e){
            GenericIO.writeString("Main Program - One thread of BusDriver was interrupted.");
            System.exit(1);
        }
    }
}
