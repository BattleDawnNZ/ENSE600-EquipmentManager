package grp.twentytwo.equipmentmanagerapplication;

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
	ModelManager model = new ModelManager();
	View view = new View();

	//<editor-fold desc="Initializing connections between View and Model and vice versa">
	// Login
	view.login.addListener((ActionEvent e) -> {
	    model.login();
	});
	model.loginSuccessful.addListener((Object newValue) -> {
	    view.loginComplete();
	});
	//<editor-fold desc="Items Tab">----------------------------------------
	// Item Search
	view.searchForItem.addListener((String searchString) -> {
	    view.setItemSearchResults(model.searchForItem(searchString));
	});
	// Item Preview
	view.viewItem.addListener((String itemID) -> {
	    model.getItemPreview(itemID);
	});
	model.itemPreview.addListener((Item itemData) -> {
	    view.setItemPreview(itemData);
	});
	//</editor-fold>
	//<editor-fold desc="Users Tab">----------------------------------------
	// User Search
	view.searchForUser.addListener((String searchString) -> {
	    view.setUserSearchResults(model.searchForUser(searchString));
	});
	// User Preview
	view.viewUser.addListener((String userID) -> {
	    model.getUserPreview(userID);
	});
	model.userPreview.addListener((User userData) -> {
	    view.setUserPreview(userData);
	});
	//</editor-fold>
	//</editor-fold>
	/* Create and display the form */ //	java.awt.EventQueue.invokeLater(new Runnable() {
	//	    public void run() {
	view.setVisible(true);
//	    }
//	});
    }
}
