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
