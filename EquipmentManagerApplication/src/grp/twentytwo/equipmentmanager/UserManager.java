package grp.twentytwo.equipmentmanager;

import grp.twentytwo.database.DatabaseManager;
import grp.twentytwo.database.Column;
import grp.twentytwo.database.DatabaseConnectionException;
import grp.twentytwo.database.TableManager;
import grp.twentytwo.database.InvalidColumnNameException;
import grp.twentytwo.database.NullColumnValueException;
import grp.twentytwo.database.PrimaryKeyClashException;
import grp.twentytwo.database.UnfoundPrimaryKeyException;
import grp.twentytwo.equipmentmanager.User.SecurityLevels;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages all users allowing for login, logout, create user, remove user,
 * verifying IDs, and tracking the current user.
 *
 * @author ppj1707
 */
class UserManager {

    private final DatabaseManager dbManager;
    private final TableManager tableManager;

    // Database table properties
    private final String tableName = "USERTABLE";
    private final ArrayList<Column> columnData;

    private Column column_userID = new Column("UserID", "VARCHAR(12) not NULL", "");
    private Column column_name = new Column("Name", "VARCHAR(30)", "");
    private Column column_securityLevel = new Column("SecurityLevel", "VARCHAR(15)", "");
    private Column column_password = new Column("Password", "VARCHAR(15)", "");

    UserManager(DatabaseManager databaseManager) throws DatabaseConnectionException {

        this.dbManager = databaseManager;
        // Define Table Parameters
        columnData = new ArrayList<Column>();
        columnData.add(column_userID);
        columnData.add(column_name);
        columnData.add(column_securityLevel);
        columnData.add(column_password);

        // Initialise Table
        tableManager = new TableManager(dbManager, tableName, columnData, column_userID);
    }

    /**
     *
     * @param userID
     * @return a User object (if the id string exists) else, null
     */
    User getUserFromID(String userID) throws UnfoundPrimaryKeyException {
        ResultSet rs = tableManager.getRowByPrimaryKey(userID);
        return getUserObjectFromResultSet(rs);
    }

    /**
     *
     * @param userID
     * @param level
     * @return a user object from specified input
     */
    User importUser(String userID, String name, SecurityLevels level) {
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
    boolean addUser(User user) throws PrimaryKeyClashException, NullColumnValueException {

        if (!tableManager.verifyPrimaryKey(user.getID())) { // Ensure user is new
            column_userID.data = user.getID();
            column_name.data = user.getName();
            column_securityLevel.data = user.getSecurityLevel().toString();
            column_password.data = user.getPassword();

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
    boolean updateUser(User user) throws UnfoundPrimaryKeyException {

        String id = user.getID();

        if (tableManager.verifyPrimaryKey(id)) { // Update the table
            column_userID.data = id;
            column_name.data = user.getName();
            column_securityLevel.data = user.getSecurityLevel().toString();
            try {
                ResultSet rs = tableManager.getRowByPrimaryKey(id);
                if (rs.next()) {
                    column_password.data = rs.getString("Password");
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }

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
     * @param userID
     * @return true if the user was removed successfully (or false if they
     * already do not exist)
     */
    boolean removeUser(String userID) throws UnfoundPrimaryKeyException {
        return tableManager.deleteRowByPrimaryKey(userID);
    }

    /**
     * Returns an ArrayList of users that partial match the partial userID from
     * the (if blank all items will be returned)
     *
     * @param userID The desired users partial ID.
     * @return A list of item IDs that match.
     */
    ArrayList<String> searchUsersByID(String userID) {
        if (userID.isBlank()) {
            return tableManager.getAllPrimaryKeys();
        }
        ArrayList<String> validItems = new ArrayList<>();
        ResultSet rs;
        try {
            rs = tableManager.searchColumn("UserID", userID);
            while (rs.next()) {
                validItems.add(rs.getString("UserID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return validItems;
    }

    /**
     * Returns an ArrayList of users that partial match the partial user Name
     * (if blank all users will be returned)
     *
     * @param userName The desired users partial name.
     * @return A list of user IDs that match.
     */
    ArrayList<String> searchUsersByName(String userName) {
        if (userName.isBlank()) {
            return tableManager.getAllPrimaryKeys();
        }
        ArrayList<String> validItems = new ArrayList<>();
        ResultSet rs;
        try {
            rs = tableManager.searchColumn("Name", userName);
            while (rs.next()) {
                validItems.add(rs.getString("UserID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return validItems;
    }

    /**
     * Returns an ArrayList of users that partial match the partial security
     * level (if blank all users will be returned)
     *
     * @param securityLevel The desired users partial security level.
     * @return A list of item IDs that match.
     */
    ArrayList<String> searchUsersBySecurityLevel(String securityLevel) {
        try {
            if (securityLevel.isBlank()) {
                return tableManager.getAllPrimaryKeys();
            }
            ArrayList<String> validItems = new ArrayList<>();
            ResultSet rs;

            rs = tableManager.searchColumn("SecurityLevel", securityLevel);

            while (rs.next()) {
                validItems.add(rs.getString("UserID"));
            }
            return validItems;
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param searchString
     * @return an ArrayList of users that partial match the partial
     * securityLevel, name, or id (if blank all items will be returned)
     */
    LinkedHashSet<String> searchForUsers(String searchString) {

        LinkedHashSet<String> validItems = new LinkedHashSet<>(); // Hashset forces duplicates to be removed. Preserves order for priority

        validItems.addAll(searchUsersByID(searchString));
        validItems.addAll(searchUsersByName(searchString));
        validItems.addAll(searchUsersBySecurityLevel(searchString));
        return validItems;
    }

    /**
     * Login a user
     *
     * @param user
     * @return whether the id and password are valid (false if the user id was
     * not found in the system or password was incorrect for the user)
     */
    boolean login(User user) throws UnfoundPrimaryKeyException {
        try {
            ResultSet rs = tableManager.getRowByPrimaryKey(user.getID());
            if (rs.next()) {
                if (rs.getString("Password").equals(user.getPassword())) {
                    return true;
                }
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Print the entire user table to the console
     */
    void printTable() {
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
