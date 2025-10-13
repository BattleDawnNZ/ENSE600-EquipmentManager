package grp.twentytwo.equipmentmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages booking and returning of items.
 *
 * @author ppj1707
 */
public class BookingManager {

    private ItemManager itemManager;

    private final DatabaseManager dbManager;
    private final TableManager tableManager;

    // Database table properties
    String tableName = "BOOKINGTABLE";
    HashMap<String, String> columnDefinitions;
    String primaryKey = "BookingID";

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager("pdc", "pdc", "jdbc:derby:EquipmentManagerDB; create=true");
        ItemManager itemManager = new ItemManager();
        BookingManager um = new BookingManager(itemManager, dbManager);
        um.printTable();
        //um.removeUser("000004");
        //um.saveUser(user);
        um.printTable();
        System.out.println(um.getBookingFromID("000001"));
    }

    public BookingManager(ItemManager itemManager, DatabaseManager databaseManager) {
        this.itemManager = itemManager;
        this.dbManager = databaseManager;

        // Define Table Parameters
        columnDefinitions = new HashMap<String, String>();
        columnDefinitions.put("BookingID", "VARCHAR(12) not NULL");
        columnDefinitions.put("UserID", "VARCHAR(12) not NULL");
        columnDefinitions.put("ItemID", "VARCHAR(12) not NULL");
        columnDefinitions.put("BookedDate", "VARCHAR(20) not NULL");
        columnDefinitions.put("ReturnDate", "VARCHAR(20) not NULL");

        // Initialise Table
        tableManager = new TableManager(dbManager, tableName, columnDefinitions, primaryKey);

        // Add test data to table
        dbManager.updateDB("INSERT INTO BOOKINGTABLE VALUES ('1', '000001', '000001', '03-10-2025 14:20', '06-11-2025 15:30')");
    }

    /**
     *
     * @param bookingID The booking ID of the booking requested.
     * @return the booking with the corresponding booking ID.
     */
    public Booking getBookingFromID(String bookingID) {
        ResultSet rs = tableManager.getRowByPrimaryKey(bookingID);
        return getBookingObjectsFromResultSet(rs).getFirst(); // Booking IDs are unique in SQL table therefore only 1 will ever be returned
    }

    /**
     * Creates a new booking.
     *
     * @param userID The booking users ID.
     * @param itemID The ID of the item being booked.
     * @param bookedDate The date of the booking.
     * @param returnDate The return date of the booking.
     * @return true if the booking was valid and created.
     */
    public boolean issueItem(String userID, String itemID, ZonedDateTime bookedDate, ZonedDateTime returnDate) {
        String bookingID = tableManager.getNextPrimaryKeyId();

        // Note. The format must be correct, but this allows bookings that overlap timeslots for the same item. 
        // If this is not desired then it should be checked application-layer.
        HashMap<String, String> data = new HashMap<>();
        data.put("BookingID", bookingID);
        data.put("UserID", userID);
        data.put("ItemID", itemID);
        data.put("BookedDate", bookedDate.format(formatter));
        data.put("ReturnDate", returnDate.format(formatter));
        tableManager.createRow(data);
        return true; // User created

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

        //!!!!!!!!!!!!!!!!!!!!!bookedItem.addHistory("(Booking ID: " + bookingID + ") Returned by " + booking.getUserID());
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
    private ArrayList<Booking> getBookingObjectsFromResultSet(ResultSet resultSet) {
        ArrayList<Booking> bookingList = new ArrayList<Booking>();

        try {

            while (resultSet.next()) { // User exists
                String bookingID = resultSet.getString("BookingID");
                String userID = resultSet.getString("UserID");
                String itemID = resultSet.getString("ItemID");
                ZonedDateTime bookedDate = ZonedDateTime.parse(resultSet.getString("BookedDate"), formatter);
                ZonedDateTime returnDate = ZonedDateTime.parse(resultSet.getString("ReturnDate"), formatter);
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
    public boolean verifyBookingOwner(String bookingID, String userID) {
        ResultSet rs = tableManager.getRowByPrimaryKey(bookingID);
        Booking booking = getBookingObjectsFromResultSet(rs).getFirst();
        return (booking != null && booking.isOwnedBy(userID));
    }

//    /**
//     *
//     * @return An ArrayList of the bookings.
//     */
//    public ArrayList<Booking> getBookings() {
//        return (ArrayList<Booking>) bookings.values();
//    }
    /**
     *
     * @param itemID
     * @return All bookings for the item.
     */
    public ArrayList<Booking> getBookingsForItem(String itemID) {
        ResultSet rs = tableManager.getRowByColumnValue("ItemID", itemID);
        return getBookingObjectsFromResultSet(rs);
    }

    /**
     *
     * @param userID
     * @return All bookings for the user.
     */
    public ArrayList<Booking> getBookingsForUser(String userID) {
        ResultSet rs = tableManager.getRowByColumnValue("UserID", userID);
        return getBookingObjectsFromResultSet(rs);
    }

}
