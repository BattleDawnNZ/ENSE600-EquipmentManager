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
     * Singleton Instance
     */
    public static BookingManager instance;

    private BookingManager() {
	bookings = new HashMap<>();
	currentID = 0;
    }

    /**
     *
     * @return The instance of this object.
     */
    public static BookingManager getInstance() {
	if (instance == null) {
	    instance = new BookingManager();
	}
	return instance;
    }

    /**
     * Contains All the Bookings. Keyed by Booking ID.
     */
    private HashMap<String, Booking> bookings;
    private int currentID;

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
	if (getInstance().bookings.put(bookingID, booking) != null) {
	    System.out.println("ERROR! BookingID: " + bookingID + " was already in use.");
	}
	FileManager.saveBookingManager();
    }

    /**
     * Remove a booking.
     *
     * @param bookingID The booking ID fo the booking to remove.
     */
    public static void returnItem(String bookingID) {
	getInstance().bookings.remove(bookingID);
	FileManager.saveBookingManager();
    }

    /**
     *
     * @param bookingID The booking ID of the booking requested.
     * @return the booking with the corresponding booking ID.
     */
    public static Booking getBooking(String bookingID) {
	return getInstance().bookings.get(bookingID);
    }

    /**
     *
     * @return An array copy of the bookings.
     */
    public static Booking[] getBookings() {
	return getInstance().bookings.values().toArray(new Booking[0]);
    }

    /**
     *
     * @return A unique booking ID.
     */
    private static String generateBookingID() {
	while (getInstance().bookings.containsKey(Integer.toString(getInstance().currentID))) {
	    getInstance().currentID++;
	}
	return Integer.toString(getInstance().currentID++);
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
