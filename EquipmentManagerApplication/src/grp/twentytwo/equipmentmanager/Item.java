package grp.twentytwo.equipmentmanager;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author fmw5088
 */
public class Item implements Serializable {

    private String id;
    private String name;
    private String description;
    private String location;
    private Status status;
    private String type;
    private ArrayList<History> history;
    private boolean needsCalibration;
    private ZonedDateTime lastCalibration;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    /**
     *
     * @param id
     * @param name
     * @param location
     * @param type
     */
    Item(String id, String name, String location, String type) {
	this.id = id;
	this.name = name;
	this.description = "";
	this.location = location;
	this.status = Status.WORKING;
	this.type = type;
	this.history = new ArrayList<>();
	addHistory("Item created at location " + location + ".");
	this.needsCalibration = false;
	this.lastCalibration = null;
    }

    /**
     *
     * @return Item ID
     */
    public String getId() {
	return id;
    }

    /**
     *
     * @return Item name
     */
    public String getName() {
	return name;
    }

    /**
     *
     * @return Item Description
     */
    public String getDescription() {
	return description;
    }

    /**
     *
     * @return Item location
     */
    public String getLocation() {
	return location;
    }

    /**
     *
     * @param newLocation
     */
    void setLocation(String newLocation) {
	String oldLocation = location;
	location = newLocation;
	addHistory("Moved from " + oldLocation + " to " + newLocation + ".");
    }

    /**
     *
     * @return Item Status
     */
    public Status getStatus() {
	return status;
    }

    /**
     *
     * @return Item Type
     */
    public String getType() {
	return type;
    }

    /**
     * Flags an item as needing calibration
     */
    void flagForCalibration() {
	needsCalibration = true;
	addHistory("Needs calibration.");
    }

    /**
     *
     * @return needsCalibration flag
     */
    public boolean getNeedsCalibration() {
	return needsCalibration;
    }

    /**
     * Updates the items last calibration date.
     */
    void calibrate() {
	needsCalibration = false;
	lastCalibration = ZonedDateTime.now();
	addHistory("Calibrated.");
    }

    /**
     *
     * @return LastCalibration as a ZonedDateTime object
     */
    public ZonedDateTime getLastCalibration() {
	return lastCalibration;
    }

    /**
     *
     * @param description
     */
    void addHistory(String description) {
	History entry = new History(description);
	history.add(entry);
    }

    /**
     *
     * @return An array copy of the items history.
     */
    public History[] getHistory() {
	return history.toArray(History[]::new);
    }

    /**
     * @param partID the partial ID string to check
     * @return An ArrayList of all items where partID is contained in the id
     */
    public boolean hasInID(String partID) {
	return id.toLowerCase().contains(partID.toLowerCase());
    }

    /**
     * @param partName the partial name string to check
     * @return An ArrayList of all items where partName is contained in the name
     */
    public boolean hasInName(String partName) {
	return name.toLowerCase().contains(partName.toLowerCase());
    }

    /**
     * Checks whether the item has a particular type.
     *
     * i.e. electrical/measurement hasType electrical
     *
     * @param partType the partial type string to check
     * @return An ArrayList of all items where partType is contained in the type
     */
    public boolean hasInType(String partType) {
	return type.toLowerCase().contains(partType.toLowerCase());
    }

    /**
     *
     * @return a string representing the object.
     */
    @Override
    public String toString() {
	return "Item ID: " + id
		+ ", Name: " + name
		+ ", Description: " + description
		+ ", Location: " + location
		+ ", Status: " + status
		+ ", Type: " + type
		+ ", Last Calibration: " + ((lastCalibration == null) ? "Uncalibrated" : lastCalibration.format(formatter))
		+ ", History: " + history;
    }

    /**
     * Indicates the status of an item.
     */
    public static enum Status {
	WORKING,
	DECOMMISIONED,
	FAULTY
    }

    /**
     * Represents an event in an items history.
     */
    public static class History implements Serializable {

	/**
	 *
	 * @return The time stamp for when this event happened.
	 */
	private ZonedDateTime timestamp;

	/**
	 *
	 * @return The description of the event.
	 */
	private String description;

	/**
	 *
	 * @param description What happened.
	 */
	History(String description) {
	    this.timestamp = ZonedDateTime.now();
	    this.description = description;
	}

	/**
	 *
	 * @return The time stamp for when this event happened.
	 */
	public ZonedDateTime getTimestamp() {
	    return timestamp;
	}

	/**
	 *
	 * @return The description of the event.
	 */
	public String getDescription() {
	    return description;
	}

	/**
	 *
	 * @return a string representing the object.
	 */
	@Override
	public String toString() {
	    return "" + timestamp.format(formatter) + " - " + description;
	}
    }
}
