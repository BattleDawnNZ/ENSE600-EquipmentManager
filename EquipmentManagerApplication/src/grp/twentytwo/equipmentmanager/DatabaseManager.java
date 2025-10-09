package grp.twentytwo.equipmentmanager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ppj1707
 */
public class DatabaseManager {

    private final String USERNAME; // DB username
    private final String PASSWORD; //DB password
    private final String URL;  // url of the DB host (add ;create=true to create db)   "jdbc:derby://localhost:1527/BookStoreDB"
    Connection conn;
    Statement batchStatement = null;

    public DatabaseManager(String username, String password, String url) {
        this.URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;
        establishConnection();
    }

    // TEST CODE FOR DB CONNECTION
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:UserDB; create=true");
        System.out.println(dbManager.getConnection());
    }

    public Connection getConnection() {
        return this.conn;
    }

    //Establish connection
    public void establishConnection() {
        if (this.conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                batchStatement = conn.createStatement();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void closeConnections() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public ResultSet queryDB(String sql) {

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

    public void updateDB(String sql) {

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

    public void addToBatch(String sql) {
        try {
            batchStatement.addBatch(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void executeBatch() {
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

    public boolean dropTable(String table) {
        if (checkTableExists(table)) {
            updateDB("DROP TABLE " + table);
        }
        if (!checkTableExists(table)) {
            return true;
        }
        return false;
    }

}
