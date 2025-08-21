package equipmentmanagerapplication;

import equipmentmanagerapplication.User.SecurityLevels;
import java.io.Serializable;
import java.util.HashMap; // Import the HashSet class

/**
 *
 * @author ppj1707
 */
public class UserManager implements Serializable, Saveable {

    private HashMap<String, User> users; // Store all users
    private User activeUser; // Stores the active user object
    private final String fileName = "user_manager.bin"; // The name for the save file.

    public UserManager() {
	Manager root = new Manager("111", "Bob"); // DEBUG!!!
	users = new HashMap<>(); // DEBUG!!!
	users.put("111", root); // DEBUG!!!
	Employee rootE = new Employee("222", "Fred"); // DEBUG!!!
	users.put("222", rootE);
	Guest rootG = new Guest("333", "Robert"); // DEBUG!!!
	users.put("333", rootG);
    }

    /**
     * Loads the user manager from a file.
     */
    @Override
    public void load() {
	UserManager um = (UserManager) FileManager.loadFile(fileName);
	if (um != null) {
	    this.users = um.users;
	}
    }

    /**
     * Saves the user manager to a file
     */
    @Override
    public void save() {
	FileManager.saveFile(this, fileName);
    }

    /**
     *
     * @return the User object for the active user
     */
    public User getActiveUser() {
	return activeUser;
    }

    /**
     *
     * @param id
     * @return whether the user ID is registered in the system
     */
    public boolean verifyID(String id) {
	return users.containsKey(id);
    }

    /**
     *
     * @param userID
     * @return a User object (if the id string exists) else, null
     */
    public User getUserFromID(String userID) {
	if (users.containsKey(userID)) {
	    return users.get(userID);
	} else {
	    return null;
	}
    }

    /**
     *
     * @param userID
     * @param level
     * @return true if the user was successfully created (or false if they
     * already exist)
     */
    public boolean createUser(String userID, String name, SecurityLevels level) {
	if (verifyID(userID)) {
	    return false;
	} else {
	    switch (level) { // Create new user based on their security level
		case MANAGER:
		    Manager newManager = new Manager(userID, name);
		    users.put(userID, newManager);
		    break;
		case EMPLOYEE:
		    Employee newEmployee = new Employee(userID, name);
		    users.put(userID, newEmployee);
		    break;
		case GUEST:
		    Guest newGuest = new Guest(userID, name);
		    users.put(userID, newGuest);
		    break;
	    }
	    save();
	    return true;
	}
    }

    /**
     *
     * @param userID
     * @return true if the user was removed successfully (or false if they
     * already do not exist)
     */
    public boolean removeUser(String userID) {
	if (users.remove(userID) != null) {
	    save();
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Login a user
     *
     * @param userID
     * @return whether the login was successful (false if the user id was not
     * found in the system)
     */
    public boolean login(String userID) {
	if (users.containsKey(userID)) {
	    activeUser = users.get(userID); // Save the current user
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Logout the current user
     *
     * @return if the logout was successful
     */
    public boolean logout() {
	activeUser = null; // Save the current user
	return true;
    }
}
