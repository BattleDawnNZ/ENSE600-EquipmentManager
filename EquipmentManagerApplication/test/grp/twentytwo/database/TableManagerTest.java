/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package grp.twentytwo.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
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

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
            dbManager.dropTableIfExists(tableName);
            columns = new ArrayList<Column>();
            column_primaryKey = new Column("Column1", "VARCHAR(12)", "key");
            column_dataColumn = new Column("Column2", "VARCHAR(12)", "data2");
            columns.add(column_primaryKey);
            columns.add(column_dataColumn);
            tableManager = new TableManager(dbManager, tableName, columns, column_primaryKey);
            tableManager.createRow(columns);
        } catch (Exception ex) {
            Logger.getLogger(TableManagerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test(expected = PrimaryKeyClashException.class)
    public void testCreateRow_duplicateKey() throws InvalidColumnNameException, PrimaryKeyClashException, SQLException, NullColumnValueException {
        System.out.println("TEST: Create row, duplicate key");
        tableManager.createRow(columns);
    }

    @Test(expected = NullColumnValueException.class)

    public void testCreateRow_nullKey() throws InvalidColumnNameException, PrimaryKeyClashException, SQLException, NullColumnValueException {
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
        newArr.add(new Column("Column2", "VARCHAR(12)", "587")); // Data here is different
        newArr.add(new Column("Column5", "VARCHAR(12)", "data1"));
        tableManager.createRow(newArr);
    }

    @After
    public void tearDown() {
        dbManager.closeConnections();
    }
}
