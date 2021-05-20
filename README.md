# Airport rhapsody

The described activities take place at an airport, somewhere in Portugal, and aim to portray what
happens when passengers arrive from a flight. There are eight main locations: the arrival lounge, the
baggage collection point, the temporary storage area (for holding the luggage of passengers in transit), the
baggage reclaim office, the terminal transfer quays, the arrival terminal exit and the departure terminal
entrance.

There are three types of entities: the _passengers_ , who terminate their voyage at the airport or are in
transit, the _porter_ , who unloads the the bags from a plane, when it lands, and carries them to the baggage
collection point or to the temporary storage area, and the _bus driver_ , who moves the passengers in transit
between the arrival and the departure terminals.

_K_ plane landings are assumed, each involving the arrival of _N_ passengers. Each passenger carries 0 to
_M_ pieces of luggage in the plane hold. The bus, which moves the passengers between terminals, has a
capacity of _T_ seating places.

Activities are organized, for each plane landing, in the following way:

- the passengers walk from the arrival lounge to the baggage collection point, if their journey ends at
    this airport and have bags to collect; those without bags go directly to the arrival terminal exit and
    leave the airport; the remaining passengers, who are in transit, walk to the terminal transfer quay;
- after all the passengers have left the plane, the porter picks up the pieces of luggage, one by one,
    from the plane hold and carries them either to the baggage collection point, or to the temporary
    storage area, as they belong to local or in transit passengers, respectively;
- in the baggage collection point, the passengers wait for the arrival of their bags; upon taking
    possession of them, they walk to the arrival terminal exit and leave the airport; those with missed
    bags go first to the baggage reclaim office to post their complaint, before walking to the arrival
    terminal exit and leave the airport;
- on the terminal transfer quay, the passengers wait for the bus arrival, which will take them to the
    departure terminal for the next leg of the journey;
- the bus leaves the terminal transfer quay according to a predefined schedule, executing a circular
    path which has as another stop the terminal transfer quay of the departure terminal; however, if it
    happens that all seats are occupied prior to the predefined time to leave, the driver may depart
    sooner.
In the end of the day, a full report of the activities is issued.
Assume that there are five plane landings, each involving the arrival of six passengers, carrying a
maximum of two pieces of luggage in the plane hold and that the transfer bus has a capacity of three
seating places. Write a simulation of the life cycle of the passengers, the porter and the bus driver using
one of the models for _thread_ communication and synchronization which have been studied: monitors or
semaphores and shared memory.

One aims for a distributed solution with multiple information sharing regions, written in Java, run in
Linux and which terminates. A _logging_ file, which describes the evolution of the internal state of the
problem in a clear and precise way, must be included.


### Characterization of the interaction

**Porter:**
- _WAITING_FOR_A_PLANE_TO_LAND_ – _blocking state_ (initial / final state)
The porter is waken up by the operation _whatShouldIDo_ of the last of the passengers to
reach the arrival lounge
- _AT_THE_PLANES_HOLD_ – _transition state
- _AT_THE_LUGGAGE_BELT_CONVEYOR_ – _transition state
- _AT_THE_STOREROOM_ – _transition state_

**Passenger:**
- _AT_THE_DISEMBARKING_ZONE_ – _transition state_ (initial state)
- _AT_THE_LUGGAGE_COLLECTION_POINT_ – _blocking state with eventual transition_
the passenger is waken up by the operations _carryItToAppropriateStore_ and _tryToCollectABag_
of the porter when he places on the conveyor belt a bag she owns, the former, or when he
signals that there are no more pieces of luggage in the plane hold, the latter, and makes a tran-
sition when either she has in her possession all the bags she owns, or was signaled that there
are no more bags in the plane hold
- _AT_THE_BAGGAGE_RECLAIM_OFFICE_ – _transition state
EXITING_THE_ARRIVAL_TERMINAL_ – _blocking state with eventual transition_ (final state)
the passenger is waken up by the operations _goHome_ or _prepareNextLeg_ of the last passen-
ger of each flight to exit the arrival terminal or to enter the departure terminal
- _AT_THE_ARRIVAL_TRANSFER_TERMINAL_ – _blocking state_
before blocking, the passenger wakes up the bus driver, if her place in the waiting queue equals
the bus capacity, and is waken up by the operation _announcingBusBoarding_ of the driver to
mimic her entry in the bus
- _TERMINAL_TRANSFER_ – _blocking state_
the passenger is waken up by the operation _parkTheBusAndLetPassOff_ of the driver
- _AT_THE_DEPARTURE_TRANSFER_TERMINAL_ – _transition state
ENTERING_THE_DEPARTURE_TERMINAL_ – _blocking state with eventual transition_ (final state)
the passenger is waken up by the operations _goHome_ or _prepareNextLeg_ of the last passen-
ger of each flight to exit the arrival terminal or to enter the departure terminal

**Bus driver:**
- _PARKING_AT_THE_ARRIVAL_TERMINAL_ – _double blocking state_ (initial / final state)
the driver is waken up the first time by the operation _takeABus_ of the passenger who arrives at
the transfer terminal and finds out her place in the waiting queue equals the bus capacity, or
when the departure time has been reached (transition will only occurs if there is at least one
passenger forming the queue); the driver is waken up the second time by the operation _enter
TheBus_ of the last passenger to enter the bus
- _DRIVING_FORWARD_ – _transition state
- _PARKING_AT_THE_DEPARTURE_TERMINAL_ – _blocking state_
the driver is waken up by the operation _leaveTheBus_ of the last passenger to exit the bus
- _DRIVING_BACKWARD_ – _transition state_
