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
	broken,
	decommissioned,
	faulty
    }
    private Status status;
    private String type;

    /**
     * Represents an event in an items history.
     */
    public static class History {

	/**
	 *
	 * @return The time stamp for when this event happened.
	 */
	private ZonedDateTime timeStamp;

	/**
	 *
	 * @return The description of the event.
	 */
	private String description;

	/**
	 *
	 * @param timeStamp When this event happened.
	 * @param description What happened.
	 */
	public History(ZonedDateTime timeStamp, String description) {
	    this.timeStamp = timeStamp;
	    this.description = description;
	}

	/**
	 *
	 * @return The time stamp for when this event happened.
	 */
	public ZonedDateTime getTimeStamp() {
	    return timeStamp;
	}

	/**
	 *
	 * @return The description of the event.
	 */
	public String getDescription() {
	    return description;
	}
    }
    private ArrayList<History> history;
    private ZonedDateTime lastCalibration;

    public Item(String id, String name, String description, String location, Status status, String type, ZonedDateTime lastCalibration) {
	this.id = id;
	this.name = name;
	this.description = description;
	this.location = location;
	this.status = status;
	this.type = type;
	this.history = new ArrayList<History>();
	this.lastCalibration = lastCalibration;
    }

    /**
     *
     * @return An array copy of the items history.
     */
    public History[] getHistory() {
	return history.toArray(new History[0]);
    }

    /**
     * Checks whether the item has a particular type.
     *
     * i.e. electrical/measurement hasType electrical
     *
     * @param partType the partial type string to check
     * @return true if the partType is contained in the type
     */
    public boolean hasType(String partType) {
	return type.contains(partType);
    }
}
