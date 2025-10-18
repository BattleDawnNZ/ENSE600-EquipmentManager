package grp.twentytwo.equipmentmanager;

import grp.twentytwo.database.DatabaseManager;
import grp.twentytwo.database.Column;
import grp.twentytwo.database.TableManager;
import grp.twentytwo.database.InvalidColumnNameException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages booking and returning of items.
 *
 * @author ppj1707
 */
public class BookingManager {
    //////// MUST CHECK ITEM ID IS VALID

    private final DatabaseManager dbManager;
    private final TableManager tableManager;

    // Database table properties
    String tableName = "BOOKINGTABLE";
    private final ArrayList<Column> columnData;

    private Column column_bookingID = new Column("BookingID", "VARCHAR(12) not NULL", "");
    private Column column_userID = new Column("UserID", "VARCHAR(12) not NULL", "");
    private Column column_itemID = new Column("ItemID", "VARCHAR(12) not NULL", "");
    private Column column_bookedDate = new Column("BookedDate", "VARCHAR(20) not NULL", "");
    private Column column_returnDate = new Column("ReturnDate", "VARCHAR(20) not NULL", "");

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
        LocationManager lManager = new LocationManager(dbManager);
        ItemManager itemManager = new ItemManager(dbManager, lManager);
        BookingManager um = new BookingManager(itemManager, dbManager);
        um.printTable();
        //um.removeUser("000004");
        //um.saveUser(user);
        um.printTable();
//        System.out.println(um.getBookingFromID("000001"));
//        System.out.println(um.getBookingsForItem("000001"));
    }

    public BookingManager(ItemManager itemManager, DatabaseManager databaseManager) {
        this.dbManager = databaseManager;

        // Define table parameters
        columnData = new ArrayList<>();
        columnData.add(column_bookingID);
        columnData.add(column_userID);
        columnData.add(column_itemID);
        columnData.add(column_bookedDate);
        columnData.add(column_returnDate);

        // Initialise Table
        tableManager = new TableManager(dbManager, tableName, columnData, column_bookingID);

        // Add test data to table
        dbManager.updateDB("INSERT INTO BOOKINGTABLE VALUES ('1', '000001', '000001', '03-10-2025 14:20', '06-11-2025 15:30')");
    }

    /**
     *
     * @param bookingID The booking ID of the booking requested.
     * @return the booking with the corresponding booking ID.
     */
    public Booking getBookingFromID(String bookingID) throws InvalidBookingRangeException {
        try {
            ResultSet rs = tableManager.getRowByPrimaryKey(bookingID);
            return getBookingObjectsFromResultSet(rs).getFirst(); // Booking IDs are unique in SQL table therefore only 1 will ever be returned
        } catch (NoSuchElementException e) {
            System.out.println("InvalidID");
            return null;
        }
    }

    /**
     * Creates a new booking.
     *
     * @param booking
     * @return true if the booking was valid, did not clash with other bookings
     * and was created.
     */
    public boolean issueItem(Booking booking) throws InvalidBookingRangeException {
        ArrayList<Booking> itemBookings = getBookingsForItem(booking.getItemID());
        for (Booking b : itemBookings) { // Checking booking does not clash with other bookings
            if (booking.overlaps(b)) {
                return false;
            }
        }
        column_bookingID.data = tableManager.getNextPrimaryKeyId();
        column_userID.data = booking.getUserID();
        column_itemID.data = booking.getItemID();
        column_bookedDate.data = booking.getBookedDate().format(formatter);
        column_returnDate.data = booking.getReturnDate().format(formatter);

        try {
            tableManager.createRow(columnData);
            return true; // User created
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(BookingManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        //HISTORY ADDitemManagergetItemFromID(itemID).addHistory("(Booking ID: " + bookingID + ") Booked by " + userID + ", from " + bookedDate.format(formatter) + " to " + returnDate.format(formatter));
    }

    /**
     * Remove a booking.
     *
     * @param bookingID The booking ID fo the booking to remove.
     * @return True if the booking was successfully completed. i.e. item and
     * booking both actually existed.
     */
    public boolean returnItem(String bookingID) {
        return tableManager.deleteRowByPrimaryKey(bookingID);
    }

    /**
     * Print the entire user table to the console
     */
    public void printTable() {
        tableManager.printTable();
    }

    /**
     *
     * @param resultSet
     * @return a user object
     */
    private ArrayList<Booking> getBookingObjectsFromResultSet(ResultSet resultSet) throws InvalidBookingRangeException {
        ArrayList<Booking> bookingList = new ArrayList<Booking>();

        try {

            while (resultSet.next()) { // User exists
                String bookingID = resultSet.getString("BookingID");
                String userID = resultSet.getString("UserID");
                String itemID = resultSet.getString("ItemID");
                System.out.println(resultSet.getString("BookedDate"));
                LocalDateTime bookedDate = LocalDateTime.parse(resultSet.getString("BookedDate"), formatter);
                LocalDateTime returnDate = LocalDateTime.parse(resultSet.getString("ReturnDate"), formatter);

                bookingList.add(new Booking(bookingID, userID, itemID, bookedDate, returnDate));
            }
            return bookingList;

        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     *
     * @param bookingID The bookings ID
     * @param userID The User to check ownership
     * @return true if the booking exists and is owned by the User.
     */
    public boolean verifyBookingOwner(String bookingID, String userID) throws InvalidBookingRangeException {
        try {
            ResultSet rs = tableManager.getRowByPrimaryKey(bookingID);
            Booking booking = getBookingObjectsFromResultSet(rs).getFirst();
            return (booking != null && booking.isOwnedBy(userID));
        } catch (NoSuchElementException e) {
            System.out.println("InvalidID");
            return false;
        }
    }

    /**
     *
     * @param itemID
     * @return All bookings for the item.
     */
    public ArrayList<Booking> getBookingsForItem(String itemID) throws InvalidBookingRangeException {
        ResultSet rs;
        try {
            rs = tableManager.getRowByColumnValue("ItemID", itemID);
            return getBookingObjectsFromResultSet(rs);
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(BookingManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     *
     * @param userID
     * @return All bookings for the user.
     */
    public ArrayList<Booking> getBookingsForUser(String userID) throws InvalidBookingRangeException {
        ResultSet rs;
        try {
            rs = tableManager.getRowByColumnValue("UserID", userID);
            return getBookingObjectsFromResultSet(rs);
        } catch (InvalidColumnNameException ex) {
            Logger.getLogger(BookingManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
