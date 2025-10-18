package grp.twentytwo.equipmentmanager;

import grp.twentytwo.equipmentmanager.User.SecurityLevels;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    // Database table properties
    String tableName = "USERTABLE";
    ArrayList<Column> columnData;

    private Column column_userID = new Column("UserID", "VARCHAR(12) not NULL", "");
    private Column column_name = new Column("Name", "VARCHAR(30)", "");
    private Column column_securityLevel = new Column("SecurityLevel", "VARCHAR(15)", "");

    private User activeUser; // Stores the active user object

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
        UserManager um = new UserManager(dbManager);
        um.printTable();
        //User user = new Manager("000004", "Alice");
        //um.removeUser("000004");
        //um.saveUser(user);
        //um.printTable();
        //System.out.println(um.getUserFromID("000004"));
    }

    public UserManager(DatabaseManager databaseManager) {

        this.dbManager = databaseManager;
        // Define Table Parameters
        columnData = new ArrayList<Column>();
        columnData.add(column_userID);
        columnData.add(column_name);
        columnData.add(column_securityLevel);

        // Initialise Table
        tableManager = new TableManager(dbManager, tableName, columnData, column_userID);
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
     * @param userID
     * @param name
     * @param level
     * @return true if created (did not previously exist)
     */
    public boolean addUser(String userID, String name, SecurityLevels level) {

        if (!tableManager.verifyPrimaryKey(userID)) { // Ensure user is new
            column_userID.data = userID;
            column_name.data = name;
            column_securityLevel.data = level.toString();

            try {
                tableManager.createRow(columnData);
                return true; // User created
            } catch (InvalidColumnNameException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
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

        if (tableManager.verifyPrimaryKey(id)) { // Update the table
            column_userID.data = id;
            column_name.data = user.getName();
            column_securityLevel.data = user.getSecurityLevel().toString();

            try {
                tableManager.updateRowByPrimaryKey(columnData);
                return true;
            } catch (InvalidColumnNameException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

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
        if (tableManager.verifyPrimaryKey(userID)) {
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

    /**
     * Print the entire user table to the console
     */
    public void printTable() {
        tableManager.printTable();
    }

    /**
     *
     * @param resultSet
     * @return a user object
     */
    private User getUserObjectFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) { // User exists
                String userID = resultSet.getString("UserID");
                String name = resultSet.getString("Name");
                String sL = resultSet.getString("SecurityLevel");
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
