package grp.twentytwo.equipmentmanager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores booking data.
 *
 * @author fmw5088
 */
public class Booking {

    private final String id;

    private String userID;
    private String itemID;
    private LocalDateTime bookedDate;
    private LocalDateTime returnDate;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    /**
     * Creates a new booking with the specified details. Package private - only
     * for official bookings with an assigned ID
     *
     * @param id the bookings ID
     * @param userID the ID of the user who booked
     * @param bookedDate the date of the booking
     * @param returnDate the return date of the booking
     */
    Booking(String id, String userID, String itemID, LocalDateTime bookedDate, LocalDateTime returnDate) {
        this.id = id;
        this.userID = userID;
        this.itemID = itemID;
        this.bookedDate = bookedDate;
        this.returnDate = returnDate;
    }

    /**
     * Public booking constructor (no ID will be assigned)
     *
     * @param userID
     * @param itemID
     * @param bookedDate
     * @param returnDate
     */
    public Booking(String userID, String itemID, LocalDateTime bookedDate, LocalDateTime returnDate) {
        this(null, userID, itemID, bookedDate, returnDate);
    }

    /**
     *
     * @return The bookings ID
     */
    public String getID() {
        return id;
    }

    /**
     *
     * @return The ID of the user who booked.
     */
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     *
     * @param userID The User ID in Question
     * @return True if the user owns the booking.
     */
    public boolean isOwnedBy(String userID) {
        return this.userID.equals(userID);
    }

    /**
     *
     * @return The ID of the item being booked.
     */
    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    /**
     *
     * @return The date of the booking
     */
    public LocalDateTime getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(LocalDateTime bookedDate) {
        this.bookedDate = bookedDate;
    }

    /**
     *
     * @return The return date of the booking.
     */
    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public boolean overlaps(Booking other) {
        return !(returnDate.isBefore(other.bookedDate) || bookedDate.isAfter(other.returnDate));
    }

    /**
     *
     * @param date
     * @return true if a given date is outside the booked date range (inclusive
     * of booking start/end days)
     */
    public boolean isOutsideBookedDate(LocalDate date) {
        return !(date.isAfter(this.bookedDate.toLocalDate()) && date.isBefore(this.returnDate.toLocalDate()));
    }

    /**
     *
     * @param dateTime
     * @return true if a given date AND time is outside the booked time and date
     * range
     */
    public boolean isOutsideBookedDateAndTime(LocalDateTime dateTime) {
        return (dateTime.isBefore(this.bookedDate) || dateTime.isEqual(this.bookedDate) || dateTime.isEqual(this.returnDate) || dateTime.isAfter(this.returnDate));
    }

    @Override
    public String toString() {
        return "Booking ID: " + id + ", User ID: " + userID + ", Item ID: " + itemID + ", Booked Date: " + bookedDate.format(formatter) + ", Return Date: " + returnDate.format(formatter) + ".";
    }

}
