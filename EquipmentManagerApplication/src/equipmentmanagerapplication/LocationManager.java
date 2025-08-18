package equipmentmanagerapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author fmw5088
 */
public class LocationManager implements Serializable {

    /**
     * The name for the save file.
     */
    private final String fileName = "location_manager.bin";
    /**
     * Contains all the locations.
     */
    private HashSet<String> locations;
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

    /**
     * Loads the location manager from a file.
     */
    public void load() {
	instance = (LocationManager) FileManager.loadFile(fileName);
    }

    /**
     * Saves the location manager to a file
     */
    public void save() {
	FileManager.saveFile(this, fileName);
    }

    /**
     *
     * @param location The location to check
     * @return True if location exists
     */
    public static boolean isValidLocation(String location) {
	return getInstance().locations.contains(location.toUpperCase());
    }

    /**
     * Adds a new location code
     *
     * @param newLocation The new location code to add.
     * @return True if new location did not already exist.
     */
    public static boolean addLocation(String newLocation) {
	boolean result = getInstance().locations.add(newLocation.toUpperCase());
	getInstance().save();
	return result;
    }

    /**
     * Removes a location code
     *
     * @param oldLocation The location code to remove.
     */
    public static void removeLocation(String oldLocation) {
	getInstance().locations.remove(oldLocation.toUpperCase());
	getInstance().save();
    }

    /**
     * Moves an item to a new location.
     *
     * @param itemID The items ID.
     * @param newLocation The location to move the item to.
     */
    public static void moveItem(String itemID, String newLocation) {
	ItemManager.getItemFromID(itemID).setLocation(newLocation.toUpperCase());
    }

    /**
     *
     * @return An ArrayList of all locations
     */
    public static ArrayList<String> getLocations() {
	return new ArrayList<>(getInstance().locations);
    }
}
