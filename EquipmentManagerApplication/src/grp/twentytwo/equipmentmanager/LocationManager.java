package grp.twentytwo.equipmentmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages all locations, adding locations, moving items, and validating
 * locations.
 *
 * @author ppj1707
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
        //Location loc = new Location("000001", "Workshop1");
        System.out.println(lm.isValidLocationName("Workshop "));

        //lm.addLocation("WORKSHOP3");
        //lm.removeLocation(loc);
        //System.out.println(lm.getLocationFromID("000001"));
        //System.out.println(lm.searchLocationsByName("W"));
        lm.printTable();
        //System.out.println(lm.getLocationFromName("WORKSHOP 12").getId()); //System.out.println(lm.getLocationFromID("000004"));
        //System.out.println(lm.isValidLocationName("WORKSHOP3"));
    }

    public LocationManager(DatabaseManager databaseManager) {

        this.dbManager = databaseManager;

        // Define Table Parameters
        columns = new ArrayList<Column>();
        columns.add(column_primaryKey);
        columns.add(column_locationName);
        tableManager = new TableManager(dbManager, tableName, columns, column_primaryKey);
    }

    /**
     *
     * @param locationID
     * @return a Location object (if the id string exists) else, null
     */
    public Location getLocationFromID(String locationID) {
        try {
            ResultSet rs = tableManager.getRowByPrimaryKey(locationID);
            return getLocationObjectsFromResultSet(rs).getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("InvalidID");
            return null;
        }
    }

    /**
     *
     * @param locationID
     * @return true if the item exists
     */
    public boolean isValidLocationID(String locationID) {
        ResultSet rs = tableManager.getRowByPrimaryKey(locationID);
        return (getLocationObjectsFromResultSet(rs) != null);
    }

    /**
     *
     * @param locationName
     * @return true if the item exists
     */
    public Location getLocationFromName(String locationName) {
        ResultSet rs;
        try {
            rs = tableManager.getRowByColumnValue("Name", locationName);
            return getLocationObjectsFromResultSet(rs).getFirst();
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(LocationManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (NoSuchElementException e) {
            System.out.println("Invalid Name");
            return null;
        }
    }

    /**
     *
     * @param locationName
     * @return true if the item exists
     */
    public boolean isValidLocationName(String locationName) {
        ResultSet rs;
        try {
            rs = tableManager.getRowByColumnValue("Name", locationName);
            if (getLocationObjectsFromResultSet(rs).size() > 0) {
                return true;
            }
            return false;
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(LocationManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean addLocation(String name) {
        try {
            if (!isValidLocationName(name)) { // Ensure Location is new
                column_primaryKey.data = tableManager.getNextPrimaryKeyId();
                column_locationName.data = name.toUpperCase();
                tableManager.createRow(columns);
                return true; // Location created
            }
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
    public boolean removeLocation(Location locationID) {
        return tableManager.deleteRowByPrimaryKey(locationID.getId());
    }

    /**
     * Returns an ArrayList of locations that partial match the partial name (if
     * blank all locations will be returned)
     *
     * @param locationName The desired locations partial name.
     * @return A list of location IDs that match.
     */
    public ArrayList<String> searchLocationsByName(String locationName) {
        ArrayList<String> validLocations = new ArrayList<>();
        ResultSet rs;
        try {
            rs = tableManager.searchColumn("Name", locationName);
            while (rs.next()) {
                validLocations.add(rs.getString("Name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return validLocations;
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
    private ArrayList<Location> getLocationObjectsFromResultSet(ResultSet resultSet) {
        try {
            ArrayList<Location> locationObjects = new ArrayList<>();
            while (resultSet.next()) { // Location exists
                String locationID = resultSet.getString("LocationID");
                String name = resultSet.getString("Name");
                locationObjects.add(new Location(locationID, name));
            }
            return locationObjects;
        } catch (SQLException ex) {
            Logger.getLogger(LocationManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
