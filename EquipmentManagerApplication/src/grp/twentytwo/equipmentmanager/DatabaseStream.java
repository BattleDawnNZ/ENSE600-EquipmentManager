package grp.twentytwo.equipmentmanager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 *
 * @author ppj1707
 */
public class DatabaseStream {

    private final DatabaseManager dbManager;
    private final Connection conn;
    //PreparedStatement stCreateTable;

    public DatabaseStream() {
        dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
        conn = dbManager.getConnection();
        System.out.println(conn);
        //stCreateTable = conn.prepareStatement("A");

        // Initialise Tables ()
        createUserTableIfNotExist();

    }

    public static void main(String[] args) {
        DatabaseStream db = new DatabaseStream();
        db.printUserTableFromDB();
        db.verifyID("000001");
    }

    public void createUserTableIfNotExist() {
        if (!dbManager.checkTableExists("USERTABLE")) {
            // Create table. Assign user id as primary key to prevent duplicates
            System.out.println("USER table does not exist. Creating table");
            dbManager.updateDB("CREATE TABLE USERTABLE (UserID VARCHAR(12) not NULL, Name VARCHAR(30), SecurityLevel VARCHAR(15), PRIMARY KEY (UserID))");
            // Add test data to table
            dbManager.updateDB("INSERT INTO USERTABLE VALUES ('000001', 'Bob', 'Manager'), "
                    + "('000002', 'Sally', 'Employee'), "
                    + "('000003', 'Fred', 'Guest')");
        }
    }

//    public ResultSet getWeekSpecial() {
//        if (!dbManager.checkTableExists("BOOK") && !dbManager.checkTableExists("PROMOTION")) {
//            System.out.println("Promotion table and / or book table does not exist. Please create these first.");
//            return null;
//        } else {
//            ResultSet rs = dbManager.queryDB("SELECT TITLE, PRICE, DISCOUNT FROM BOOK, PROMOTION WHERE BOOK.CATEGORY=PROMOTION.CATEGORY");
//            return (rs);
//        }
//    }
//
//
    public void printUserTableFromDB() {
        try {
            System.out.println("User Table Data:");
            ResultSet rs = dbManager.queryDB("SELECT * FROM USERTABLE");
            while (rs.next()) {
                System.out.println("ID: " + rs.getString("UserID") + "      Name: " + rs.getString("Name") + "      SecurityLevel" + rs.getString("SecurityLevel"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

    }

    public boolean verifyID(String id) {
        String sql = "SELECT * FROM usertable where UserID = ?";
        try {
            PreparedStatement stmt;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "000001");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("securitylevel"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }

        return true;
    }

}
