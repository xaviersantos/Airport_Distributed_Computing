package airport.MainProgram;

import airport.InterveningEntities.*;
import airport.SharedRegions.*;
import airport.utils;
import genclass.GenericIO;

/**
 * Main program class:
 * Main function launches all threads: a Porter, Bus Driver and passengers.
 * It uses the SimulationParameters class to get the simulation parameters.
 * The shared regions are also initialized, as well the logger and its parameters.
 *
 * @author Xavier Santos
 */

public class MainProgram {

    /**
     * Main method.
     *
     * @param args runtime arguments
     */
    public static void main(String[] args) {

        //Problem Initialization

        ArrivalLounge arrivalLounge = new ArrivalLounge();
        ArrivalTerminalExit arrivalTerminalExit = new ArrivalTerminalExit();
        ArrivalTermTransfQuay arrivalTermTransfQuay = new ArrivalTermTransfQuay();
        BaggageCollectionPoint baggageCollectionPoint = new BaggageCollectionPoint();
        BaggageReclaimOffice baggageReclaimOffice = new BaggageReclaimOffice();
        DepartureTerminalEntrance departureTerminalEntrance = new DepartureTerminalEntrance();
        DepartureTermTransfQuay departureTermTransfQuay = new DepartureTermTransfQuay(arrivalTermTransfQuay);
        TmpStorageArea tmpStorageArea = new TmpStorageArea();

        Porter porter = new Porter(PorterState.WAITING_FOR_A_PLANE_TO_LAND, arrivalLounge, baggageCollectionPoint, tmpStorageArea);

        BusDriver busDriver = new BusDriver(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL, arrivalTermTransfQuay, departureTermTransfQuay);

        Passenger[] passengers = new Passenger[SimulationParameters.N_PASSENGERS];

        GeneralRepoOfInfo generalRepoOfInfo = new GeneralRepoOfInfo(porter.getPorterState(), baggageCollectionPoint.getNumberOfBags(),
                tmpStorageArea.getNumberOfBags(), busDriver.getBusDriverState(), arrivalTermTransfQuay.getQueue(),
                arrivalTermTransfQuay.getSeatOccupation());
        generalRepoOfInfo.initLog();

        arrivalLounge.setLogger(generalRepoOfInfo);
        arrivalTerminalExit.setLogger(generalRepoOfInfo);
        arrivalTermTransfQuay.setLogger(generalRepoOfInfo);
        baggageCollectionPoint.setLogger(generalRepoOfInfo);
        baggageReclaimOffice.setLogger(generalRepoOfInfo);
        departureTerminalEntrance.setLogger(generalRepoOfInfo);
        departureTermTransfQuay.setLogger(generalRepoOfInfo);
        tmpStorageArea.setLogger(generalRepoOfInfo);

        //Start of Simulation

        porter.start();
        GenericIO.writelnString("Porter has started.");

        busDriver.start();
        GenericIO.writelnString("Bus Driver has started.");

        for (int n = 1; n <= SimulationParameters.N_LANDINGS; n++) {

            for (int i = 0; i < passengers.length; i++) {
                passengers[i] = new Passenger(i + 1, utils.randInt(0, SimulationParameters.MAX_N_BAGS), arrivalLounge,
                        baggageCollectionPoint, baggageReclaimOffice, departureTermTransfQuay,
                        departureTerminalEntrance, arrivalTerminalExit, arrivalTermTransfQuay);
            }

            generalRepoOfInfo.newFlight(n, passengers);
            GenericIO.writelnString(String.format("\n----Flight %d has landed----\n", n));
            arrivalLounge.setPassengersInPlane(passengers.length);

            for (Passenger passenger : passengers) {
                passenger.start();
                GenericIO.writelnString(String.format("Passenger %d has started.", passenger.getPassengerId()));
            }

            //Wait for the end of simulation

            for (Passenger passenger : passengers) {
                try {
                    passenger.join();
                } catch (InterruptedException e) {
                    GenericIO.writeString("Main Program - One thread of Passenger was interrupted.");
                    System.exit(1);
                }
                GenericIO.writelnString(String.format("Passenger %d has ended.", passenger.getPassengerId()));
            }
        }

        try {
            porter.join();
        } catch (InterruptedException e) {
            GenericIO.writeString("Main Program - One thread of Porter was interrupted.");
            System.exit(1);
        }
        GenericIO.writelnString("Porter has ended.");

        try {
            busDriver.join();
        } catch (InterruptedException e) {
            GenericIO.writeString("Main Program - One thread of BusDriver was interrupted.");
            System.exit(1);
        }
        GenericIO.writelnString("Bus Driver has ended.");

        generalRepoOfInfo.printFinalReport();
    }
}
