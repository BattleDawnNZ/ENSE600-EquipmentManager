package grp.twentytwo.equipmentmanager;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author ppj1707
 */
/**
 * Represents an event in an items history.
 */
class History {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private final String id; // Unique identifier for the history 
    private final String itemID; // Asociated item the history is for
    private final ZonedDateTime timestamp; // The time stamp for when this event happened.
    private final String description; // The description of the event.

    /**
     *
     * Package private constructor for official objects
     */
    History(String id, String itemID, ZonedDateTime timestamp, String description) {
        this.id = id;
        this.itemID = itemID;
        this.timestamp = timestamp;
        this.description = description;
    }

    /**
     * Public constructor for new (unsaved) history objects
     *
     * @param itemID
     * @param description
     */
    public History(String itemID, String description) {
        this(null, itemID, ZonedDateTime.now(), description);
    }

    public String getID() {
        return this.id;
    }

    /**
     *
     * @return The time stamp for when this event happened.
     */
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getItemID() {
        return this.itemID;
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
