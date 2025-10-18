package grp.twentytwo.equipmentmanagerapplication;

import grp.twentytwo.equipmentmanager.Booking;
import grp.twentytwo.equipmentmanager.Item;
import grp.twentytwo.equipmentmanager.ModelManager;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

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
	});
	//</editor-fold>
	//<editor-fold desc="Items Tab">----------------------------------------
	// Item Add
	view.addItem.addListener((ActionEvent e) -> {
	    Item item = model.getNewItem();
	    view.setNewItemDetails(item);
	    model.AddItem(item);
	});
	// Item Edit
	view.editItem.addListener((String itemID) -> {
	    view.setItemEditingPreview(model.getItem(itemID));
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
	// Item Book
	view.bookItem.addListener((String itemID) -> {
	    Booking booking = model.getNewBooking();
	    booking.setItemID(itemID);
	    view.setNewBookingDetails(booking);
	    model.AddBooking(booking);
	});
	//</editor-fold>
	//<editor-fold desc="Users Tab">----------------------------------------
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
	// Location Search
	view.searchForLocation.addListener((String searchString) -> {
	    view.setLocationSearchResults(model.searchForLocations(searchString));
	});
	// Location Preview
	view.viewLocation.addListener((String locationName) -> {
	    view.setLocationPreview(model.getLocation(locationName));
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
