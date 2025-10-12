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
    PreparedStatement st_getRowByPrimaryKey;
    PreparedStatement st_deleteRowByPrimaryKey;
    PreparedStatement st_updateRowByPrimaryKey;
    PreparedStatement st_createRowByPrimaryKey;
    PreparedStatement st_createTable;

    public TableManager(DatabaseManager dbManager, String tableName, HashMap columns, String primaryKey) {
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

        String sql_createTable = "CREATE TABLE " + tableName + "(";
        for (Map.Entry<String, String> col : columnDefinitions.entrySet()) { // Update everything other than the id
            sql_createTable += (col.getKey() + " " + col.getValue() + ", ");
        }
        sql_createTable += ("PRIMARY KEY (" + primaryKey + "))");

        // Print SQL to verify
        System.out.println(sql_createRowByPrimaryKey);
        System.out.println(sql_createTable);
        System.out.println(sql_deleteRowByPrimaryKey);
        System.out.println(sql_updateRowByPrimaryKey);
        System.out.println(sql_getRowByPrimaryKey);

        // Prepare Statements
        try {
            st_getRowByPrimaryKey = conn.prepareStatement(sql_getRowByPrimaryKey);
            st_deleteRowByPrimaryKey = conn.prepareStatement(sql_deleteRowByPrimaryKey);
            st_createRowByPrimaryKey = conn.prepareStatement(sql_createRowByPrimaryKey);
            st_updateRowByPrimaryKey = conn.prepareStatement(sql_updateRowByPrimaryKey);
            st_createTable = conn.prepareStatement(sql_createTable);
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

    private void createUserTableIfNotExist() {
        if (!dbManager.checkTableExists(tableName)) {
            System.out.println(tableName + " table does not exist. Creating table");
            try {
                // Create table. Assign primary key to prevent duplicates
                st_createTable.execute();
            } catch (SQLException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Add test data to table
            dbManager.updateDB("INSERT INTO USERTABLE VALUES ('000001', 'Bob', 'MANAGER'), "
                    + "('000002', 'Sally', 'EMPLOYEE'), "
                    + "('000003', 'Fred', 'GUEST')");
        }
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

}
