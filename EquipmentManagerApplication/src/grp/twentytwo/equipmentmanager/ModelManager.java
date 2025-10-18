package grp.twentytwo.equipmentmanager;

import grp.twentytwo.equipmentmanagerapplication.Speaker;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fmw5088
 */
public class ModelManager {

    public Speaker<Exception> modelError;

    DatabaseManager databaseManager;
    LocationManager locationManager;
    ItemManager itemManager;
    UserManager userManager;

    public ModelManager() {
	modelError = new Speaker<>();
    }

    public void SetupManagers() {
	try {
	    databaseManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
	    locationManager = new LocationManager(databaseManager);
	    itemManager = new ItemManager(databaseManager, locationManager);
	    userManager = new UserManager(databaseManager);
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    public boolean login() {
	boolean success = false;
	try {
	    success = true;
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return success;
    }
    // Item Functions ----------------------------------------------------------

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

    public void getItemBookings(String itemID) {
	try {
	    // get a list of all bookings for the item.
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
    }

    // User Functions ----------------------------------------------------------
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
	ArrayList<String> test = new ArrayList<>();
	try {
	    test.add(searchQuery);
	    for (int i = 0; i < 10; i++) {
		test.add(searchQuery.substring(0, 2) + "0" + i);
	    }
	} catch (Exception err) {
	    modelError.notifyListeners(err);
	    Logger.getLogger(ModelManager.class.getName()).log(Level.SEVERE, null, err);
	}
	return test;
    }
}
