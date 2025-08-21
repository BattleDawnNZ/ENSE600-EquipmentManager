package grp.twentytwo.equipmentmanager;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores booking data.
 *
 * @author fmw5088
 */
public class Booking implements Serializable {

    /**
     * The bookings ID.
     */
    private String id;
    /**
     * The ID of the user that booked.
     */
    private String userID;
    /**
     * The ID of the piece of equipment being used.
     */
    private String itemID;
    /**
     * The date of the booking.
     */
    private ZonedDateTime bookedDate;
    /**
     * The return date of the booking.
     */
    private ZonedDateTime returnDate;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    /**
     * Creates a new booking with the specified details.
     *
     * @param id the bookings ID
     * @param userID the ID of the user who booked
     * @param bookedDate the date of the booking
     * @param returnDate the return date of the booking
     */
    Booking(String id, String userID, String itemID, ZonedDateTime bookedDate, ZonedDateTime returnDate) {
	this.id = id;
	this.userID = userID;
	this.itemID = itemID;
	this.bookedDate = bookedDate;
	this.returnDate = returnDate;
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

    /**
     *
     * @return The date of the booking
     */
    public ZonedDateTime getBookedDate() {
	return bookedDate;
    }

    /**
     *
     * @return The return date of the booking.
     */
    public ZonedDateTime getReturnDate() {
	return returnDate;
    }

    public boolean overlaps(Booking other) {
	return !(returnDate.isBefore(other.bookedDate) || bookedDate.isAfter(other.returnDate));
    }

    @Override
    public String toString() {
	return "Booking ID: " + id + ", User ID: " + userID + ", Item ID: " + itemID + ", Booked Date: " + bookedDate.format(formatter) + ", Return Date: " + returnDate.format(formatter) + ".";
    }
}
