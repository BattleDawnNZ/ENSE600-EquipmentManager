package grp.twentytwo.equipmentmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages all locations, adding locations, moving items, and validating
 * locations.
 *
 * @author fmw5088
 */
public class LocationManager {

    private final DatabaseManager dbManager;
    private final TableManager tableManager;

    String tableName = "LOCATIONTABLE";
    HashMap<String, String> columnDefinitions;
    String primaryKey = "LocationID";

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
        LocationManager lm = new LocationManager(dbManager);
        lm.printTable();
        Location loc = new Location("1", "Workshop1");

        lm.addLocation("Workshop3");
        //lm.removeLocation(loc);
        //System.out.println(lm.getLocationFromID("000001"));
        lm.printTable();
        //System.out.println(lm.getLocationFromID("000004"));
    }

    public LocationManager(DatabaseManager databaseManager) {

        this.dbManager = databaseManager;

        // Define Table Parameters
        columnDefinitions = new HashMap<String, String>();
        columnDefinitions.put("LocationID", "VARCHAR(12) not NULL");
        columnDefinitions.put("Name", "VARCHAR(30)");
        tableManager = new TableManager(dbManager, tableName, columnDefinitions, primaryKey);

        // Add test data to table
        dbManager.updateDB("INSERT INTO LOCATIONTABLE VALUES ('000001', 'Workshop1')");
    }

    /**
     *
     * @param userID
     * @return a User object (if the id string exists) else, null
     */
    public Location getLocationFromID(String locationID) {
        ResultSet rs = tableManager.getRowByPrimaryKey(locationID);
        return getLocationObjectFromResultSet(rs);
    }

    public boolean addLocation(String name) {

        try {
            if (!tableManager.getRowByColumnValue("Name", name).next()) { // Ensure Location is new
                HashMap<String, String> data = new HashMap<>();
                data.put("LocationID", tableManager.getNextPrimaryKeyId());
                data.put("Name", name);
                tableManager.createRow(data);
                return true; // Location created
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false; // Location already exists

    }

    /**
     *
     * @param locationID
     * @return true if the location was removed successfully (or false if it
     * already does not exist)
     */
    public boolean removeLocation(Location location) {
        return tableManager.deleteRowByPrimaryKey(location.getId());
    }

    /**
     * Print the entire location table to the console
     */
    public void printTable() {
        tableManager.printTable();
    }

    /**
     *
     * @param resultSet
     * @return a user object
     */
    private Location getLocationObjectFromResultSet(ResultSet resultSet) {
        try {
            if (resultSet.next()) { // User exists
                String locationID = resultSet.getString("LocationID");
                String name = resultSet.getString("Name");
                return (new Location(locationID, name));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
