package grp.twentytwo.equipmentmanager;

import grp.twentytwo.database.DatabaseConnectionException;
import grp.twentytwo.database.DatabaseManager;
import grp.twentytwo.guiapplication.Speaker;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fmw5088
 */
public class ModelManager {

    private User activeUser = null;

    public Speaker<Exception> modelError;
    public Speaker<String> modelInvalidEntry;

    DatabaseManager databaseManager;
    LocationManager locationManager;
    ItemManager itemManager;
    UserManager userManager;
    BookingManager bookingManager;
    HistoryManager historyManager;

    public ModelManager() {
	modelError = new Speaker<>();
	modelInvalidEntry = new Speaker<>();
    }

    public void setupManagers() {
	try {
	    databaseManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
	    locationManager = new LocationManager(databaseManager);
	    itemManager = new ItemManager(databaseManager);
	    userManager = new UserManager(databaseManager);
	    bookingManager = new BookingManager(databaseManager);
	    historyManager = new HistoryManager(databaseManager);
	} catch (DatabaseConnectionException err) {
	    modelError.notifyListeners(err);
	    System.out.println(err.getMessage());
	    System.exit(0);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public boolean login(User user) {
	boolean success = false;
	try {
	    success = userManager.login(user);
	    if (success) {
		activeUser = getUser(user.getID());
	    }
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return success;
    }

    public User getActiveUser() {
	return activeUser;
    }

    public void logout() {
	activeUser = null;
    }

    // Item Functions ----------------------------------------------------------
    public Item getNewItem() {
	return new Item("", "", "");
    }

    public void addItem(Item item) {
	try {
	    if (locationManager.isValidLocationName(item.getLocation())) {
		itemManager.addItem(item);
		historyManager.addHistory(new History(item.getID(), "Created by User: " + activeUser.getID()));
	    }
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void removeItem(String itemID) {
	try {
	    itemManager.removeItem(itemID);
	    historyManager.addHistory(new History(itemID, "Item " + itemID + " removed by User: " + activeUser.getID()));
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public Item getItem(String itemID) {
	Item item = null;
	try {
	    item = itemManager.getItemFromID(itemID);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return item;
    }

    public void updateItem(Item item) {
	try {
	    if (locationManager.isValidLocationName(item.getLocation())) {
		itemManager.updateItem(item);
		historyManager.addHistory(new History(item.getID(), "Details Updated by User: " + activeUser.getID()));
	    }
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public LinkedHashSet<String> searchForItems(String searchQuery) {
	LinkedHashSet<String> items = null;
	try {
	    items = itemManager.searchForItems(searchQuery);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return items;
    }

    public ArrayList<String> getItemsForLocation(String location) {
	return itemManager.getItemsForLocation(location);
    }

    public Booking getNewBooking() {
	try {
	    return new Booking(activeUser.getID(), "", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return null;
    }

    public boolean addBooking(Booking booking) {
	boolean success = false;
	try {
	    // Todo Add feedback to user on failed issuing of item. Fix Overlap Function to allow back to back bookings.
	    if (itemManager.verifyID(booking.getItemID())) {
		success = bookingManager.issueItem(booking);
		if (success) {
		    historyManager.addHistory(new History(booking.getItemID(), "Item Booked by User: " + activeUser.getID() + ", Booking ID: " + booking.getID()));
		}
	    }
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return success;
    }

    public void returnItemBooking(String bookingID) {
	try {
	    if (bookingManager.getBookingFromID(bookingID).getUserID().equals(activeUser.getID())) {
		bookingManager.returnItem(bookingID);
	    } // Todo throw error to alert user that it's not their booking
	    else {
		modelInvalidEntry.notifyListeners("This booking is not booked by the current user.");
	    }
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public Booking getBooking(String bookingID) {
	Booking booking = null;
	try {
	    booking = bookingManager.getBookingFromID(bookingID);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return booking;
    }

    public List<Booking> getBookingsForItem(String itemID) {
	ArrayList<Booking> bookings = null;
	try {
	    bookings = bookingManager.getBookingsForItem(itemID);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return bookings;
    }

    public void removeBookingsForItem(String itemID) {
	// Todo Implement item booking deletion
    }

    public void addNote(String itemID, String note) {
	try {
	    Item item = itemManager.getItemFromID(itemID);
	    itemManager.updateItem(item);
	    historyManager.addHistory(new History(itemID, "Note: " + note + ", Added by User: " + activeUser.getID()));
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void flagItemForCalibration(String itemID) {
	try {
	    Item item = itemManager.getItemFromID(itemID);
	    item.flagForCalibration();
	    itemManager.updateItem(item);
	    historyManager.addHistory(new History(itemID, "Flagged for Calibration by User: " + activeUser.getID()));
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void calibrateItem(String itemID) {
	try {
	    Item item = itemManager.getItemFromID(itemID);
	    item.calibrate();
	    itemManager.updateItem(item);
	    historyManager.addHistory(new History(itemID, "Calibrated by User: " + activeUser.getID()));
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public ArrayList<History> getHistoryForItem(String itemID) {
	return historyManager.getHistoryForItem(itemID);
    }

    // User Functions ----------------------------------------------------------
    public User getNewUser() {
	return new Manager("", "");
    }

    public void addUser(User user) {
	try {
	    userManager.addUser(user);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void updateUser(User user) {
	try {
	    userManager.updateUser(user);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public User getUser(String userID) {
	User user = null;
	try {
	    user = userManager.getUserFromID(userID);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return user;
    }

    public void removeUser(String userID) {
	try {
	    userManager.removeUser(userID);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public LinkedHashSet<String> searchForUsers(String searchQuery) {
	LinkedHashSet<String> users = new LinkedHashSet<>();
	try {
	    users = userManager.searchForUsers(searchQuery);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return users;
    }

    // Location Functions ------------------------------------------------------
    public void addLocation(String locationName) {
	try {
	    locationManager.addLocation(locationName);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void removeLocation(String locationID) {
	try {
	    if (!locationID.isBlank()) {
		if (itemManager.getItemsForLocation(locationManager.getLocationFromID(locationID).getName()).isEmpty()) {
		    locationManager.removeLocation(locationID);
		} else {
		    modelInvalidEntry.notifyListeners("Cannot remove a location that contains items.\nPlease move items first.");
		}
	    }// Todo throw custom exception to alert user to the fact that the location currently has items stored in it.
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public Location getLocation(String locationName) {
	Location location = null;
	try {
	    location = locationManager.getLocationFromName(locationName);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return location;
    }

    public ArrayList<String> searchForLocations(String searchQuery) {
	ArrayList<String> locations = new ArrayList<>();
	try {
	    locations = locationManager.searchLocationsByName(searchQuery);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return locations;
    }
}
