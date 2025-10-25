package grp.twentytwo.equipmentmanager;

import grp.twentytwo.database.DatabaseManager;
import grp.twentytwo.database.Column;
import grp.twentytwo.database.DatabaseConnectionException;
import grp.twentytwo.database.TableManager;
import grp.twentytwo.database.InvalidColumnNameException;
import grp.twentytwo.database.NonNumericKeyClashException;
import grp.twentytwo.database.NullColumnValueException;
import grp.twentytwo.database.PrimaryKeyClashException;
import grp.twentytwo.database.UnfoundPrimaryKeyException;
import grp.twentytwo.equipmentmanager.Item.Status;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages item operations such as adding, removing, generating IDs, and
 * retrieving items from the HashMap.
 *
 * @author ppj1707
 */
class ItemManager {

    private final DatabaseManager dbManager;
    private final TableManager tableManager;

    // Database table properties
    private final String tableName = "ITEMTABLE";
    private final ArrayList<Column> columnData;

    private Column column_itemID = new Column("ItemID", "VARCHAR(12) not NULL", "");
    private Column column_name = new Column("Name", "VARCHAR(30) not NULL", "");
    private Column column_description = new Column("Description", "VARCHAR(200)", "");
    private Column column_location = new Column("Location", "VARCHAR(30)", "");
    private Column column_status = new Column("Status", "VARCHAR(14)", "");
    private Column column_type = new Column("Type", "VARCHAR(40)", "");
    private Column column_calibrationFlag = new Column("CalibrationFlag", "VARCHAR(6)", "");
    private Column column_lastCalibration = new Column("LastCalibration", "VARCHAR(20)", "");

    ItemManager(DatabaseManager databaseManager) throws DatabaseConnectionException {
        this.dbManager = databaseManager;
        // Define Table Parameters
        columnData = new ArrayList<Column>();
        columnData.add(column_itemID);
        columnData.add(column_name);
        columnData.add(column_description);
        columnData.add(column_location);
        columnData.add(column_status);
        columnData.add(column_type);
        columnData.add(column_calibrationFlag);
        columnData.add(column_lastCalibration);

        //DESCRIPTION MAX 200. HOW THROW THIS ERRPR?????!!!!!
        // Initialise Table
        tableManager = new TableManager(dbManager, tableName, columnData, column_itemID);

        // Add test data to table
        //dbManager.updateDB("INSERT INTO ITEMTABLE VALUES ('1', 'R9000 Universal Laser Cutter', 'Cuts mdf (1mm-12mm), arcrylic (1mm-10mm)', 'Workshop1', 'WORKING', 'Manufacturing/Cutting', '0', '03-03-2025 14:20')");
    }

    /**
     * Returns an item. This should only be used if you have the exact ID,
     * otherwise use search.
     *
     * @param itemID The desired items ID.
     * @return The desired item.
     */
    Item getItemFromID(String itemID) throws UnfoundPrimaryKeyException {
        try {
            ResultSet rs = tableManager.getRowByPrimaryKey(itemID);
            return getItemObjectsFromResultSet(rs).getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("InvalidID");
            return null;
        }
    }

    /**
     *
     * @param locationName
     * @return Items for a specified location name
     */
    ArrayList<String> getItemsForLocation(String locationName) {
        try {
            ArrayList<String> validItems = new ArrayList<>();
            ResultSet rs = tableManager.getRowByColumnValue("Location", locationName);
            while (rs.next()) {
                validItems.add(rs.getString("ItemID"));
            }
            return validItems;
        } catch (NoSuchElementException e) {
            System.out.println("InvalidID");
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Adds a new item to the items HashMap.
     *
     * @param item An item object. Id will be ignored
     * @return The Item ID of the Item Added.
     */
    String addItem(Item item) throws PrimaryKeyClashException, NullColumnValueException {
        item.setID(generateItemID(item.getType()));
        column_itemID.data = item.getID();
        column_name.data = item.getName();
        column_description.data = item.getDescription();
        column_location.data = item.getLocation().toUpperCase();
        column_status.data = item.getStatus().toString();
        column_type.data = item.getType();
        column_calibrationFlag.data = String.valueOf(item.getNeedsCalibration());
        column_lastCalibration.data = item.getLastCalibrationAsString();
        try {
            tableManager.createRow(columnData);
            return column_itemID.data; // Item id
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     *
     * @param item
     * @return true if updated (Item previously existed)
     */
    boolean updateItem(Item item) throws UnfoundPrimaryKeyException {

        String id = item.getID();

        if (tableManager.verifyPrimaryKey(id)) { // Update the table
            return saveItem(item);
        }
        return false;
    }

    /**
     * Verifies item parameters (eg. location) !!!!!!!!!!!! Mediator level???
     *
     * @param item
     * @return true if updated (Item previously existed)
     */
    boolean saveItem(Item item) throws UnfoundPrimaryKeyException {

        String id = item.getID();

        if (tableManager.verifyPrimaryKey(id)) { // Update the table
            column_itemID.data = id;
            column_name.data = item.getName();
            column_description.data = item.getDescription();
            column_location.data = item.getLocation().toUpperCase();
            column_status.data = item.getStatus().toString();
            column_type.data = item.getType();
            column_calibrationFlag.data = String.valueOf(item.getNeedsCalibration());
            column_lastCalibration.data = item.getLastCalibrationAsString();
            try {
                tableManager.updateRowByPrimaryKey(columnData);
            } catch (InvalidColumnNameException ex) {
                Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
        return false;
    }

    /**
     *
     * @param type the items type.
     * @return A unique item ID.
     */
    private String generateItemID(String type) {
        String newID;
        newID = "";
        for (String str : type.toUpperCase().split("/")) {
            newID += str.toCharArray()[0];
        }
        try {
            newID += String.format(tableManager.getNextPrimaryKeyId());
        } catch (NonNumericKeyClashException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newID;
    }

    /**
     * Removes an item from the database
     *
     * @param itemID The ID of the item to be removed.
     * @return true if the item existed.
     */
    boolean removeItem(String itemID) throws UnfoundPrimaryKeyException {
        return tableManager.deleteRowByPrimaryKey(itemID);
    }

    /**
     * Returns an ArrayList of items that partial match the partial partID from
     * the (if blank all items will be returned)
     *
     * @param partID The desired items partial ID.
     * @return A list of item IDs that match.
     */
    ArrayList<String> searchItemsByID(String partID) {
        if (partID.isBlank()) {
            return tableManager.getAllPrimaryKeys();
        }
        ArrayList<String> validItems = new ArrayList<>();
        ResultSet rs;
        try {
            rs = tableManager.searchColumn("ItemID", partID);
            while (rs.next()) {
                validItems.add(rs.getString("ItemID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidColumnNameException ex) {
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
    ArrayList<String> searchItemsByName(String partName) {
        if (partName.isBlank()) {
            return tableManager.getAllPrimaryKeys();
        }
        ArrayList<String> validItems = new ArrayList<>();
        ResultSet rs;
        try {
            rs = tableManager.searchColumn("Name", partName);
            while (rs.next()) {
                validItems.add(rs.getString("ItemID"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidColumnNameException ex) {
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
    ArrayList<String> searchItemsByType(String partType) {
        try {
            if (partType.isBlank()) {
                return tableManager.getAllPrimaryKeys();
            }
            ArrayList<String> validItems = new ArrayList<>();
            ResultSet rs;

            rs = tableManager.searchColumn("Type", partType);

            while (rs.next()) {
                validItems.add(rs.getString("ItemID"));
            }
            return validItems;
        } catch (SQLException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Returns an ArrayList of items that partial match the partial partType,
     * name, or id (if blank all items will be returned)
     *
     * @param searchString
     * @return
     */
    LinkedHashSet<String> searchForItems(String searchString) {

        LinkedHashSet<String> validItems = new LinkedHashSet<>(); // Hashset forces duplicates to be removed. Preserves order

        validItems.addAll(searchItemsByID(searchString));
        validItems.addAll(searchItemsByName(searchString));
        validItems.addAll(searchItemsByType(searchString));
        return validItems;
    }

    /**
     *
     * @param itemID
     * @return True if ID is valid.
     */
    boolean verifyID(String itemID) {
        return tableManager.verifyPrimaryKey(itemID);
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
    private ArrayList<Item> getItemObjectsFromResultSet(ResultSet resultSet) {

        ArrayList<Item> itemList = new ArrayList<Item>();

        try {

            while (resultSet.next()) { // User exists

                String itemID = resultSet.getString("ItemID");
                String name = resultSet.getString("Name");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String statusString = resultSet.getString("Status");
                Status status = null;
                if (statusString != null && !statusString.isBlank()) {
                    status = Status.valueOf(statusString);
                }
                String type = resultSet.getString("Type");
                boolean calibrationFlag = resultSet.getBoolean("CalibrationFlag");
                String calibrationDateString = resultSet.getString("LastCalibration");
                LocalDateTime lastCalibration = null;
                if (calibrationDateString != null && !calibrationDateString.isBlank()) {
                    lastCalibration = LocalDateTime.parse(calibrationDateString, Item.getDateTimeFormatter());
                }
                itemList.add(new Item(itemID, name, description, location, status, type, calibrationFlag, lastCalibration));
            }
            return itemList;
        } catch (SQLException ex) {
            Logger.getLogger(ItemManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
}
