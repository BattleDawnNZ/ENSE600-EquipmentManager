package equipmentmanagerapplication;

import java.io.Serializable;
import java.util.HashSet;

/**
 *
 * @author fmw5088
 */
public class LocationManager implements Serializable {

    /**
     * Singleton Instance
     */
    public static LocationManager instance;

    private LocationManager() {
	locations = new HashSet<>();
    }

    /**
     *
     * @return The instance of this object.
     */
    public static LocationManager getInstance() {
	if (instance == null) {
	    instance = new LocationManager();
	}
	return instance;
    }

    public static void setInstance(LocationManager newInstance) {
	instance = newInstance;
    }
    private HashSet<String> locations;

    /**
     *
     * @param location The location to check
     * @return True if location exists
     */
    public static boolean isValidLocation(String location) {
	return getInstance().locations.contains(location);
    }

    /**
     * Adds a new location code
     *
     * @param newLocation The new location code to add.
     */
    public static void addLocation(String newLocation) {
	getInstance().locations.add(newLocation);
	FileManager.saveLocationManager();
    }

    /**
     * Removes a location code
     *
     * @param oldLocation The location code to remove.
     */
    public static void removeLocation(String oldLocation) {
	getInstance().locations.remove(oldLocation);
	FileManager.saveLocationManager();
    }

    /**
     * Moves an item to a new location.
     *
     * @param itemID The items ID.
     * @param newLocation The location to move the item to.
     */
    public static void moveItem(String itemID, String newLocation) {
	ItemManager.getItemFromID(itemID).setLocation(newLocation);
    }
}
