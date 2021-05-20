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
 * Area where passengers wait for in a queue and board the bus.
 *
 * @author Xavier Santos
 */

public class ArrivalTermTransfQuay {

    /**
     * Marks the time reference of the beginning of the day.
     */
    long startingTime = System.currentTimeMillis();

    /**
     * Total time duration of a working day.
     */
    final long dayDuration = 1000;

    /**
     * Flags if the bus is on terminal.
     */
    private boolean busOnTerminal = true;

    /**
     * Current waiting queue occupation.
     */
    private LinkedList<Integer> waitingQueue = new LinkedList<>();

    /**
     * Current seat occupation.
     */
    private LinkedList<Integer> seatOccupation = new LinkedList<>();

    /**
     * Current number of passengers on the bus.
     */
    private int passengersOnBus;

    /**
     * Get the current passengers position in the queue.
     *
     * @return int array with the id of the passengers enqueued.
     */
    public int[] getQueue() {
        LinkedList<Integer> queue = new LinkedList<>(waitingQueue);

        for(int i=queue.size(); i < SimulationParameters.N_PASSENGERS; i++)
            queue.add(0);
        return queue.stream().mapToInt(Integer::intValue).toArray();
    }

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

    public LinkedList<Integer> getSeats(){
        return seatOccupation;
    }

    /**
     * Checks if the work day is over.
     */
    public synchronized boolean hasWorkDayEnded(){
        long timeElapsed = System.currentTimeMillis() - startingTime;
        return timeElapsed >= dayDuration;
    }

    /**
     * Wakes the passengers waiting and the bus driver.
     * If the Passenger's place in the waiting queue equals the bus capacity
     * the Passenger stays
     */
    public synchronized void announcingBusBoarding(){
        boolean timeout = false;
        long timeStartBoarding = System.currentTimeMillis();
        long timeElapsed;

        /*
        for(int i=0;i<SimulationParameters.N_BUS_SEATS;i++){
            notify();
        }
         */
        notifyAll();

        while(!timeout && passengersOnBus < SimulationParameters.N_BUS_SEATS || passengersOnBus == 0){
            try{
                wait(100);
            }
            catch (InterruptedException e){
                GenericIO.writelnString("announcingBusBoarding - One thread of BusDriver was interrupted.");
                System.exit(1);
            }

            if(hasWorkDayEnded())
                break;

            timeElapsed = System.currentTimeMillis() - timeStartBoarding;
            timeout = timeElapsed >= 100;
        }
    }

    /**
     * When the bus is filled or there are no more passengers waiting,
     * drive the bus to the departure terminal
     */
    public synchronized void goToDepartureTerminal(){
        BusDriver busDriver = ((BusDriver)Thread.currentThread());

        try{
            wait(10);
        }
        catch (InterruptedException e){
            GenericIO.writelnString("goToDepartureTerminal - One thread of BusDriver was interrupted.");
            System.exit(1);
        }

        busDriver.setNPassengers(passengersOnBus);
        busOnTerminal = false;

        busDriver.setBusDriverState(BusDriverState.DRIVING_FORWARD);
        repo.updateBusDriverState(BusDriverState.DRIVING_FORWARD);
        GenericIO.writelnString("==BUS==" + seatOccupation.toString() + "==>");
    }

    /**
     * Change the bus state from DRIVING_BACKWARD to PARKING_AT_THE_ARRIVAL_TERMINAL
     */
    public synchronized void parkTheBus(){
        seatOccupation.clear();
        passengersOnBus = 0;

        BusDriver busDriver = ((BusDriver)Thread.currentThread());
        busDriver.setBusDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);
        repo.updateBusDriverState(BusDriverState.PARKING_AT_THE_ARRIVAL_TERMINAL);

        busOnTerminal = true;
    }

    /**
     * Passenger takes a bus
     */
    public synchronized void takeABus(){
        Passenger passenger = ((Passenger)Thread.currentThread());

        waitingQueue.add(passenger.getPassengerId());

        passenger.setPassengerState(PassengerState.AT_THE_ARRIVAL_TRANSFER_TERMINAL);
        repo.updateWaitingQueue(getQueue());
        repo.updatePassengerState(passenger.getPassengerId(),
                passenger.getPassengerState(), passenger.getPassengerSituation());

        //if(waitingQueue.size() == SimulationParameters.N_BUS_SEATS)
        notifyAll();
    }

    /**
     * While there are empty seats, the passengers will board the bus
     */
    public synchronized void enterTheBus(){
        Passenger passenger = ((Passenger)Thread.currentThread());

        while(!busOnTerminal || passengersOnBus == SimulationParameters.N_BUS_SEATS){
            try{
                wait();
            }
            catch (InterruptedException e){
                GenericIO.writelnString("enterTheBus - One thread of Passenger was interrupted.");
                System.exit(1);
            }
        }
        waitingQueue.remove(Integer.valueOf(passenger.getPassengerId()));
        seatOccupation.add(passenger.getPassengerId());
        passengersOnBus++;
        repo.updateWaitingQueue(getQueue());
        repo.updateSeatOccupation(getSeatOccupation());
        passenger.setPassengerState(PassengerState.TERMINAL_TRANSFER);
        repo.updatePassengerState(passenger.getPassengerId(),
                passenger.getPassengerState(), passenger.getPassengerSituation());

        GenericIO.writeString(String.format("Passenger %d boarded the bus.\n", passenger.getPassengerId()));

        //if(waitingQueue.isEmpty() || passengersOnBus == SimulationParameters.N_BUS_SEATS)
        notifyAll();
    }

    /**
     * General repository of information.
     */
    private GeneralRepoOfInfo repo;

    /**
     * Set the current repo
     * @param repo Logger to be used for the entity
     */
    public synchronized void setLogger(GeneralRepoOfInfo repo){
        this.repo = repo;
    }
}
