package grp.twentytwo.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private final Column primaryKey;
    private final ArrayList<Column> allColumns;
    private final ArrayList<Column> valueColumns; // Excludes column with primary key

    // Prepared Statements
    private PreparedStatement st_getRowByPrimaryKey;
    private PreparedStatement st_deleteRowByPrimaryKey;
    private PreparedStatement st_updateRowByPrimaryKey;
    private PreparedStatement st_createRowByPrimaryKey;
    private PreparedStatement st_createTable;
    private PreparedStatement st_dropTable;
    private PreparedStatement st_getMaxPrimaryKey;

    public TableManager(DatabaseManager dbManager, String tableName, ArrayList<Column> columns, Column primaryKey) throws DatabaseConnectionException {
        this.dbManager = dbManager;
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.allColumns = columns;
        this.valueColumns = new ArrayList<Column>() {
        };
        if (!columns.contains(primaryKey)) {
            allColumns.add(primaryKey); // Add primary key if not given
        }
        for (int i = 1; i < allColumns.size(); i++) {
            if (allColumns.get(i) != primaryKey) {
                valueColumns.add(allColumns.get(i));
            }
        }
        valueColumns.remove(primaryKey); // Excludes column with primary key
        conn = dbManager.getConnection();
        System.out.println(conn);
        createTableIfNotExist();
        prepareStatements();
    }

    public void prepareStatements() {

        // Prepare SQL
        String sql_getRowByPrimaryKey = "SELECT * FROM " + tableName + " where " + primaryKey + " = ?";

        String sql_deleteRowByPrimaryKey = "DELETE FROM " + tableName + " where " + primaryKey + " = ?";

        String sql_updateRowByPrimaryKey = "UPDATE " + tableName + " SET ";
        for (int i = 0; i < valueColumns.size(); i++) { // Update everything other than the id
            sql_updateRowByPrimaryKey += (valueColumns.get(i) + " = ?");
            if (i != valueColumns.size() - 1) { // If column is not the last one
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
        System.out.println("Update Row By Primary Key: " + sql_updateRowByPrimaryKey);
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
            Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ResultSet getRowByPrimaryKey(String primaryKeyValue) throws UnfoundPrimaryKeyException {
        try {
            if (verifyPrimaryKey(primaryKeyValue)) {
                st_getRowByPrimaryKey.setString(1, primaryKeyValue);
                ResultSet rs = st_getRowByPrimaryKey.executeQuery();
                return rs;
            } else {
                throw new UnfoundPrimaryKeyException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean deleteRowByPrimaryKey(String primaryKeyValue) throws UnfoundPrimaryKeyException {
        try {
            if (verifyPrimaryKey(primaryKeyValue)) {
                st_deleteRowByPrimaryKey.setString(1, primaryKeyValue);
                st_deleteRowByPrimaryKey.executeUpdate();
                return true;
            } else {
                throw new UnfoundPrimaryKeyException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean updateRowByPrimaryKey(ArrayList<Column> columnData) throws InvalidColumnNameException, UnfoundPrimaryKeyException {
        if (verifyDataMapping(columnData)) { // Verify the data array
            if (!verifyPrimaryKey(columnData)) {
                throw new InvalidColumnNameException();
            }
            try {

                int st_index = 1;
                for (int i = 0; i < allColumns.size(); i++) {
                    if (!columnData.get(i).getName().equals(primaryKey.getName())) { // Ensure primary key cannot be updated
                        st_updateRowByPrimaryKey.setString(st_index, columnData.get(i).data); // Add data
                        st_index++;
                    }
                }
                st_updateRowByPrimaryKey.setString(columnData.size(), primaryKey.data); // Condition
                st_updateRowByPrimaryKey.executeUpdate();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            throw new InvalidColumnNameException();
        }
        return false;
    }

    public void createRow(ArrayList<Column> columnData) throws InvalidColumnNameException, PrimaryKeyClashException, NullColumnValueException {
        if (verifyDataMapping(columnData)) { // Verify the data array
            try {
                if (verifyPrimaryKey(columnData)) {
                    throw new PrimaryKeyClashException();
                }
                for (int i = 0; i < allColumns.size(); i++) {
                    st_createRowByPrimaryKey.setString(i + 1, columnData.get(i).data); // 
                }
                st_createRowByPrimaryKey.executeUpdate();
            } catch (SQLException ex) {
                throw new NullColumnValueException();
            }
        } else {
            throw new InvalidColumnNameException();
        }

    }

    private boolean createTableIfNotExist() {

        String sql_createTable = "CREATE TABLE " + tableName + "(";
        for (Column col : allColumns) { // Update everything other than the id
            sql_createTable += (col.getName() + " " + col.getType() + ", ");
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
                Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }

        return false;

    }

    private boolean dropTableIfExists() {

        String sql_createTable = "DROP TABLE " + tableName;
        try {
            st_dropTable = conn.prepareStatement(sql_createTable);
        } catch (SQLException ex) {
            Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (dbManager.checkTableExists(tableName)) {
            System.out.println(tableName + " table exists. Dropping table.");
            try {
                // Create table. Assign primary key to prevent duplicates
                st_dropTable.execute();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;

    }

    public void printTable() {
        try {
            System.out.println("Table Data:");
            ResultSet rs = dbManager.queryDB("SELECT * FROM " + tableName);
            while (rs.next()) {
                for (Column col : allColumns) {
                    System.out.print(col.getName() + ": " + rs.getString(col.getName()) + "     ");
                }
                System.out.print("\n");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param id
     * @return whether the user ID is registered in the system
     */
    public boolean verifyPrimaryKey(String id) {
        try {
            st_getRowByPrimaryKey.setString(1, id);
            ResultSet rs = st_getRowByPrimaryKey.executeQuery();
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

    public boolean verifyPrimaryKey(ArrayList<Column> columnData) {
        for (Column col : columnData) {
            if (col.getName().equals(primaryKey.getName())) {
                if (verifyPrimaryKey(col.data)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public ResultSet getRowByColumnValue(String columnName, String value) throws InvalidColumnNameException {
        if (!verifyColumnName(columnName)) {
            throw new InvalidColumnNameException();
        }
        String sql_query = "SELECT * FROM " + tableName + " WHERE UPPER(" + columnName + ") = UPPER('" + value + "')";
        System.out.println(sql_query);
        return dbManager.queryDB(sql_query);
    }

    /**
     * Searches a column for any partial instance of 'value'. Case in-sensitive
     *
     * @param columnName
     * @param value
     * @return
     * @throws grp.twentytwo.database.InvalidColumnNameException
     */
    public ResultSet searchColumn(String columnName, String value) throws InvalidColumnNameException {
        if (!verifyColumnName(columnName)) {
            throw new InvalidColumnNameException();
        }
        String sql_query = "SELECT * FROM " + tableName + " WHERE LOWER (" + columnName + ") LIKE LOWER('%" + value + "%')"; // %%
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
        String sql_query = "SELECT " + primaryKey.getName() + " FROM " + tableName;
        ResultSet rs = dbManager.queryDB(sql_query);
        ArrayList<String> keyList = new ArrayList<>();
        try {
            while (rs.next()) {
                keyList.add(rs.getString(primaryKey.getName()));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TableManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return keyList;
    }

    public boolean verifyDataMapping(ArrayList<Column> columnData) { // Verify the received array has matching table column names, same index
        if (columnData == null || (allColumns.size() != columnData.size())) {
            return false;
        }
        for (int i = 0; i < allColumns.size(); i++) {
            if (!allColumns.get(i).getName().equals(columnData.get(i).getName())) {
                return false;
            }
        }
        return true;
    }

    public boolean verifyColumnName(String colName) { // Verify a columnName exists
        for (int i = 0; i < allColumns.size(); i++) {
            if (allColumns.get(i).getName().equals(colName)) {
                return true;
            }
        }
        return false;
    }
}
