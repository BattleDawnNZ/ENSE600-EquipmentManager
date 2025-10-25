/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package grp.twentytwo.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This JUNIT class tests key functionality of the table manager. Eg. create,
 * update delete, get rows and getNextPrimaryKey. Low-level functions are
 * inheritely tested therefore focus is on these main functions.
 *
 * @author mymys
 */
public class TableManagerTest {

    static DatabaseManager dbManager;
    static TableManager tableManager;
    static String tableName = "TESTTABLE";
    static ArrayList<Column> columns;
    static Column column_primaryKey;
    static Column column_dataColumn;

    public TableManagerTest() {
    }

    @Before
    public void setUp() {
        try {
            dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
            dbManager.dropTableIfExists(tableName);
            columns = new ArrayList<>();
            column_primaryKey = new Column("Column1", "VARCHAR(12)", "00000");
            column_dataColumn = new Column("Column2", "VARCHAR(12)", "data2");
            columns.add(column_primaryKey);
            columns.add(column_dataColumn);
            tableManager = new TableManager(dbManager, tableName, columns, column_primaryKey);
            tableManager.createRow(columns);
        } catch (DatabaseConnectionException | InvalidColumnNameException | NullColumnValueException | PrimaryKeyClashException ex) {
            Logger.getLogger(TableManagerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ------------------ CREATE ROW TESTS -------------------- \\
    @Test(expected = PrimaryKeyClashException.class)
    public void testCreateRow_duplicateKey() throws InvalidColumnNameException, PrimaryKeyClashException, NullColumnValueException {
        System.out.println("TEST: Create row, duplicate key");
        tableManager.createRow(columns);
        tableManager.createRow(columns);
    }

    @Test(expected = NullColumnValueException.class)
    public void testCreateRow_nullKey() throws InvalidColumnNameException, PrimaryKeyClashException, NullColumnValueException {
        System.out.println("TEST: Create row, Null primary key");
        column_primaryKey.data = null;
        tableManager.createRow(columns);
    }

    @Test(expected = InvalidColumnNameException.class)
    public void testCreateRow_invalidColumn() throws InvalidColumnNameException, PrimaryKeyClashException, NullColumnValueException {
        System.out.println("TEST: Create row, Invalid column definitions");
        // Create new, different column array
        ArrayList<Column> newArr = new ArrayList<>();
        newArr.add(new Column("Column1", "VARCHAR(12)", "key"));
        newArr.add(new Column("Column2", "VARCHAR(12)", "587"));
        newArr.add(new Column("Column5", "VARCHAR(12)", "data1")); // Data here is different
        tableManager.createRow(newArr);
    }

    @Test
    public void testCreateRow_valid() throws InvalidColumnNameException, PrimaryKeyClashException, NullColumnValueException {
        System.out.println("TEST: Create row, valid column names and data");
        column_primaryKey.data = "1234"; // Unique, non-duplicate key
        tableManager.createRow(columns);
    }

    // ------------------ GET ROW TESTS -------------------- \\
    @Test
    public void testGetRowByPrimaryKey_valid() throws UnfoundPrimaryKeyException {
        System.out.println("TEST: Get row, valid primaryKey");
        tableManager.getRowByPrimaryKey(column_primaryKey.data);
    }

    @Test(expected = UnfoundPrimaryKeyException.class)
    public void testGetRowByPrimaryKey_invalidKey() throws UnfoundPrimaryKeyException {
        System.out.println("TEST: Get row, non-existent primaryKey");
        tableManager.getRowByPrimaryKey("989"); // Invalid Key
    }

    @Test(expected = UnfoundPrimaryKeyException.class)
    public void testGetRowByPrimaryKey_nullKey() throws UnfoundPrimaryKeyException {
        System.out.println("TEST: Get row, null primaryKey");
        tableManager.getRowByPrimaryKey(null); // Invalid Key
    }

    // ------------------ DELETE ROW TESTS -------------------- \\
    @Test
    public void testDeleteRowByPrimaryKey_valid() throws UnfoundPrimaryKeyException, SQLException {
        System.out.println("TEST: Delete row, valid primaryKey");
        boolean result = tableManager.deleteRowByPrimaryKey(column_primaryKey.data);
        boolean verify = false;
        if (!tableManager.verifyPrimaryKey(column_primaryKey.data)) {
            verify = true;
        }
        assert (result && verify);
    }

    @Test(expected = UnfoundPrimaryKeyException.class)
    public void testDeleteRowByPrimaryKey_invalidKey() throws UnfoundPrimaryKeyException {
        System.out.println("TEST: Delete row, non-existent primaryKey");
        tableManager.deleteRowByPrimaryKey("242");
    }

    @Test(expected = UnfoundPrimaryKeyException.class)
    public void testDeleteRowByPrimaryKey_nullKey() throws UnfoundPrimaryKeyException {
        System.out.println("TEST: Delete row, null primaryKey");
        tableManager.deleteRowByPrimaryKey(null); // Invalid Key
    }

    // ------------------ UPDATE ROW TESTS -------------------- \\
    @Test
    public void testUpdateRowByPrimaryKey_valid() throws UnfoundPrimaryKeyException, SQLException, InvalidColumnNameException {
        System.out.println("TEST: Update row, valid primaryKey and data");
        column_dataColumn.data = "22";
        tableManager.updateRowByPrimaryKey(columns);
        ResultSet newVal = tableManager.getRowByPrimaryKey(column_primaryKey.data);
        if (newVal.next()) {
            assert (newVal.getString("Column2").equals("22"));
        }
    }

    @Test(expected = UnfoundPrimaryKeyException.class)
    public void testUpdateRowByPrimaryKey_invalidKey() throws UnfoundPrimaryKeyException, InvalidColumnNameException {
        System.out.println("TEST: Update row, non-existent primaryKey");
        column_primaryKey.data = "242";
        tableManager.updateRowByPrimaryKey(columns);
    }

    @Test(expected = UnfoundPrimaryKeyException.class)
    public void testUpdateRowByPrimaryKey_nullKey() throws UnfoundPrimaryKeyException, InvalidColumnNameException {
        System.out.println("TEST: Update row, null primaryKey");
        column_primaryKey.data = null;
        tableManager.updateRowByPrimaryKey(columns);
    }

    @Test(expected = InvalidColumnNameException.class)
    public void testUpdateRowByPrimaryKey_invalidColumn() throws InvalidColumnNameException, UnfoundPrimaryKeyException {
        System.out.println("TEST: Update row, Invalid column definitions");
        // Create new, different column array
        ArrayList<Column> newArr = new ArrayList<>();
        newArr.add(new Column("Column1", "VARCHAR(12)", "key"));
        newArr.add(new Column("Column2", "VARCHAR(12)", "587"));
        newArr.add(new Column("Column5", "VARCHAR(12)", "data1")); // Data here is different
        tableManager.updateRowByPrimaryKey(newArr);
    }

    // ------------------ GET NEXT PRIMARY KEY TESTS -------------------- \\
    @Test
    public void testGetNextPrimaryKey_numericalKey() throws UnfoundPrimaryKeyException, DatabaseConnectionException, DatabaseConnectionException, DatabaseConnectionException, DatabaseConnectionException, DatabaseConnectionException, NonNumericKeyClashException {
        System.out.println("TEST: Get next primary key, numerical first entry");
        String nextKey = tableManager.getNextPrimaryKeyId();
        assert (nextKey.equals("00001"));
    }

    @Test(expected = NonNumericKeyClashException.class)
    public void testGetNextPrimaryKey_nonNumericKey() throws UnfoundPrimaryKeyException, DatabaseConnectionException, InvalidColumnNameException, PrimaryKeyClashException, NullColumnValueException, NonNumericKeyClashException {
        System.out.println("TEST: Get next primary key, alphabetical first entry");
        column_primaryKey.data = "hhh";
        tableManager.createRow(columns);
        String nextKey = tableManager.getNextPrimaryKeyId();
        assert (nextKey.equals("00001"));
    }

    @Test
    public void testGetNextPrimaryKey_partialNumericKey() throws UnfoundPrimaryKeyException, DatabaseConnectionException, InvalidColumnNameException, PrimaryKeyClashException, NullColumnValueException, NonNumericKeyClashException {
        System.out.println("TEST: Get next primary key, alphabetical first entry");
        column_primaryKey.data = "h2";
        tableManager.createRow(columns);
        String nextKey = tableManager.getNextPrimaryKeyId();
        assert (nextKey.equals("00003"));
    }

    @Test
    public void testGetNextPrimaryKey_largeNumber() throws UnfoundPrimaryKeyException, DatabaseConnectionException, InvalidColumnNameException, PrimaryKeyClashException, NullColumnValueException, NonNumericKeyClashException {
        System.out.println("TEST: Get next primary key, large entries in DB (10000)");
        column_primaryKey.data = "10000";
        tableManager.createRow(columns);
        String nextKey = tableManager.getNextPrimaryKeyId();
        assert (nextKey.equals("10001"));
    }

    @Test
    public void testGetNextPrimaryKey_emptyTable() throws UnfoundPrimaryKeyException, DatabaseConnectionException, InvalidColumnNameException, PrimaryKeyClashException, NullColumnValueException, NonNumericKeyClashException {
        System.out.println("TEST: Get next primary key, alphabetical first entry");
        tableManager.deleteRowByPrimaryKey(column_primaryKey.data);
        String nextKey = tableManager.getNextPrimaryKeyId();
        assert (nextKey.equals("00000"));
    }

    @After
    public void tearDown() {
        dbManager.closeConnections();
    }
}
