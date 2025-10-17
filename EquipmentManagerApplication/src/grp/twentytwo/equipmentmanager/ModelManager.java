package grp.twentytwo.equipmentmanager;

import grp.twentytwo.equipmentmanagerapplication.Speaker;
import java.util.ArrayList;

/**
 *
 * @author fmw5088
 */
public class ModelManager {

    public ModelManager() {
    }

    public boolean login() {
	return true;
    }
    // Item Functions ----------------------------------------------------------

    public Item getItem(String itemID) {
	return new Item(itemID + "'s Name", "TJ06A01", "Test/Item/Useless");
    }

    public ArrayList<String> searchForItems(String searchQuery) {
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
    public User getUser(String userID) {
	return new Manager(userID, userID + "'s Name");
    }

    public ArrayList<String> searchForUsers(String searchQuery) {
	ArrayList<String> test = new ArrayList<>();
	for (int i = 0; i < 10; i++) {
	    test.add(searchQuery.substring(0, 2) + "0" + i);
	}
	return test;
    }
}
