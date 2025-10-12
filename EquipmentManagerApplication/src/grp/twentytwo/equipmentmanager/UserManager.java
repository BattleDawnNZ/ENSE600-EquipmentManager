package grp.twentytwo.equipmentmanager;

import grp.twentytwo.equipmentmanager.User.SecurityLevels;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages all users allowing for login, logout, create user, remove user,
 * verifying IDs, and tracking the current user.
 *
 * @author ppj1707
 */
public class UserManager {

    private final DatabaseManager dbManager;
    private final TableManager tableManager;

    private User activeUser; // Stores the active user object

    String tableName = "USERTABLE";
    HashMap<String, String> columns;
    String primaryKey = "UserID";

    public static void main(String[] args) {
        UserManager um = new UserManager();
        um.printTable();
        User user = new Manager("000004", "Alice");
        //um.removeUser("000004");
        //um.saveUser(user);
        um.printTable();
        System.out.println(um.getUserFromID("000004"));
    }

    public UserManager() {
        dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");

        // Define Table Parameters
        columns = new HashMap<String, String>();
        columns.put("UserID", "VARCHAR(12) not NULL");
        columns.put("Name", "VARCHAR(30)");
        columns.put("SecurityLevel", "VARCHAR(15)");

        // Initialise Table
        tableManager = new TableManager(dbManager, tableName, columns, primaryKey);
    }

    /**
     *
     * @param id
     * @return whether the user ID is registered in the system
     */
    public boolean verifyID(String id) {
        try {
            ResultSet rs = tableManager.getRowByPrimaryKey(id);
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    /**
     *
     * @param userID
     * @return a User object (if the id string exists) else, null
     */
    public User getUserFromID(String userID) {
        ResultSet rs = tableManager.getRowByPrimaryKey(userID);
        return getUserObjectFromResultSet(rs);
    }

    /**
     *
     * @param userID
     * @param level
     * @return a user object from specified input
     */
    private User importUser(String userID, String name, SecurityLevels level) {
        switch (level) { // Create new user based on their security level
            case MANAGER:
                Manager manager = new Manager(userID, name);
                return manager;
            case EMPLOYEE:
                Employee employee = new Employee(userID, name);
                return employee;
            case GUEST:
                Guest guest = new Guest(userID, name);
                return guest;
            default:
                return null;
        }
    }

    /**
     *
     * @param user
     * @return true if created (did not previously exist)
     */
    public boolean createUser(User user) {
        String id = user.getUserID();

        if (!verifyID(id)) { // Ensure user is new
            HashMap<String, String> data = new HashMap<>();
            data.put("UserID", id);
            data.put("Name", user.getName());
            data.put("SecurityLevel", user.getSecurityLevel().toString());
            tableManager.createRow(data);
            return true; // User created
        }
        return false; // User already exists

    }

    /**
     *
     * @param user
     * @return true if updated (User previously existed)
     */
    public boolean updateUser(User user) {

        String id = user.getUserID();

        if (verifyID(id)) { // Update the table
            HashMap<String, String> data = new HashMap<>();
            data.put("UserID", id);
            data.put("Name", user.getName());
            data.put("SecurityLevel", user.getSecurityLevel().toString());
            tableManager.updateRowByPrimaryKey(data);
            return true;
        }
        return false;

    }

    /**
     *
     * @return the User object for the active user
     */
    public User getActiveUser() {
        return activeUser;
    }

//    /**
//     *
//     * @param userID
//     * @param level
//     * @return true if the user was successfully created (or false if they
//     * already exist)
//     */
//    public boolean createUser(String userID, String name, SecurityLevels level) {
//        if (verifyID(userID)) {
//            return false;
//        } else {
//            switch (level) { // Create new user based on their security level
//                case MANAGER:
//                    Manager newManager = new Manager(userID, name);
//                    users.put(userID, newManager);
//                    break;
//                case EMPLOYEE:
//                    Employee newEmployee = new Employee(userID, name);
//                    users.put(userID, newEmployee);
//                    break;
//                case GUEST:
//                    Guest newGuest = new Guest(userID, name);
//                    users.put(userID, newGuest);
//                    break;
//            }
//            return true;
//        }
//    }
    /**
     *
     * @param userID
     * @return true if the user was removed successfully (or false if they
     * already do not exist)
     */
    public boolean removeUser(String userID) {
        return tableManager.deleteRowByPrimaryKey(userID);
    }

    /**
     * Login a user
     *
     * @param userID
     * @return whether the login was successful (false if the user id was not
     * found in the system)
     */
    public boolean login(String userID) {
        if (verifyID(userID)) {
            activeUser = getUserFromID(userID); // Save the current user
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

    public void printTable() {
        tableManager.printTable();
    }

    private User getUserObjectFromResultSet(ResultSet rs) {
        try {
            if (rs.next()) { // User exists
                String userID = rs.getString("UserID");
                String name = rs.getString("Name");
                String sL = rs.getString("SecurityLevel");
                return importUser(userID, name, SecurityLevels.valueOf(sL));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

}
