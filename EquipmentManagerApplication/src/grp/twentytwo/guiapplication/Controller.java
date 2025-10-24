package grp.twentytwo.guiapplication;

import grp.twentytwo.equipmentmanager.Booking;
import grp.twentytwo.equipmentmanager.Item;
import grp.twentytwo.equipmentmanager.ModelManager;
import grp.twentytwo.equipmentmanager.User;
import java.awt.event.ActionEvent;

/**
 * Base application entry class. Responsible for creating and loading managers,
 * and starting application flow.
 *
 * @author fmw5088
 */
public class Controller {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	View view = new View();
	ModelManager model = new ModelManager();
	// Errors
	model.modelError.addListener((Exception err) -> {
	    view.showError(err);
	});
	model.setupManagers();

	//<editor-fold desc="Initializing connections between View and Model and vice versa">
	//<editor-fold desc="Login Page">
	// Login
	view.login.addListener((ActionEvent e) -> {
	    view.login(model.login(view.getLoginDetails(model.getNewUser())));
	    view.secureUI(model.getActiveUser());
	});
	//</editor-fold>
	//<editor-fold desc="Items Tab">----------------------------------------
	view.getLocations.addListener((ActionEvent e) -> {
	    view.setLocations(model.searchForLocations(""));
	});
	// Item Adding
	view.addItem.addListener((ActionEvent e) -> {
	    Item item = model.getNewItem();
	    view.setNewItemDetails(item);
	    model.AddItem(item);
	});
	// Item Removal	
	view.removeItem.addListener((String itemID) -> {
	    model.removeItem(itemID);
	});
	// Item Edit
	view.getEditDetails.addListener((String itemID) -> {
	    view.setItemEditingPreview(model.getItem(itemID));
	});
	view.editItem.addListener((String itemID) -> {
	    Item item = model.getItem(itemID);
	    view.editItemDetails(item);
	    model.updateItem(item);
	});
	// Item Search
	view.searchForItem.addListener((String searchString) -> {
	    view.setItemSearchResults(model.searchForItems(searchString));
	});
	// Item Preview
	view.viewItem.addListener((String itemID) -> {
	    view.setItemPreview(model.getItem(itemID));
	    view.setItemBookings(model.getBookingsForItem(itemID));
	});
	view.viewBooking.addListener((String bookingID) -> {
	    view.setBookingPreview(model.getBooking(bookingID));
	});
	// Item Booking
	view.bookItem.addListener((String itemID) -> {
	    Booking booking = model.getNewBooking();
	    booking.setItemID(itemID);
	    if (view.setNewBookingDetails(booking)) {
		model.AddBooking(booking);
	    }
	});
	// Item Returning
	view.returnItem.addListener((String bookingID) -> {
	    model.returnItemBooking(bookingID);
	});
	// Item Maintenance
	view.addNote.addListener((String itemID) -> {
	    model.addNote(itemID, view.getNote());
	});
	view.flagItem.addListener((String itemID) -> {
	    model.flagItemForCalibration(itemID);
	});
	view.calibrateItem.addListener((String itemID) -> {
	    model.calibrateItem(itemID);
	});
	view.viewHistory.addListener((String itemID) -> {
	    view.setupViewHistoryDialog(model.getHistoryForItem(itemID));
	});
	//</editor-fold>
	//<editor-fold desc="Users Tab">----------------------------------------
	// User Adding
	view.addUser.addListener((ActionEvent e) -> {
	    User user = model.getNewUser();
	    view.setNewUserDetails(user);
	    model.AddUser(user);
	});
	// User Removal	
	view.removeUser.addListener((String userID) -> {
	    model.RemoveUser(userID);
	});
	// User Search
	view.searchForUser.addListener((String searchString) -> {
	    view.setUserSearchResults(model.searchForUsers(searchString));
	});
	// User Preview
	view.viewUser.addListener((String userID) -> {
	    view.setUserPreview(model.getUser(userID));
	});
	//</editor-fold>
	//<editor-fold desc="Location Tab">----------------------------------------
	// Location Add
	view.addLocation.addListener((ActionEvent e) -> {
	    model.AddLocation(view.getNewLocationDetails());
	});
	// Location Removal	
	view.removeLocation.addListener((String locationID) -> {
	    model.removeLocation(locationID);
	});
	// Location Search
	view.searchForLocation.addListener((String searchString) -> {
	    view.setLocationSearchResults(model.searchForLocations(searchString));
	});
	// Location Preview
	view.viewLocation.addListener((String locationName) -> {
	    view.setLocationPreview(model.getLocation(locationName));
	    view.setLocationItemsPreview(model.getItemsForLocation(locationName));
	});
	//</editor-fold>
	//</editor-fold>
	/* Create and display the form */
	//java.awt.EventQueue.invokeLater(new Runnable() {
	//    public void run() {
	view.setVisible(true);
	//    }
	//});
    }
}
