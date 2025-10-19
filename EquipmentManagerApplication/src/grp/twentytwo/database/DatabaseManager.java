package grp.twentytwo.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
 *
 * @author ppj1707
 */
public class DatabaseManager {

    private final String USERNAME; // DB username
    private final String PASSWORD; //DB password
    private final String URL;  // url of the DB host (add ;create=true to create db)   "jdbc:derby://localhost:1527/BookStoreDB"
    private Connection conn;
    private Statement batchStatement = null;

    public DatabaseManager(String username, String password, String url) throws DatabaseConnectionException {
        this.URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;
        establishConnection();
    }

    // TEST CODE FOR DB CONNECTION
    public static void main(String[] args) {
        //DatabaseManager dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:UserDB; drop=true");
        //System.out.println(dbManager.getConnection());
        //dbManager.updateDB("DROP DATABASE UserDB");

    }

    Connection getConnection() {
        return this.conn;
    }

    //Establish connection
    private void establishConnection() throws DatabaseConnectionException {
        if (this.conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                if (conn == null) {
                    throw new DatabaseConnectionException();
                }
                batchStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    ResultSet queryDB(String sql) {

        Connection connection = this.conn;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return resultSet;
    }

    void updateDB(String sql) {

        Connection connection = this.conn;
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException ex) {
            // Check if the exception is due to a primary (unique) key violation
            if (ex.getSQLState().startsWith("23505")) { // Duplicate entry error
                System.out.println("Entry already exists, skipping update command: " + sql);
            } else {
                System.out.println(ex.getMessage());
            }
        }
    }

    void addToBatch(String sql) {
        try {
            batchStatement.addBatch(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    void executeBatch() {
        try {
            batchStatement.executeBatch();

        } catch (SQLException ex) {
            while (ex != null) { // Check if the exception is due to a primary (unique) key violation
                System.out.println(ex.getMessage());
                ex = ex.getNextException();
            }

        }
    }

    public boolean checkTableExists(String table) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, table, null); // Column 3 contains the table name
            if (tables.next()) {
                return true; // Table exists
            } else {
                return false; // Table does not exist
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean dropTableIfExists(String tableName) { // USED FOR TESTING PURPOSES
        try {
            PreparedStatement st_dropTable = conn.prepareStatement("DROP TABLE " + tableName);
            if (checkTableExists(tableName)) {
                System.out.println(tableName + " table exists. Dropping table.");
                // Create table. Assign primary key to prevent duplicates
                updateDB("DROP TABLE " + tableName);
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
        return false;

    }

}
