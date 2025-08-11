package equipmentmanagerapplication;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fmw5088
 */
public class ItemManager implements Serializable {

    /**
     * Singleton Instance
     */
    private static ItemManager instance;

    private ItemManager() {
	items = new HashMap<>();
	currentID = 0;
    }

    /**
     *
     * @return The instance of this object.
     */
    public static ItemManager getInstance() {
	if (instance == null) {
	    instance = new ItemManager();
	}
	return instance;
    }
    private HashMap<String, Item> items;
    private int currentID;

    /**
     * Adds a new item to the items HashMap.
     *
     * @param name The items name.
     * @param location The items location
     * @param type The items type.
     */
    public static void addItem(String name, String location, String type) {
	String itemID = generateItemID(type);
	Item item = new Item(itemID, name, location, type);
	getInstance().items.put(itemID, item);
	FileManager.saveItemManager();
    }

    /**
     *
     * @return A unique item ID.
     */
    public static String generateItemID(String type) {
	String newID;
	do {
	    newID = "";
	    for (String str : type.toUpperCase().split("/")) {
		newID += str.toCharArray()[0];
	    }
	    newID += String.format("%06d", getInstance().currentID++);
	} while (getInstance().items.containsKey(newID));
	return newID;
    }

    /**
     * Removes an item from the items HashMap.
     *
     * @param itemID The ID of the item to be removed.
     */
    public static void removeItem(String itemID) {
	getInstance().items.remove(itemID);
	FileManager.saveItemManager();
    }

    /**
     * Removes an item from the items HashMap.
     *
     * @param item The item to be removed.
     */
    public static void removeItem(Item item) {
	removeItem(item.getID());
    }

    /**
     * Returns an item from the items HashMap.
     *
     * @param itemID The desired items ID.
     * @return The desired item.
     */
    public static Item getItemFromID(String itemID) {
	return getInstance().items.get(itemID);
    }

    /**
     *
     * @param itemID
     * @return True if ID is valid.
     */
    public static boolean verifyID(String itemID) {
	return getInstance().items.containsKey(itemID);
    }

    public String toString() {
	String output = "----- ItemManager -----\n";
	output += "Current ID: " + currentID + "\n";
	output += "Items:\n";
	for (Map.Entry<String, Item> entry : items.entrySet()) {
	    output += " - " + entry.getValue().toString() + "\n";
	}
	return output;
    }
}
