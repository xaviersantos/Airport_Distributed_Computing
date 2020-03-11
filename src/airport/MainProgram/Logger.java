package airport.MainProgram;

import airport.InterveningEntities.BusDriverState;
import airport.InterveningEntities.PassengerState;
import airport.InterveningEntities.PorterState;
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

public class Logger implements LoggerInterface{
    public Logger(int[] passengerIds, PassengerState[] passengerStates, PorterState porterState, BusDriverState busDriverState) {
    }

    public synchronized void initLog() {

    }

    /**
     * Updates the state of the bus driver.
     * @param state new bus driver state
     */
    @Override
    public void updateBusDriverState(BusDriverState state) {

    }

    /**
     * Updates the state of the porter.
     * @param state new porter state
     */
    @Override
    public void updatePorterState(PorterState state) {

    }

    /**
     * Updates the state of one of the passengers.
     * @param id id of the passenger
     * @param state new passenger state
     */
    @Override
    public void updatePassengerState(int id, PassengerState state) {

    }
}
