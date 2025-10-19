package grp.twentytwo.equipmentmanager;

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

    DatabaseManager databaseManager;
    LocationManager locationManager;
    ItemManager itemManager;
    UserManager userManager;
    BookingManager bookingManager;
    HistoryManager historyManager;

    public ModelManager() {
	modelError = new Speaker<>();
    }

    public void setupManagers() {
	try {
	    databaseManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
	    locationManager = new LocationManager(databaseManager);
	    itemManager = new ItemManager(databaseManager);
	    userManager = new UserManager(databaseManager);
	    bookingManager = new BookingManager(databaseManager);
	    historyManager = new HistoryManager(databaseManager);
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

    public void logout() {
	activeUser = null;
    }

    // Item Functions ----------------------------------------------------------
    public Item getNewItem() {
	return new Item("", "", "");
    }

    public void AddItem(Item item) {
	try {
	    if (locationManager.isValidLocationName(item.getLocation())) {
		itemManager.addItem(item);
	    }
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void removeItem(String itemID) {
	try {
	    itemManager.removeItem(itemID);
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

    public boolean AddBooking(Booking booking) {
	boolean success = false;
	try {
	    // Todo Add feedback to user on failed issuing of item. Fix Overlap Function to allow back to back bookings.
	    if (itemManager.verifyID(booking.getItemID())) {
		success = bookingManager.issueItem(booking);
	    }
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return success;
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

    public void addNote(String itemID, String note) {
	try {
	    Item item = itemManager.getItemFromID(itemID);
	    itemManager.updateItem(item);
	    historyManager.addHistory(new History(itemID, "Note Added by User: " + activeUser.getID() + ", Note: " + note));
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

    public void RemoveUser(String userID) {
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
    public void AddLocation(String locationName) {
	try {
	    locationManager.addLocation(locationName);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public void removeLocation(String locationID) {
	try {
	    if (!locationID.isBlank() && itemManager.getItemsForLocation(locationManager.getLocationFromID(locationID).getName()).isEmpty()) {
		locationManager.removeLocation(locationID);
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
