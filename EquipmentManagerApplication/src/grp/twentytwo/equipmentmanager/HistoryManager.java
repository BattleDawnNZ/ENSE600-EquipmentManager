package grp.twentytwo.equipmentmanager;

import grp.twentytwo.database.DatabaseManager;
import grp.twentytwo.database.Column;
import grp.twentytwo.database.TableManager;
import grp.twentytwo.database.InvalidColumnNameException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ppj1707
 */
public class HistoryManager {
    /// MAX 50 description

    private final DatabaseManager dbManager;
    private final TableManager tableManager;

    String tableName = "HISTORYTABLE";
    private final ArrayList<Column> columnData;

    private Column column_historyID = new Column("HistoryID", "VARCHAR(12) not NULL", "");
    private Column column_itemID = new Column("ItemID", "VARCHAR(12)", "");
    private Column column_timestamp = new Column("Timestamp", "VARCHAR(30)", "");
    private Column column_description = new Column("Description", "VARCHAR(50)", "");

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public HistoryManager(DatabaseManager databaseManager) {
        this.dbManager = databaseManager;

        // Define Table Parameters
        columnData = new ArrayList<Column>();
        columnData.add(column_historyID);
        columnData.add(column_itemID);
        columnData.add(column_timestamp);
        columnData.add(column_description);
        tableManager = new TableManager(dbManager, tableName, columnData, column_historyID);
    }

    /**
     *
     * @param historyID
     * @return a History object (if the id string exists) else, null
     */
    public History getHistoryFromID(String historyID) {
        try {
            ResultSet rs = tableManager.getRowByPrimaryKey(historyID);
            return getHistoryObjectsFromResultSet(rs).getFirst();
        } catch (NoSuchElementException e) {
            System.out.println("InvalidID");
            return null;
        }
    }

    /**
     *
     * @param itemID
     * @return All history for the item.
     */
    public ArrayList<History> getHistoryForItem(String itemID) {
        ResultSet rs;
        try {
            rs = tableManager.getRowByColumnValue("ItemID", itemID);
            return getHistoryObjectsFromResultSet(rs);
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(BookingManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     *
     * @param historyID
     * @return true if the history entry exists
     */
    public boolean isValidHistoryID(String historyID) {
        ResultSet rs = tableManager.getRowByPrimaryKey(historyID);
        return ((getHistoryObjectsFromResultSet(rs).size()) > 0);
    }

    public boolean addHistory(History history) {
        try {
            column_historyID.data = tableManager.getNextPrimaryKeyId();
            column_itemID.data = history.getItemID();
            column_description.data = history.getDescription();
            column_timestamp.data = history.getTimestamp().format(formatter);
            tableManager.createRow(columnData);
            return true; // History created
        } catch (InvalidColumnNameException e) {
            System.out.println("Error: Invalid Column Name!");
            return false;
        }
    }

    /**
     * Print the entire history table to the console
     */
    public void printTable() {
        tableManager.printTable();
    }

    /**
     *
     * @param resultSet
     * @return a user object
     */
    private ArrayList<History> getHistoryObjectsFromResultSet(ResultSet resultSet) {
        try {
            ArrayList<History> historyObjects = new ArrayList<History>();
            while (resultSet.next()) { // User exists
                String historyID = resultSet.getString("HistoryID");
                String description = resultSet.getString("Description");
                LocalDateTime timestamp = LocalDateTime.parse(resultSet.getString("Timestamp"), formatter);
                String itemID = resultSet.getString("ItemID");
                historyObjects.add(new History(historyID, itemID, timestamp, description));
            }
            return historyObjects;
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
