package grp.twentytwo.equipmentmanager;

import grp.twentytwo.equipmentmanagerapplication.Speaker;
import java.util.ArrayList;

/**
 *
 * @author fmw5088
 */
public class ModelManager {
    // <editor-fold desc="Speakers">

    public Speaker loginSuccessful = new Speaker();
    public Speaker<Item> itemPreview = new Speaker<>();
    public Speaker<User> userPreview = new Speaker<>();
    // </editor-fold>
    UserManager userManager;
    ItemManager itemManager;
    BookingManager bookingManager;
    LocationManager locationManager;
    MaintenanceManager maintenanceManager;

    public ModelManager() {
	userManager = new UserManager();
	itemManager = new ItemManager();
	bookingManager = new BookingManager(itemManager);
	locationManager = new LocationManager(itemManager);
	maintenanceManager = new MaintenanceManager(itemManager);
	itemManager.setLocationManager(locationManager);
    }

    public void login() {
	loginSuccessful.notifyListeners(null);
    }
    // Item Functions ----------------------------------------------------------

    public void getItemPreview(String itemID) {
	Item itemData = new Item(itemID, itemID + "'s Name", "TJ06A01", "Test/Item/Useless");
	itemPreview.notifyListeners(itemData);
    }

    public ArrayList<String> searchForItem(String searchQuery) {
	ArrayList<String> test = new ArrayList<>();
	for (int i = 0; i < 10; i++) {
	    test.add(searchQuery.substring(0, 2) + "0" + i);
	}
	return test;
    }

    public void getItemBookings(String itemID) {
	// get a list of all bookings for the item.
    }

    // User Functions ----------------------------------------------------------
    public void getUserPreview(String userID) {
	User userData = new Manager(userID, userID + "'s Name");
	userPreview.notifyListeners(userData);
    }

    public ArrayList<String> searchForUser(String searchQuery) {
	ArrayList<String> test = new ArrayList<>();
	for (int i = 0; i < 10; i++) {
	    test.add(searchQuery.substring(0, 2) + "0" + i);
	}
	return test;
    }
}
