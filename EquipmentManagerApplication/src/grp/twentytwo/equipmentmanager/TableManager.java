package grp.twentytwo.equipmentmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ppj1707
 */
public class TableManager {

    private final Connection conn;
    private final DatabaseManager dbManager;

    private final String tableName;
    private final String primaryKey;
    private final HashMap<String, String> columnDefinitions;
    private final ArrayList<String> columnNames; // Store column names to make access easier. DOES NOT include the primary key
    private final ArrayList<String> allColumns; // Store column names INCLUDING the primary key

    // Prepared Statements
    private PreparedStatement st_getRowByPrimaryKey;
    private PreparedStatement st_deleteRowByPrimaryKey;
    private PreparedStatement st_updateRowByPrimaryKey;
    private PreparedStatement st_createRowByPrimaryKey;
    private PreparedStatement st_createTable;
    private PreparedStatement st_getMaxPrimaryKey;

    public TableManager(DatabaseManager dbManager, String tableName, HashMap<String, String> columns, String primaryKey) {
        this.dbManager = dbManager;
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.columnDefinitions = columns;
        this.columnNames = new ArrayList<>(this.columnDefinitions.keySet());
        this.allColumns = (ArrayList<String>) columnNames.clone();
        this.columnNames.remove(primaryKey);
        conn = dbManager.getConnection();
        System.out.println(conn);
        createUserTableIfNotExist();
        prepareStatements();
    }

    public void prepareStatements() {

        // Prepare SQL
        String sql_getRowByPrimaryKey = "SELECT * FROM " + tableName + " where " + primaryKey + " = ?";

        String sql_deleteRowByPrimaryKey = "DELETE FROM " + tableName + " where " + primaryKey + " = ?";

        String sql_updateRowByPrimaryKey = "UPDATE " + tableName + " SET ";
        for (int i = 0; i < columnNames.size(); i++) { // Update everything other than the id
            sql_updateRowByPrimaryKey += (columnNames.get(i) + " = ?");
            if (i != columnNames.size() - 1) { // If column is not the last one
                sql_updateRowByPrimaryKey += ", ";
            }
        }
        sql_updateRowByPrimaryKey += (" WHERE " + primaryKey + " = ?");

        String sql_createRowByPrimaryKey = "INSERT INTO " + tableName + " (";
        for (int i = 0; i < allColumns.size(); i++) { // Update everything other than the id
            sql_createRowByPrimaryKey += allColumns.get(i);
            if (i != allColumns.size() - 1) { // If column is not the last one
                sql_createRowByPrimaryKey += ", ";
            }
        }
        sql_createRowByPrimaryKey += ") VALUES (";
        for (int i = 0; i < allColumns.size(); i++) { // Update everything other than the id
            sql_createRowByPrimaryKey += "?";
            if (i != allColumns.size() - 1) { // If column is not the last one
                sql_createRowByPrimaryKey += ", ";
            }
        }
        sql_createRowByPrimaryKey += ")";

        String sql_getMaxPrimaryKey = "SELECT MAX(" + primaryKey + ") AS maxId FROM " + tableName;

        // Print SQL to verify
        System.out.println(sql_createRowByPrimaryKey);
        System.out.println(sql_deleteRowByPrimaryKey);
        System.out.println(sql_updateRowByPrimaryKey);
        System.out.println(sql_getRowByPrimaryKey);
        System.out.println(sql_getMaxPrimaryKey);

        // Prepare Statements
        try {
            st_getRowByPrimaryKey = conn.prepareStatement(sql_getRowByPrimaryKey);
            st_deleteRowByPrimaryKey = conn.prepareStatement(sql_deleteRowByPrimaryKey);
            st_createRowByPrimaryKey = conn.prepareStatement(sql_createRowByPrimaryKey);
            st_updateRowByPrimaryKey = conn.prepareStatement(sql_updateRowByPrimaryKey);

            st_getMaxPrimaryKey = conn.prepareStatement(sql_getMaxPrimaryKey);
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ResultSet getRowByPrimaryKey(String primaryKeyValue) {
        try {
            st_getRowByPrimaryKey.setString(1, primaryKeyValue);
            ResultSet rs = st_getRowByPrimaryKey.executeQuery();
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public boolean deleteRowByPrimaryKey(String primaryKeyValue) {
        try {
            st_deleteRowByPrimaryKey.setString(1, primaryKeyValue);
            st_deleteRowByPrimaryKey.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void updateRowByPrimaryKey(HashMap<String, String> columnData) {
        //Verify hashmap
        try {
            for (int i = 0; i < columnNames.size(); i++) {
                st_updateRowByPrimaryKey.setString(i + 1, columnData.get(columnNames.get(i))); // 
            }
            st_updateRowByPrimaryKey.setString(columnData.size(), columnData.get(primaryKey)); // Condition
            st_updateRowByPrimaryKey.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createRow(HashMap<String, String> columnData) {
        //Verify hashmap
        try {
            for (int i = 0; i < allColumns.size(); i++) {
                st_createRowByPrimaryKey.setString(i + 1, columnData.get(allColumns.get(i))); // 
            }
            st_createRowByPrimaryKey.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean createUserTableIfNotExist() {

        String sql_createTable = "CREATE TABLE " + tableName + "(";
        for (Map.Entry<String, String> col : columnDefinitions.entrySet()) { // Update everything other than the id
            sql_createTable += (col.getKey() + " " + col.getValue() + ", ");
        }
        sql_createTable += ("PRIMARY KEY (" + primaryKey + "))");
        try {
            st_createTable = conn.prepareStatement(sql_createTable);
        } catch (SQLException ex) {
            Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!dbManager.checkTableExists(tableName)) {
            System.out.println(tableName + " table does not exist. Creating table");
            try {
                // Create table. Assign primary key to prevent duplicates
                st_createTable.execute();
            } catch (SQLException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }

        return false;

    }

    public void printTable() {
        try {
            System.out.println("Table Data:");
            ResultSet rs = dbManager.queryDB("SELECT * FROM " + tableName);
            while (rs.next()) {
                for (String col : allColumns) {
                    System.out.print(col + ": " + rs.getString(col) + "     ");
                }
                System.out.print("\n");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param id
     * @return whether the user ID is registered in the system
     */
    public boolean verifyPrimaryKey(String id) {
        try {
            ResultSet rs = getRowByPrimaryKey(id);
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

    public ResultSet getRowByColumnValue(String columnName, String value) {
        String sql_query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = '" + value + "'";
        return dbManager.queryDB(sql_query);
    }

    /**
     * Searches a column for any partial instance of 'value'
     *
     * @param columnName
     * @param value
     * @return
     */
    public ResultSet searchColumn(String columnName, String value) {
        String sql_query = "SELECT * FROM " + tableName + " WHERE " + columnName + " LIKE '" + value + "'"; // %%
        return dbManager.queryDB(sql_query);
    }

    /**
     *
     * @return the next primary key based (incremented from the maximum key)
     */
    public String getNextPrimaryKeyId() {
        String next = "";
        try {
            ResultSet rs = st_getMaxPrimaryKey.executeQuery();
            if (rs.next()) {
                if (rs.getString("maxId") != null) {
                    return Integer.toString(Integer.parseInt(rs.getString("maxId").replaceAll("[^0-9]", "")) + 1);
                } else {
                    next = "0";  // No data in the table yet
                }
            } else { // No data in the table yet
                next = "0";
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        //String next = Integer.toString(Integer.parseInt(rs.getString(primaryKey)) + 1);
        return next;

    }

    /**
     *
     * @return all the primary keys in the table as an ArrayList
     */
    public ArrayList<String> getAllPrimaryKeys() {
        String sql_query = "SELECT " + primaryKey + " FROM " + tableName;
        ResultSet rs = dbManager.queryDB(sql_query);
        ArrayList<String> keyList = new ArrayList<>();
        try {
            while (rs.next()) {
                keyList.add(rs.getString(primaryKey));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return keyList;
    }
}
