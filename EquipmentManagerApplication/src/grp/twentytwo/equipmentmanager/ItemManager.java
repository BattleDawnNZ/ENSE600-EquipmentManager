package grp.twentytwo.equipmentmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fmw5088
 */
public class ItemManager implements Saveable {

    private LocationManager locationManager;
    /**
     * The name for the save file.
     */
    private final String fileName = "item_manager.bin";

    /**
     * Contains all the items. Keyed by item ID.
     */
    private HashMap<String, Item> items;
    private int currentID;

    public ItemManager() {
	items = new HashMap<>();
	currentID = 0;
    }

    public void setLocationManager(LocationManager locationManager) {
	this.locationManager = locationManager;
    }

    /**
     * Loads the item manager from a file.
     */
    @Override
    public void load() {
	ItemManager im = (ItemManager) FileManager.loadFile(fileName);
	if (im != null) {
	    items = im.items;
	    currentID = im.currentID;
	}
    }

    /**
     * Saves the item manager to a file
     */
    @Override
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
    public String addItem(String name, String location, String type) {
	if (!locationManager.isValidLocation(location)) {
	    return null;
	}
	String itemID = generateItemID(type);
	Item item = new Item(itemID, name, location, type);
	items.put(itemID, item);
	save();
	return itemID;
    }

    /**
     *
     * @param type the items type.
     * @return A unique item ID.
     */
    public String generateItemID(String type) {
	String newID;
	do {
	    newID = "";
	    for (String str : type.toUpperCase().split("/")) {
		newID += str.toCharArray()[0];
	    }
	    newID += String.format("%06d", currentID++);
	} while (items.containsKey(newID));
	return newID;
    }

    /**
     * Removes an item from the items HashMap.
     *
     * @param itemID The ID of the item to be removed.
     * @return true if the item existed.
     */
    public boolean removeItem(String itemID) {
	Item returned = items.remove(itemID);
	save();
	return returned != null;
    }

    /**
     * Removes an item from the items HashMap.
     *
     * @param item The item to be removed.
     * @return true if the item existed.
     */
    public boolean removeItem(Item item) {
	return removeItem(item.getId());
    }

    /**
     * Returns an item from the items HashMap. This should only be used if you
     * have the exact ID, otherwise use getItemsFromID.
     *
     * @param itemID The desired items ID.
     * @return The desired item.
     */
    public Item getItemFromID(String itemID) {
	return items.get(itemID);
    }

    /**
     * Returns an ArrayList of items that partial match the partID from the
     * items HashMap. (if blank all items will be returned)
     *
     * @param partID The desired items ID.
     * @return The desired item.
     */
    public ArrayList<Item> getItemsFromID(String partID) {
	if (partID.isBlank()) {
	    return new ArrayList<>(items.values());
	}
	ArrayList<Item> validItems = new ArrayList<>();
	items.forEach((k, v) -> {
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
    public ArrayList<Item> getItemsFromName(String partName) {
	if (partName.isBlank()) {
	    return new ArrayList<>(items.values());
	}
	ArrayList<Item> validItems = new ArrayList<>();
	items.forEach((k, v) -> {
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
    public ArrayList<Item> getItemsFromType(String partType) {
	if (partType.isBlank()) {
	    return new ArrayList<>(items.values());
	}
	ArrayList<Item> validItems = new ArrayList<>();
	items.forEach((k, v) -> {
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
    public boolean verifyID(String itemID) {
	return items.containsKey(itemID);
    }

    /**
     *
     * @return a string representing the object.
     */
    @Override
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
