package equipmentmanagerapplication;

import java.io.Serializable;
import java.time.ZonedDateTime;
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

    /**
     * Indicates the status of an item.
     */
    public static enum Status {
	working,
	decommissioned,
	faulty
    }
    private Status status;
    private String type;

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
	public History(String description) {
	    this.timestamp = ZonedDateTime.now();
	    this.description = description;
	}

	/**
	 *
	 * @param timestamp When this event happened.
	 * @param description What happened.
	 */
	public History(ZonedDateTime timestamp, String description) {
	    this.timestamp = timestamp;
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

	public String toString() {
	    return "" + timestamp + " - " + description;
	}
    }
    private ArrayList<History> history;
    private boolean needsCalibration;
    private ZonedDateTime lastCalibration;

    /**
     *
     * @param id
     * @param name
     * @param location
     * @param type
     */
    public Item(String id, String name, String location, String type) {
	this.id = id;
	this.name = name;
	this.description = "";
	setLocation(location);
	this.status = Status.working;
	this.type = type;
	this.history = new ArrayList<>();
	addHistory("Item created.");
	this.needsCalibration = false;
	this.lastCalibration = null;
    }

    /**
     *
     * @return Item ID
     */
    public String getID() {
	return id;
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
    public final void setLocation(String newLocation) {
	if (LocationManager.isValidLocation(newLocation)) {
	    addHistory("Moved from " + location + " to " + newLocation + ".");
	    location = newLocation;
	}
    }

    /**
     *
     * @param description
     */
    public final void addHistory(String description) {
	History entry = new History(description);
	history.add(entry);
    }

    /**
     *
     * @return An array copy of the items history.
     */
    public History[] getHistory() {
	return history.toArray(new History[0]);
    }

    /**
     * Flags an item as needing calibration
     */
    public void flagForCalibration() {
	needsCalibration = true;
	addHistory("Needs calibration.");
    }

    /**
     * Updates the items last calibration date.
     */
    public void calibrate() {
	needsCalibration = false;
	lastCalibration = ZonedDateTime.now();
	addHistory("Calibrated.");
    }

    /**
     * @param partID the partial ID string to check
     * @return An ArrayList of all items where partID is contained in the id
     */
    public boolean hasInID(String partID) {
	return id.contains(partID);
    }

    /**
     * @param partName the partial name string to check
     * @return An ArrayList of all items where partName is contained in the name
     */
    public boolean hasInName(String partName) {
	return name.contains(partName);
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
	return type.contains(partType);
    }

    public String toString() {
	return "ID: " + id
		+ ", Name: " + name
		+ ", Description: " + description
		+ ", Location: " + location
		+ ", Status: " + status
		+ ", Type: " + type
		+ ", Last Calibration: " + lastCalibration
		+ ", History: " + history;
    }
}
