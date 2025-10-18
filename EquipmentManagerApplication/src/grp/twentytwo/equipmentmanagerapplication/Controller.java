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
	View view = new View();
	ModelManager model = new ModelManager();
	// Errors
	model.modelError.addListener((Exception err) -> {
	    view.showError(err);
	});
	model.SetupManagers();

	//<editor-fold desc="Initializing connections between View and Model and vice versa">
	//<editor-fold desc="Login Page">
	// Login
	view.login.addListener((ActionEvent e) -> {
	    view.login(model.login());
	});
	//</editor-fold>
	//<editor-fold desc="Items Tab">----------------------------------------
	// Item Search
	view.searchForItem.addListener((String searchString) -> {
	    view.setItemSearchResults(model.searchForItems(searchString));
	});
	// Item Preview
	view.viewItem.addListener((String itemID) -> {
	    view.setItemPreview(model.getItem(itemID));
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
	//</editor-fold>
	/* Create and display the form */
	//java.awt.EventQueue.invokeLater(new Runnable() {
	//    public void run() {
	view.setVisible(true);
	//    }
	//});
    }
}
