package airport.SharedRegions;

import airport.InterveningEntities.BusDriver;
import airport.InterveningEntities.BusDriverState;
import airport.InterveningEntities.Passenger;
import airport.InterveningEntities.PassengerState;
import airport.MainProgram.GeneralRepoOfInfo;
import airport.MainProgram.SimulationParameters;
import genclass.GenericIO;

import java.util.LinkedList;

/**
 * Area where the bus drops the passengers who are taking a transfer flight
 * and then returns to the other terminal transfer quay
 * while the passengers proceed to the departure terminal
 *
 * @author Xavier Santos
 */

public class DepartureTermTransfQuay {
    /**
     * Number of passengers currently inside the bus.
     */
    private int passengersOnBus;

    /**
     * Flags if the bus is parked on the departure terminal transfer quay.
     */
    private boolean busParked = false;

    /**
     * List with the current seat occupation on the bus.
     */
    private LinkedList<Integer> seatOccupation = new LinkedList<>();

    /**
     * Get the current seat occupation on the bus.
     *
     * @return int array with the id of the passengers seated
     */
    public int[] getSeatOccupation() {
        LinkedList<Integer> seats = new LinkedList<>(seatOccupation);

        for(int i=seats.size(); i < SimulationParameters.N_BUS_SEATS; i++)
            seats.add(0);
        return seats.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * When the bus is empty, drive the bus to the arrival terminal
     */
    public synchronized void goToArrivalTerminal() {

        while (passengersOnBus != 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                GenericIO.writelnString("goToArrivalTerminal - One thread of BusDriver was interrupted.");
                System.exit(1);
            }
        }
        busParked = false;
        BusDriver busDriver = ((BusDriver) Thread.currentThread());
        busDriver.setBusDriverState(BusDriverState.DRIVING_BACKWARD);
        repo.updateBusDriverState(BusDriverState.DRIVING_BACKWARD);
        GenericIO.writelnString("<====BUS===");
    }

    /**
     * BusDriver drops the passengers at DepartureTerminalEntrance and
     * change the bus state from DRIVING_FORWARD to PARKING_AT_THE_DEPARTURE_TERMINAL
     */
    public synchronized void parkTheBusAndLetPassOff() {
        BusDriver busDriver = ((BusDriver) Thread.currentThread());
        busDriver.setBusDriverState(BusDriverState.PARKING_AT_THE_DEPARTURE_TERMINAL);
        //Arrays.fill(seatOccupation, 0);
        //repo.updateSeatOccupation(seatOccupation);
        repo.updateBusDriverState(BusDriverState.PARKING_AT_THE_DEPARTURE_TERMINAL);

        seatOccupation = arrivalTermTransfQuay.getSeats();
        busParked = true;
        passengersOnBus = busDriver.getNPassengers();
        busDriver.setNPassengers(0);

        notifyAll();
    }

    /**
     * Passengers leaves the bus and the last one wakes the BusDriver.
     */
    public synchronized void leaveTheBus() {

        while (!busParked) {
            try {
                wait();
            } catch (InterruptedException e) {
                GenericIO.writelnString("leaveTheBus - One thread of Passenger was interrupted.");
                System.exit(1);
            }
        }
        Passenger passenger = ((Passenger) Thread.currentThread());
        seatOccupation.remove(Integer.valueOf(passenger.getPassengerId()));
        passengersOnBus--;
        repo.updateSeatOccupation(getSeatOccupation());

        passenger.setPassengerState(PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL);
        repo.updatePassengerState(passenger.getPassengerId(),
                PassengerState.AT_THE_DEPARTURE_TRANSFER_TERMINAL, passenger.getPassengerSituation());

        GenericIO.writeString(String.format("Passenger %d left the bus.\n", passenger.getPassengerId()));
        if (passengersOnBus == 0)
            notifyAll();
    }

    /**
     * General repository of information.
     */
    private GeneralRepoOfInfo repo;

    /**
     * Instance of the ArrivalTermTransfQuay.
     */
    private ArrivalTermTransfQuay arrivalTermTransfQuay;

    /**
     * Set the current repo.
     *
     * @param repo Logger to be used for the entity
     */
    public synchronized void setLogger(GeneralRepoOfInfo repo) {
        this.repo = repo;
    }

    public DepartureTermTransfQuay(ArrivalTermTransfQuay arrivalTermTransfQuay) {
        this.arrivalTermTransfQuay = arrivalTermTransfQuay;
    }
}
