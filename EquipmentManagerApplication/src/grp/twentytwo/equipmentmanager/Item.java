package grp.twentytwo.equipmentmanager;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores item data.
 *
 * @author fmw5088
 */
public class Item {

    private final String id;
    private String name;
    private String description;
    private String location;
    private Status status;
    private String type;

    //private ArrayList<History> history;
    private boolean needsCalibration;
    private LocalDateTime lastCalibration;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    /**
     *
     * Package private constructor for saved items
     *
     * @param id
     * @param name
     * @param description
     * @param location
     * @param status
     * @param type
     * @param needsCalibration
     * @param lastCalibration
     */
    Item(String id, String name, String description, String location, Status status, String type, boolean needsCalibration, LocalDateTime lastCalibration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.status = status;
        this.type = type;
        this.needsCalibration = needsCalibration;
        this.lastCalibration = lastCalibration;
    }

    public Item(String name, String location, String type) { // Public contructor for no id (unofficial) item
        this(null, name, "", location, Status.WORKING, type, false, null);
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return formatter;
    }

    /**
     *
     * @return Item ID
     */
    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    void setLocation(String newLocation) {
        String oldLocation = location;
        location = newLocation;
        //addHistory("Moved from " + oldLocation + " to " + newLocation + ".");
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Flags an item as needing calibration
     */
    void flagForCalibration() {
        needsCalibration = true;
        //addHistory("Needs calibration.");
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
        lastCalibration = LocalDateTime.now();
        //addHistory("Calibrated.");
    }

    /**
     *
     * @return LastCalibration as a ZonedDateTime object
     */
    public LocalDateTime getLastCalibration() {
        return lastCalibration;
    }

    /**
     *
     * @return LastCalibration as a String object
     */
    public String getLastCalibrationAsString() {
        return lastCalibration.format(formatter);
    }

    /**
     *
     * @return LastCalibration as a formatted String (States uncalibrated if
     * null)
     */
    public String getLastCalibrationFormatted() {
        return ((lastCalibration == null) ? "Uncalibrated" : lastCalibration.format(formatter));
    }

//    /**
//     *
//     * @param description
//     */
//    void addHistory(String description) {
//        History entry = new History(description);
//        history.add(entry);
//    }
//    /**
//     *
//     * @return An array copy of the items history.
//     */
//    public History[] getHistory() {
//        return history.toArray(History[]::new);
//    }
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
                + ", Last Calibration: " + ((lastCalibration == null) ? "Uncalibrated" : lastCalibration.format(formatter));
        //+ ", History: " + history;
    }

    /**
     * Indicates the status of an item.
     */
    public static enum Status {
        WORKING,
        DECOMMISIONED,
        FAULTY
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

}
