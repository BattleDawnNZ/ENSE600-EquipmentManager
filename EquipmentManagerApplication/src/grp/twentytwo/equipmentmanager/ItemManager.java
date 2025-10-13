package grp.twentytwo.equipmentmanager;

import grp.twentytwo.equipmentmanager.Item.Status;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages item operations such as adding, removing, generating IDs, and
 * retrieving items from the HashMap.
 *
 * @author fmw5088
 */
public class ItemManager {

    private LocationManager locationManager;

    private final DatabaseManager dbManager;
    private final TableManager tableManager;

    // Database table properties
    String tableName = "ITEMTABLE";
    HashMap<String, String> columnDefinitions;
    String primaryKey = "ItemID";

    public ItemManager(DatabaseManager databaseManager) {
        this.dbManager = databaseManager;
        // Define Table Parameters
        columnDefinitions = new HashMap<String, String>();
        columnDefinitions.put("ItemID", "VARCHAR(12) not NULL");
        columnDefinitions.put("Name", "VARCHAR(30) not NULL");
        columnDefinitions.put("Description", "VARCHAR(200)");
        columnDefinitions.put("Location", "VARCHAR(12)");
        columnDefinitions.put("Status", "VARCHAR(14)");
        columnDefinitions.put("Type", "VARCHAR(40)");
        columnDefinitions.put("CalibrationFlag", "BIT");
        columnDefinitions.put("LastCalibration", "VARCHAR(20)");
        //DESCRIPTION MAX 200. HOW THROW THIS ERRPR?????

        // Initialise Table
        tableManager = new TableManager(dbManager, tableName, columnDefinitions, primaryKey);

        // Add test data to table
        dbManager.updateDB("INSERT INTO ITEMTABLE VALUES ('1', 'R9000 Universal Laser Cutter', 'Cuts mdf (1mm-12mm), arcrylic (1mm-10mm)', 'Workshop1', 'WORKING', 'Manufacturing/Cutting', '0', '03-03-2025 14:20')");

    }

    /**
     * Returns an item from the items HashMap. This should only be used if you
     * have the exact ID, otherwise use getItemsFromID.
     *
     * @param itemID The desired items ID.
     * @return The desired item.
     */
    public Item getItemFromID(String itemID) {
        ResultSet rs = tableManager.getRowByPrimaryKey(itemID);
        return getItemObjectsFromResultSet(rs).getFirst();
    }

    /**
     * Adds a new item to the items HashMap.
     *
     * @param name The items name.
     * @param location The items location
     * @param type The items type.
     * @return The Item ID of the Item Added.
     */
    public String addItem(String name, String location, String type) {

        if (!locationManager.isValidLocation(location)) {
            return null;
        }
        String itemID = generateItemID(type);
        HashMap<String, String> data = new HashMap<>();
        data.put("ItemID", itemID);
        data.put("Name", name);
        data.put("Location", location);
        data.put("Type", type);
        tableManager.createRow(data); // What if this fails
        return itemID;
    }

    /**
     *
     * @param item
     * @return true if updated (Item previously existed)
     */
    public boolean updateItem(Item item) {

        String id = item.getId();

        if (tableManager.verifyPrimaryKey(id)) { // Update the table
            HashMap<String, String> data = new HashMap<>();
            data.put("ItemID", id);
            data.put("Name", item.getName());
            data.put("Description", item.getDescription());
            data.put("Location", item.getLocation());
            data.put("Status", item.getStatus().toString());
            data.put("Type", item.getType());
            data.put("CalibrationFlag", String.valueOf(item.getNeedsCalibration()));
            data.put("LastCalibration", item.getLastCalibrationAsString());
            tableManager.updateRowByPrimaryKey(data);
            return true;
        }
        return false;
    }

    /**
     *
     * @param type the items type.
     * @return A unique item ID.
     */
    public String generateItemID(String type) {
        String newID;
        newID = "";
        for (String str : type.toUpperCase().split("/")) {
            newID += str.toCharArray()[0];
        }
        newID += String.format(tableManager.getNextPrimaryKeyId());
        return newID;
    }

    /**
     * Removes an item from the database
     *
     * @param itemID The ID of the item to be removed.
     * @return true if the item existed.
     */
    public boolean removeItem(String itemID) {
        return tableManager.deleteRowByPrimaryKey(itemID);
    }

    /**
     * Returns an ArrayList of items that partial match the partial partID from
     * the (if blank all items will be returned)
     *
     * @param partID The desired items partial ID.
     * @return A list of item IDs that match.
     */
    public ArrayList<String> searchItemsByID(String partID) {
        if (partID.isBlank()) {
            return tableManager.getAllPrimaryKeys();
        }
        ArrayList<String> validItems = new ArrayList<>();
        ResultSet rs = tableManager.searchColumn("ItemID", partID);
        try {
            while (rs.next()) {
                validItems.add(rs.getString("ItemID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return validItems;
    }

    /**
     * Returns an ArrayList of items that partial match the partial partName (if
     * blank all items will be returned)
     *
     * @param partName The desired items partial name.
     * @return A list of item IDs that match.
     */
    public ArrayList<String> searchItemsByName(String partName) {
        if (partName.isBlank()) {
            return tableManager.getAllPrimaryKeys();
        }
        ArrayList<String> validItems = new ArrayList<>();
        ResultSet rs = tableManager.searchColumn("Type", partName);
        try {
            while (rs.next()) {
                validItems.add(rs.getString("ItemID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return validItems;
    }

    /**
     * Returns an ArrayList of items that partial match the partial partType (if
     * blank all items will be returned)
     *
     * @param partType The desired items partial type.
     * @return A list of item IDs that match.
     */
    public ArrayList<String> getItemsFromType(String partType) {
        if (partType.isBlank()) {
            return tableManager.getAllPrimaryKeys();
        }
        ArrayList<String> validItems = new ArrayList<>();
        ResultSet rs = tableManager.searchColumn("Type", partType);
        try {
            while (rs.next()) {
                validItems.add(rs.getString("ItemID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return validItems;
    }

    /**
     *
     * @param itemID
     * @return True if ID is valid.
     */
    public boolean verifyID(String itemID) {
        return tableManager.verifyPrimaryKey(itemID);
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
    private ArrayList<Item> getItemObjectsFromResultSet(ResultSet resultSet) {

        ArrayList<Item> itemList = new ArrayList<Item>();

        try {

            while (resultSet.next()) { // User exists

                String itemID = resultSet.getString("ItemID");
                String name = resultSet.getString("Name");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                Status status = Status.valueOf(resultSet.getString("Status"));
                String type = resultSet.getString("Type");
                boolean calibrationFlag = resultSet.getBoolean("CalibrationFlag");
                ZonedDateTime lastCalibration = ZonedDateTime.parse(resultSet.getString("ReturnDate"), Item.getDateTimeFormatter());
                itemList.add(new Item(itemID, name, description, location, status, type, calibrationFlag, lastCalibration));
            }
            return itemList;
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
}
