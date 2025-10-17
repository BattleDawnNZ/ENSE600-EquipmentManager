package grp.twentytwo.equipmentmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private final ArrayList<Column> columns;

    private Column column_primaryKey = new Column("LocationID", "VARCHAR(12) not NULL", "");
    private Column column_locationName = new Column("Name", "VARCHAR(30)", "");

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
        LocationManager lm = new LocationManager(dbManager);
        lm.printTable();
        Location loc = new Location("000001", "Workshop1");

        lm.addLocation("WORKSHOP3");
        lm.removeLocation(loc);
        //System.out.println(lm.getLocationFromID("000001"));
        lm.printTable();
        //System.out.println(lm.getLocationFromID("000004"));
        System.out.println(lm.isValidLocationName("WORKSHOP3"));
    }

    public LocationManager(DatabaseManager databaseManager) {

        this.dbManager = databaseManager;

        // Define Table Parameters
        columns = new ArrayList<Column>();
        columns.add(column_primaryKey);
        columns.add(column_locationName);
        tableManager = new TableManager(dbManager, tableName, columns, column_primaryKey);

        // Add test data to table
        dbManager.updateDB("INSERT INTO LOCATIONTABLE VALUES ('000001', 'Workshop1')");
    }

    /**
     *
     * @param locationID
     * @return a Location object (if the id string exists) else, null
     */
    public Location getLocationFromID(String locationID) {
        ResultSet rs = tableManager.getRowByPrimaryKey(locationID);
        return getLocationObjectFromResultSet(rs);
    }

    /**
     *
     * @param locationID
     * @return true if the item exists
     */
    public boolean isValidLocationID(String locationID) {
        ResultSet rs = tableManager.getRowByPrimaryKey(locationID);
        return (getLocationObjectFromResultSet(rs) != null);
    }

    /**
     *
     * @param locationID
     * @return true if the item exists
     */
    public boolean isValidLocationName(String locationName) {
        ResultSet rs;
        try {
            rs = tableManager.getRowByColumnValue("Name", locationName);
            return (getLocationObjectFromResultSet(rs) != null);
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(LocationManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean addLocation(String name) {
        try {
            if (!tableManager.getRowByColumnValue("Name", name).next()) { // Ensure Location is new
                column_primaryKey.data = tableManager.getNextPrimaryKeyId();
                column_locationName.data = name.toUpperCase();
                tableManager.createRow(columns);
                return true; // Location created
            }
        } catch (SQLException ex) {
            Logger.getLogger(LocationManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidColumnNameException e) {
            System.out.println("Error: Invalid Column Name!");
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
