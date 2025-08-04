package equipmentmanagerapplication;

import java.io.Serializable;
import java.time.ZonedDateTime;

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
     * The date of the booking.
     */
    private ZonedDateTime bookedDate;
    /**
     * The return date of the booking.
     */
    private ZonedDateTime returnDate;

    /**
     * Creates a new booking with the specified details.
     *
     * @param id the bookings ID
     * @param userID the ID of the user who booked
     * @param bookedDate the date of the booking
     * @param returnDate the return date of the booking
     */
    public Booking(String id, String userID, ZonedDateTime bookedDate, ZonedDateTime returnDate) {
	this.id = id;
	this.userID = userID;
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

    @Override
    public String toString() {
	return "ID: " + id + ", UserID: " + userID + ", Booked Date: " + bookedDate + ", Return Date: " + returnDate + ".";
    }
}
