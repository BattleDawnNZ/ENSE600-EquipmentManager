package equipmentmanagerapplication;

import java.util.HashMap; // Import the HashSet class

/**
 *
 * @author ppj1707
 */
public class UserManager {

    private static HashMap<String, User> users; // BAD Practice to have type as abstract class????????????? OR OKAY??
    private static User activeUser;

    public UserManager() {
        Manager root = new Manager("root"); // DEBUG!!!
        users = new HashMap<>(); // DEBUG!!!
        users.put("root", root); // DEBUG!!!
        Employee rootE = new Employee("rootE"); // DEBUG!!!
        users.put("rootE", rootE);
        Guest rootG = new Guest("rootG"); // DEBUG!!!
        users.put("rootG", rootG);
    }

    public static User getActiveUser() {
        return activeUser;
    }

    public String User

    getActiveUser() {
        return activeUser.id;
    }

    public static User getUserFromID(String userID) {
        if (users.containsKey(userID)) {
            return users.get(userID);
        } else {
            return null;
        }
    }

    // Return True if user created successfully
    public static boolean createUser(String userID) {

        //users.add(e);
        return false;
    }

    public static boolean login(String userID) {
        if (users.containsKey(userID)) {
            activeUser = users.get(userID); // Save the current user
            return true;
        } else {
            return false;
        }
    }

    public static boolean logout() {
        activeUser = null; // Save the current user
        return true;
    }

}
