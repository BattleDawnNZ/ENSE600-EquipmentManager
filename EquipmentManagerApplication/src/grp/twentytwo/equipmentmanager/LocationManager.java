package grp.twentytwo.equipmentmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author fmw5088
 */
public class LocationManager implements Serializable, Saveable {

    private ItemManager itemManager;
    /**
     * The name for the save file.
     */
    private final String fileName = "location_manager.bin";
    /**
     * Contains all the locations.
     */
    private HashSet<String> locations;

    public LocationManager(ItemManager itemManager) {
	this.itemManager = itemManager;
	locations = new HashSet<>();
    }

    /**
     * Loads the location manager from a file.
     */
    @Override
    public void load() {
	LocationManager lm = (LocationManager) FileManager.loadFile(fileName);
	if (lm != null) {
	    locations = lm.locations;
	}
    }

    /**
     * Saves the location manager to a file
     */
    @Override
    public void save() {
	FileManager.saveFile(this, fileName);
    }

    /**
     *
     * @param location The location to check
     * @return True if location exists
     */
    public boolean isValidLocation(String location) {
	return locations.contains(location.toUpperCase());
    }

    /**
     * Adds a new location code
     *
     * @param newLocation The new location code to add.
     * @return True if new location did not already exist.
     */
    public boolean addLocation(String newLocation) {
	boolean result = locations.add(newLocation.toUpperCase());
	save();
	return result;
    }

    /**
     * Removes a location code
     *
     * @param oldLocation The location code to remove.
     */
    public void removeLocation(String oldLocation) {
	locations.remove(oldLocation.toUpperCase());
	save();
    }

    /**
     * Moves an item to a new location.
     *
     * @param itemID The items ID.
     * @param newLocation The location to move the item to.
     */
    public void moveItem(String itemID, String newLocation) {
	itemManager.getItemFromID(itemID).setLocation(newLocation.toUpperCase());
	itemManager.save();
    }

    /**
     *
     * @return An ArrayList of all locations
     */
    public ArrayList<String> getLocations() {
	return new ArrayList<>(locations);
    }
}
