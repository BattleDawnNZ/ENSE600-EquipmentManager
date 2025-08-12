package equipmentmanagerapplication;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Manages issuing and returning of items.
 *
 * @author fmw5088
 */
public class BookingManager implements Serializable {

    /**
     * Singleton Instance
     */
    private static BookingManager instance;

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

    public static void setInstance(BookingManager newInstance) {
	instance = newInstance;
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
    public static boolean issueItem(String userID, String itemID, ZonedDateTime bookedDate, ZonedDateTime returnDate) {
	String bookingID = generateBookingID();
	Booking booking = new Booking(bookingID, userID, itemID, bookedDate, returnDate);
	for (Booking other : getBookingsForItem(itemID)) {
	    if (booking.overlaps(other)) {
		return false;
	    }
	}
	// Should never occur.
	if (getInstance().bookings.put(bookingID, booking) != null) {
	    System.out.println("ERROR! BookingID: " + bookingID + " was already in use.");
	}
	ItemManager.getItemFromID(itemID).addHistory("(Booking ID: " + bookingID + ") Booked by " + userID + ", from " + bookedDate + " to " + returnDate);
	FileManager.saveBookingManager();
	FileManager.saveItemManager();
	return true;
    }

    /**
     * Remove a booking.
     *
     * @param bookingID The booking ID fo the booking to remove.
     */
    public static void returnItem(String bookingID) {
	Booking booking = getInstance().bookings.remove(bookingID);
	ItemManager.getItemFromID(booking.getItemID()).addHistory("(Booking ID: " + bookingID + ") Returned by " + booking.getUserID());
	FileManager.saveBookingManager();
	FileManager.saveItemManager();
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
    public static ArrayList<Booking> getBookings() {
	return (ArrayList<Booking>) getInstance().bookings.values();
    }

    /**
     *
     * @param itemID
     * @return All bookings for the item.
     */
    public static ArrayList<Booking> getBookingsForItem(String itemID) {
	ArrayList<Booking> validBookings = new ArrayList<>();
	getInstance().bookings.forEach((k, v) -> {
	    if (v.getID().equals(itemID)) {
		validBookings.add(v);
	    }
	});
	return validBookings;
    }

    /**
     *
     * @param userID
     * @return All bookings for the user.
     */
    public static ArrayList<Booking> getBookingsForUser(String userID) {
	ArrayList<Booking> validBookings = new ArrayList<>();
	getInstance().bookings.forEach((k, v) -> {
	    if (v.getUserID().equals(userID)) {
		validBookings.add(v);
	    }
	});
	return validBookings;
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
