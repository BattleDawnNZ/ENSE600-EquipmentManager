package grp.twentytwo.equipmentmanager;

import grp.twentytwo.equipmentmanagerapplication.Speaker;
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

    public ModelManager() {
	modelError = new Speaker<>();
    }

    public void setupManagers() {
	try {
	    databaseManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
	    locationManager = new LocationManager(databaseManager);
	    itemManager = new ItemManager(databaseManager, locationManager);
	    userManager = new UserManager(databaseManager);
	    bookingManager = new BookingManager(itemManager, databaseManager);
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
	itemManager.addItem(item);
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

    public ArrayList<String> searchForUsers(String searchQuery) {
	ArrayList<String> users = new ArrayList<>();
	try {
	    // Todo add proper search fro users
	    users.add("000001");
	    users.add("000002");
	    users.add("000003");
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return users;
    }

    // Location Functions ------------------------------------------------------
    public void AddLocation(String locationName) {
	locationManager.addLocation(locationName);
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
