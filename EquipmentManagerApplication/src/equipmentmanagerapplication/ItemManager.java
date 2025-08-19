package equipmentmanagerapplication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fmw5088
 */
public class ItemManager implements Serializable, Saveable {

    /**
     * The name for the save file.
     */
    private final String fileName = "item_manager.bin";

    /**
     * Contains all the items. Keyed by item ID.
     */
    private HashMap<String, Item> items;
    private int currentID;
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

    /**
     * Loads the item manager from a file.
     */
    public void load() {
	instance = (ItemManager) FileManager.loadFile(fileName);
    }

    /**
     * Saves the item manager to a file
     */
    public void save() {
	FileManager.saveFile(this, fileName);
    }

    /**
     * Adds a new item to the items HashMap.
     *
     * @param name The items name.
     * @param location The items location
     * @param type The items type.
     * @return The Item ID of the Item Added.
     */
    public static String addItem(String name, String location, String type) {
	if (!LocationManager.isValidLocation(location)) {
	    return null;
	}
	String itemID = generateItemID(type);
	Item item = new Item(itemID, name, location, type);
	getInstance().items.put(itemID, item);
	getInstance().save();
	return itemID;
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
    public static boolean removeItem(String itemID) {
	Item returned = getInstance().items.remove(itemID);
	getInstance().save();
	return returned != null;
    }

    /**
     * Removes an item from the items HashMap.
     *
     * @param item The item to be removed.
     */
    public static void removeItem(Item item) {
	removeItem(item.getId());
    }

    /**
     * Returns an item from the items HashMap. This should only be used if you
     * have the exact ID, otherwise use getItemsFromID.
     *
     * @param itemID The desired items ID.
     * @return The desired item.
     */
    public static Item getItemFromID(String itemID) {
	return getInstance().items.get(itemID);
    }

    /**
     * Returns an ArrayList of items that partial match the partID from the
     * items HashMap. (if blank all items will be returned)
     *
     * @param partID The desired items ID.
     * @return The desired item.
     */
    public static ArrayList<Item> getItemsFromID(String partID) {
	if (partID.isBlank()) {
	    return new ArrayList<>(getInstance().items.values());
	}
	ArrayList<Item> validItems = new ArrayList<>();
	getInstance().items.forEach((k, v) -> {
	    if (v.hasInID(partID)) {
		validItems.add(v);
	    }
	});
	return validItems;
    }

    /**
     * Returns an ArrayList of items that partial match the partName from the
     * items HashMap. (if blank all items will be returned)
     *
     * @param partName The desired items name.
     * @return The desired item.
     */
    public static ArrayList<Item> getItemsFromName(String partName) {
	if (partName.isBlank()) {
	    return new ArrayList<>(getInstance().items.values());
	}
	ArrayList<Item> validItems = new ArrayList<>();
	getInstance().items.forEach((k, v) -> {
	    if (v.hasInName(partName)) {
		validItems.add(v);
	    }
	});
	return validItems;
    }

    /**
     * Returns an ArrayList of items that partial match the partType from the
     * items HashMap. (if blank all items will be returned)
     *
     * @param partType The desired items type.
     * @return The desired item.
     */
    public static ArrayList<Item> getItemsFromType(String partType) {
	if (partType.isBlank()) {
	    return new ArrayList<>(getInstance().items.values());
	}
	ArrayList<Item> validItems = new ArrayList<>();
	getInstance().items.forEach((k, v) -> {
	    if (v.hasInType(partType)) {
		validItems.add(v);
	    }
	});
	return validItems;
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
