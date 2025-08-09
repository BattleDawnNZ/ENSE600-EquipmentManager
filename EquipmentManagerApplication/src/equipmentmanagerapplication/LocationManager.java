package equipmentmanagerapplication;

import java.util.HashSet;

/**
 *
 * @author fmw5088
 */
public class LocationManager {

    private static HashSet<String> locations = new HashSet<>();

    /**
     *
     * @param location The location to check
     * @return True if location exists
     */
    public static boolean isValidLocation(String location) {
	return locations.contains(location);
    }

    /**
     * Adds a new location code
     *
     * @param newLocation The new location code to add.
     */
    public static void addLocation(String newLocation) {
	locations.add(newLocation);
    }

    public static void moveItem(String itemID, String newLocation) {
	ItemManager.getItemFromID(itemID).setLocation(newLocation);
    }
}
