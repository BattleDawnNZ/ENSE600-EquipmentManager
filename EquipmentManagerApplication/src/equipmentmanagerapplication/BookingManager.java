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
public class BookingManager implements Serializable, Saveable {

    private ItemManager itemManager;
    /**
     * The name for the save file.
     */
    private final String fileName = "booking_manager.bin";
    /**
     * Contains All the Bookings. Keyed by Booking ID.
     */
    private HashMap<String, Booking> bookings;
    private int currentID;

    public BookingManager(ItemManager itemManager) {
	this.itemManager = itemManager;
	bookings = new HashMap<>();
	currentID = 0;
    }

    /**
     * Loads the item manager from a file.
     */
    public void load() {
	BookingManager bm = (BookingManager) FileManager.loadFile(fileName);
	if (bm != null) {
	    bookings = bm.bookings;
	    currentID = bm.currentID;
	}
    }

    /**
     * Saves the item manager to a file
     */
    public void save() {
	FileManager.saveFile(this, fileName);
    }

    /**
     * Creates a new booking.
     *
     * @param userID The booking users ID.
     * @param bookedDate The date of the booking.
     * @param returnDate The return date of the booking.
     */
    public boolean issueItem(String userID, String itemID, ZonedDateTime bookedDate, ZonedDateTime returnDate) {
	String bookingID = generateBookingID();
	Booking booking = new Booking(bookingID, userID, itemID, bookedDate, returnDate);
	for (Booking other : getBookingsForItem(itemID)) {
	    if (booking.overlaps(other)) {
		return false;
	    }
	}
	// Should never occur.
	if (bookings.put(bookingID, booking) != null) {
	    System.out.println("ERROR! BookingID: " + bookingID + " was already in use.");
	}
	itemManager.getItemFromID(itemID).addHistory("(Booking ID: " + bookingID + ") Booked by " + userID + ", from " + bookedDate + " to " + returnDate);
	save();
	itemManager.save();
	return true;
    }

    /**
     * Remove a booking.
     *
     * @param bookingID The booking ID fo the booking to remove.
     * @return True if the booking was successfully completed. i.e. item and
     * booking both actually existed.
     */
    public boolean returnItem(String bookingID) {
	Booking booking = bookings.remove(bookingID);
	if (booking == null) {
	    return false;
	}
	Item bookedItem = itemManager.getItemFromID(booking.getItemID());
	if (bookedItem == null) {
	    return false;
	}
	bookedItem.addHistory("(Booking ID: " + bookingID + ") Returned by " + booking.getUserID());
	save();
	itemManager.save();
	return true;
    }

    /**
     *
     * @param bookingID The bookings ID
     * @param userID The User to check ownership
     * @return true if the booking exists and is owned by the User.
     */
    public boolean verifyBookingOwner(String bookingID, String userID) {
	Booking booking = getBooking(bookingID);
	if (booking != null && booking.isOwnedBy(userID)) {
	    return true;
	}
	return false;
    }

    /**
     *
     * @param bookingID The booking ID of the booking requested.
     * @return the booking with the corresponding booking ID.
     */
    public Booking getBooking(String bookingID) {
	return bookings.get(bookingID);
    }

    /**
     *
     * @return An ArrayList of the bookings.
     */
    public ArrayList<Booking> getBookings() {
	return (ArrayList<Booking>) bookings.values();
    }

    /**
     *
     * @param itemID
     * @return All bookings for the item.
     */
    public ArrayList<Booking> getBookingsForItem(String itemID) {
	ArrayList<Booking> validBookings = new ArrayList<>();
	bookings.forEach((k, v) -> {
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
    public ArrayList<Booking> getBookingsForUser(String userID) {
	ArrayList<Booking> validBookings = new ArrayList<>();
	bookings.forEach((k, v) -> {
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
    private String generateBookingID() {
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
