/**
 * Package containing the eight shared regions used in the program:
 * Arrival lounge, arrival terminal exit, arrival terminal transfer quay,
 * departure terminal entrance, departure terminal transfer quay,
 * baggage collection point, baggage reclaim office and temporary storage area.
 * All these classes have functions to be used by multiple threads:
 * the passengers, the porter and the bus driver.
 * Some functions will also change the state of the entities and call the logger.
 */
package airport.SharedRegions;
