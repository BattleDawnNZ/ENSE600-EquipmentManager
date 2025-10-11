package grp.twentytwo.equipmentmanager;

import grp.twentytwo.equipmentmanager.User.SecurityLevels;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Manages all users allowing for login, logout, create user, remove user,
 * verifying IDs, and tracking the current user.
 *
 * @author ppj1707
 */
public class UserManager { // Implement importable!!

    private final DatabaseManager dbManager;
    private final Connection conn;
    private User activeUser; // Stores the active user object

    // Prepared Statements
    PreparedStatement st_getRowById;
    PreparedStatement st_deleteRowById;
    PreparedStatement st_updateRowById;
    PreparedStatement st_createRowById;

    public static void main(String[] args) {
        UserManager um = new UserManager();
        um.printUserTableFromDB();
        User user = new Manager("000004", "Alice");
        um.removeUser("000004");
        um.printUserTableFromDB();
        System.out.println(um.getUserFromID("000004"));
    }

    public UserManager() {
        dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
        conn = dbManager.getConnection();
        System.out.println(conn);

        // Initialise Tables ()
        createUserTableIfNotExist();

        // Initialise Statements
        try {
            st_getRowById = conn.prepareStatement("SELECT * FROM USERTABLE where UserID = ?");
            st_deleteRowById = conn.prepareStatement("DELETE FROM USERTABLE where UserID = ?");
            st_createRowById = conn.prepareStatement("INSERT INTO USERTABLE (UserID, Name, SecurityLevel) VALUES (?, ?, ?)");
            st_updateRowById = conn.prepareStatement("UPDATE USERTABLE SET Name = ?, SecurityLevel = ? WHERE UserID = ?");
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    public void printUserTableFromDB() {
        try {
            System.out.println("User Table Data:");
            ResultSet rs = dbManager.queryDB("SELECT * FROM USERTABLE");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("UserID") + "      Name: " + rs.getString("Name") + "      SecurityLevel" + rs.getString("SecurityLevel"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

    }

    /**
     *
     * @param id
     * @return whether the user ID is registered in the system
     */
    public boolean verifyID(String id) {
        try {
            st_getRowById.setString(1, id);
            ResultSet rs = st_getRowById.executeQuery();
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
        try {
            st_getRowById.setString(1, userID);
            ResultSet rs = st_getRowById.executeQuery();
            if (rs.next()) { // User exists
                String name = rs.getString("Name");
                String sL = rs.getString("SecurityLevel");
                return importUser(userID, name, SecurityLevels.valueOf(sL));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            return null;
        }
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
     * @return a User object (if the id string exists) else, null
     */
    public boolean saveUser(User user) {
        String id = user.getUserID();
        try {
            if (verifyID(id)) { // Update the table
                st_updateRowById.setString(1, user.getName());
                st_updateRowById.setString(2, user.getSecurityLevel().toString());
                st_updateRowById.setString(3, id);
                st_updateRowById.executeUpdate();
            } else { // Create new entry
                st_createRowById.setString(1, id);
                st_createRowById.setString(2, user.getName());
                st_createRowById.setString(3, user.getSecurityLevel().toString());
                st_createRowById.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    public void createUserTableIfNotExist() {
        if (!dbManager.checkTableExists("USERTABLE")) {
            // Create table. Assign user id as primary key to prevent duplicates
            System.out.println("USER table does not exist. Creating table");
            dbManager.updateDB("CREATE TABLE USERTABLE (UserID VARCHAR(12) not NULL, Name VARCHAR(30), SecurityLevel VARCHAR(15), PRIMARY KEY (UserID))");
            // Add test data to table
            dbManager.updateDB("INSERT INTO USERTABLE VALUES ('000001', 'Bob', 'MANAGER'), "
                    + "('000002', 'Sally', 'EMPLOYEE'), "
                    + "('000003', 'Fred', 'GUEST')");
        }
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
        try {
            st_deleteRowById.setString(1, userID);
            st_deleteRowById.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e);
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

}
