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

    /**
     * Singleton Instance
     */
    private static UserManager instance;

    private UserManager() {
        Manager root = new Manager("root"); // DEBUG!!!
        users = new HashMap<>(); // DEBUG!!!
        users.put("root", root); // DEBUG!!!
        Employee rootE = new Employee("rootE"); // DEBUG!!!
        users.put("rootE", rootE);
        Guest rootG = new Guest("rootG"); // DEBUG!!!
        users.put("rootG", rootG);
    }

    /**
     *
     * @return The instance of this object.
     */
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public static void setInstance(UserManager newInstance) {
        instance = newInstance; // Used by the file manager (as a new object instance is created when loading a file)
    }

    /**
     * Loads the user manager from a file.
     */
    @Override
    public void load() {
        instance = (UserManager) FileManager.loadFile(fileName);
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
    public static User getActiveUser() {
        return getInstance().activeUser;
    }

    /**
     *
     * @param id
     * @return whether the user ID is registered in the system
     */
    public static boolean verifyID(String id) {
        return getInstance().users.containsKey(id);
    }

    /**
     *
     * @param userID
     * @return a User object (if the id string exists) else, null
     */
    public static User getUserFromID(String userID) {
        if (getInstance().users.containsKey(userID)) {
            return getInstance().users.get(userID);
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
    public static boolean createUser(String userID, SecurityLevels level) {
        if (verifyID(userID)) {
            return false;
        } else {
            switch (level) { // Create new user based on their security level
                case MANAGER:
                    Manager newManager = new Manager(userID);
                    getInstance().users.put(userID, newManager);
                    break;
                case EMPLOYEE:
                    Employee newEmployee = new Employee(userID);
                    getInstance().users.put(userID, newEmployee);
                    break;
                case GUEST:
                    Guest newGuest = new Guest(userID);
                    getInstance().users.put(userID, newGuest);
                    break;
            }
            UserManager.getInstance().save();
            return true;
        }
    }

    /**
     *
     * @param userID
     * @return true if the user was removed successfully (or false if they
     * already do not exist)
     */
    public static boolean removeUser(String userID) {
        if (getInstance().users.remove(userID) != null) {
            UserManager.getInstance().save();
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
    public static boolean login(String userID) {
        if (getInstance().users.containsKey(userID)) {
            getInstance().activeUser = getInstance().users.get(userID); // Save the current user
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
    public static boolean logout() {
        getInstance().activeUser = null; // Save the current user
        return true;
    }
}
