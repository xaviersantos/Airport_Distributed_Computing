package airport.MainProgram;

import airport.InterveningEntities.*;
import genclass.GenericIO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Logger for the program.
 * Implements the LoggerInterface interface, with a logger that writes the logs
 * to a file specified on the SimulationParameters class.
 * Also has the intervining entities and the shared regions needed to print the logs.
 *
 * @author Xavier Santos
 */

public class GeneralRepoOfInfo {

    /**
     * Total number of passengers.
     */
    private final int numPassengers = SimulationParameters.N_PASSENGERS;

    /**
     * FN - Flight number
     */
    private int flightNumber;

    /**
     * BN - number of pieces of luggage presently at the plane's hold
     */
    private int baggageInPlane;

    /**
     * Stat - state of the porter
     */
    private PorterState porterState;

    /**
     * CB - number of pieces of luggage presently on the conveyor belt
     */
    private int baggageInBelt;

    /**
     * SR - number of pieces of luggage belonging to passengers in transit presently stored at the storeroom
     */
    private int baggageInStore;

    /**
     * Stat - state of the driver
     */
    private BusDriverState busDriverState;

    /**
     * Q# - occupation state for the waiting queue (passenger id / - (empty))
     */
    private int[] waitingQueueOccupation;

    /**
     * S# - occupation state for seat in the bus (passenger id / - (empty))
     */
    private int[] seatOccupation;

    /**
     * St# - state of passenger # (# - 0 .. 5)
     */
    private final PassengerState[] passengerStates = new PassengerState[numPassengers];

    /**
     * Si# - situation of passenger # (# - 0 .. 5) – TRT (in transit) / FDT (has this airport as her final destination)
     */
    private final PassengerSituation[] passengerSituations = new PassengerSituation[numPassengers];

    /**
     * NR# - number of pieces of luggage the passenger # (# - 0 .. 5) carried at the start of her journey
     */
    private final int[] passengerBaggageTotal = new int[numPassengers];

    /**
     * NA# - number of pieces of luggage the passenger # (# - 0 .. 5) she has presently collected
     */
    private final int[] passengerBaggageCollected = new int[numPassengers];

    /**
     * Number of bags lost during the day.
     */
    private int lostBags;

    /**
     * Total of bags that went through the airport.
     */
    private int baggageNumber;

    /**
     * Number of passengers who are left by taking another flight.
     */
    private int passengersInTransit;

    /**
     * Number of passengers who left this airport as their final destination.
     */
    private int passengersFinalDestination;

    /**
     * Get the number of the last flight to have arrived.
     *
     * @return flight number
     */
    public int getFlightNumber() {
        return flightNumber;
    }

    /**
     * Get the current situation of a passenger given his ID.
     *
     * @param index passenger's ID
     * @return passenger's situation
     */
    public PassengerSituation getPassengerSituation(int index) {
        return passengerSituations[index - 1];
    }

    /**
     * Get the number of bags still inside the plane.
     *
     * @return number of bags.
     */
    public int getBaggageInPlane() {
        return baggageInPlane;
    }

    /**
     * Logger constructor
     *
     * @param porterState            Stat
     * @param baggageInBelt          CB
     * @param baggageInStore         SR
     * @param busDriverState         Stat
     * @param waitingQueueOccupation Q#
     * @param seatOccupation         S#
     */
    public GeneralRepoOfInfo(PorterState porterState, int baggageInBelt,
                             int baggageInStore, BusDriverState busDriverState, int[] waitingQueueOccupation,
                             int[] seatOccupation) {

        this.porterState = porterState;
        this.baggageInBelt = baggageInBelt;
        this.baggageInStore = baggageInStore;
        this.busDriverState = busDriverState;
        this.waitingQueueOccupation = waitingQueueOccupation;
        this.seatOccupation = seatOccupation;
    }

    /**
     * Cleans the log file and logs the description of the problem and the header
     * of the log file.
     */
    public synchronized void initLog() {
        try {
            FileWriter fw = new FileWriter(SimulationParameters.FILENAME);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(" AIRPORT RHAPSODY - Description of the internal state of the problem\n");
            bw.newLine();

            bw.write("PLANE    PORTER                  DRIVER");
            bw.newLine();
            bw.write("FN BN  Stat CB SR   Stat ");

            for (int i = 0; i < numPassengers; i++) {
                bw.write(" Q" + String.format("%d", i));
            }

            for (int i = 0; i < SimulationParameters.N_BUS_SEATS; i++) {
                bw.write(" S" + String.format("%d", i));
            }

            bw.newLine();
            bw.write("                                                         PASSENGERS");
            bw.newLine();

            for (int i = 0; i < numPassengers; i++) {
                bw.write("St" + String.format("%d ", i));
                bw.write("Si" + String.format("%d ", i));
                bw.write("NR" + String.format("%d ", i));
                bw.write("NA" + String.format("%d ", i));
            }

            bw.newLine();

            bw.close();
            fw.close();
        } catch (IOException ex) {
            GenericIO.writelnString("initStateLog error - Could not write to logger file.");
            System.exit(1);
        }
    }

    /**
     * Logs the current state of the entities in the the airport to a file.
     */
    private synchronized void printLog() {
        try {
            FileWriter fw = new FileWriter(SimulationParameters.FILENAME, true);

            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(String.format("%2d %2d ", flightNumber, baggageInPlane));

            bw.write(String.format(" %4s ", porterState.toString()));
            bw.write(String.format("%2d %2d   ", baggageInBelt, baggageInStore));

            bw.write(String.format("%4s  ", busDriverState.toString()));

            for (int i = 0; i < numPassengers; i++) {
                if (waitingQueueOccupation[i] != 0)
                    bw.write(" " + waitingQueueOccupation[i] + " ");
                else
                    bw.write(" - ");
            }

            for (int i = 0; i < SimulationParameters.N_BUS_SEATS; i++) {
                if (seatOccupation[i] != 0)
                    bw.write(" " + seatOccupation[i] + " ");
                else
                    bw.write(" - ");
            }

            bw.newLine();

            for (int i = 0; i < numPassengers; i++) {
                if (passengerStates[i] != null && passengerSituations[i] != null)
                    bw.write(String.format("%3s %3s", passengerStates[i].toString(), passengerSituations[i].toString()));
                else
                    bw.write("--- ---");
                bw.write(String.format("  %d   %d  ", passengerBaggageTotal[i], passengerBaggageCollected[i]));
            }

            bw.newLine();

            bw.close();
            fw.close();
        } catch (IOException e) {
            GenericIO.writelnString("printLog error - Could not write to logger file.");
            System.exit(1);
        }
    }

    /**
     * Prints the final report of the airport's activity at the end of the day.
     */
    public synchronized void printFinalReport() {
        try {
            FileWriter fw = new FileWriter(SimulationParameters.FILENAME, true);

            BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write("Final Report");
            bw.newLine();
            bw.write(String.format("N. of passengers which have this airport as their final destination = %2d\n", passengersFinalDestination));
            bw.write(String.format("N. of passengers in transit = %2d\n", passengersInTransit));
            bw.write(String.format("N. of bags that should have been transported in the the planes hold = %2d\n", baggageNumber));
            bw.write(String.format("N. of bags that were lost = %2d\n", lostBags));

            bw.close();
            fw.close();
        } catch (IOException e) {
            GenericIO.writelnString("printLog error - Could not write to logger file.");
            System.exit(1);
        }
    }

    /**
     * Updates and clears the variables for a new batch of passengers to arrive.
     *
     * @param flightNumber number of the flight arriving
     * @param passengers array of passengers arriving in this flight.
     */
    public synchronized void newFlight(int flightNumber, Passenger[] passengers) {
        for (PassengerSituation passengerSituation : passengerSituations) {
            if (passengerSituation == PassengerSituation.IN_TRANSIT)
                passengersInTransit++;
            else
                passengersFinalDestination++;
        }

        this.flightNumber = flightNumber;
        this.baggageInPlane = 0;

        for (int i = 0; i < passengers.length; i++) {
            this.passengerStates[i] = passengers[i].getPassengerState();
            this.passengerSituations[i] = passengers[i].getPassengerSituation();
            this.passengerBaggageTotal[i] = passengers[i].getTotalBags();
            this.passengerBaggageCollected[i] = passengers[i].getCurrentBags();
            this.baggageInPlane += this.passengerBaggageTotal[i];

            this.baggageNumber += passengers[i].getTotalBags();
        }
    }

    /**
     * Updates the state of the bus driver.
     *
     * @param state new bus driver state
     */
    public synchronized void updateBusDriverState(BusDriverState state) {
        this.busDriverState = state;
        this.printLog();
    }

    /**
     * Updates the state of the porter.
     *
     * @param state new porter state
     */
    public synchronized void updatePorterState(PorterState state) {
        this.porterState = state;
        this.printLog();
    }

    /**
     * Updates the state of one of the passengers.
     *
     * @param id    id of the passenger
     * @param state new passenger state
     */
    public synchronized void updatePassengerState(int id, PassengerState state, PassengerSituation situation) {
        this.passengerStates[id - 1] = state;
        this.passengerSituations[id - 1] = situation;
        this.printLog();
    }

    /**
     * Updates the bags carried by one of the passengers.
     *
     * @param id id of the passenger
     * @param bags number of bags on the passenger
     */
    public synchronized void updatePassengerBags(int id, int bags) {
        this.passengerBaggageCollected[id - 1] = bags;
        this.printLog();
    }

    /**
     * Updates the occupation of the waiting queue for the transfer bus.
     *
     * @param waitingQueueOccupation int array of passenger's IDs
     */
    public synchronized void updateWaitingQueue(int[] waitingQueueOccupation) {
        this.waitingQueueOccupation = waitingQueueOccupation;
    }

    /**
     * Updates the occupation of the bus seats in the transfer bus.
     *
     * @param seatOccupation int array of passenger's IDs
     */
    public synchronized void updateSeatOccupation(int[] seatOccupation) {
        this.seatOccupation = seatOccupation;
    }

    /**
     * Decreases by one the number of bags currently inside the plane.
     */
    public synchronized void decrementBaggageInPlane() {
        this.baggageInPlane--;
    }

    /**
     * Updates the number of bags in the temporary storage unit.
     *
     * @param baggageInStore number of bags
     */
    public synchronized void updateBaggageInStore(int baggageInStore) {
        this.baggageInStore = baggageInStore;
    }

    /**
     * Updates the number of bags in the baggage collection point.
     *
     * @param baggageInBelt number of bags
     */
    public synchronized void updateBaggageInBelt(int baggageInBelt) {
        this.baggageInBelt = baggageInBelt;
    }

    /**
     * Updates the number of lost bags by adding more lost bags to the current number.
     *
     * @param lostBags number of new lost bags
     */
    public synchronized void updateBaggageLost(int lostBags) {
        this.lostBags += lostBags;
    }
}
