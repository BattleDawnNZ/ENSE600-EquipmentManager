package equipmentmanagerapplication;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages issuing and returning of items.
 *
 * @author fmw5088
 */
public class BookingManager implements Serializable {

    /**
     * Contains All the Bookings. Keyed by Booking ID.
     */
    private static HashMap<String, Booking> bookings;
    private static int currentID = 0;

    /**
     * Creates a new booking.
     *
     * @param userID The booking users ID.
     * @param bookedDate The date of the booking.
     * @param returnDate The return date of the booking.
     */
    public static void issueItem(String userID, String itemID, ZonedDateTime bookedDate, ZonedDateTime returnDate) {
	String bookingID = generateBookingID();
	Booking booking = new Booking(bookingID, userID, itemID, bookedDate, returnDate);
	if (bookings.put(bookingID, booking) != null) {
	    System.out.println("ERROR! BookingID: " + bookingID + " was already in use.");
	}
    }

    /**
     * Remove a booking.
     *
     * @param bookingID The booking ID fo the booking to remove.
     */
    public static void returnItem(String bookingID) {
	bookings.remove(bookingID);
    }

    /**
     *
     * @param bookingID The booking ID of the booking requested.
     * @return the booking with the corresponding booking ID.
     */
    public static Booking getBooking(String bookingID) {
	return bookings.get(bookingID);
    }

    /**
     *
     * @return An array copy of the bookings.
     */
    public static Booking[] getBookings() {
	return bookings.values().toArray(new Booking[0]);
    }

    /**
     *
     * @return A unique booking ID.
     */
    private static String generateBookingID() {
	while (bookings.containsKey(Integer.toString(currentID))) {
	    currentID++;
	}
	return Integer.toString(currentID++);
    }

    @Override
    public String toString() {
	String output = "----- BookingManager -----\n";
	output += "Current ID: " + currentID + "\n";
	output += "Bookings:\n";
	for (Map.Entry<String, Booking> entry : bookings.entrySet()) {
	    output += " - " + entry.getValue().toString() + "\n";
	}
	return output;
    }
}
