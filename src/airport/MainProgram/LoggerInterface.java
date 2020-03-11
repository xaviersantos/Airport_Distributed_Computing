
package airport.MainProgram;

import airport.InterveningEntities.*;

/**
 *
 * Interface for a logger for the program.
 * 
 * @author Xavier Santos
 */
public interface LoggerInterface {
    
    /**
     * Cleans the log file and logs the description of the problem and the header
     * of the log file.
     */
    public void initLog();

    /**
     * Updates the state of the bus driver
     * @param state new bus driver state
     */
    public void updateBusDriverState(BusDriverState state);

    /**
     * Updates the state of the porter
     * @param state new porter state
     */
    public void updatePorterState(PorterState state);
    
    /**
     * Updates the state of the passenger
     * @param id id of the passenger
     * @param state new passenger state
     */
    public void updatePassengerState(int id, PassengerState state);

}
