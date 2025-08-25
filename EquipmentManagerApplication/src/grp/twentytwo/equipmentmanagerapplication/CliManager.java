package grp.twentytwo.equipmentmanagerapplication;

import grp.twentytwo.equipmentmanager.UserManager;
import grp.twentytwo.equipmentmanager.ItemManager;
import grp.twentytwo.equipmentmanager.Booking;
import grp.twentytwo.equipmentmanager.LocationManager;
import grp.twentytwo.equipmentmanager.BookingManager;
import grp.twentytwo.equipmentmanager.MaintenanceManager;
import grp.twentytwo.equipmentmanager.Item;
import grp.twentytwo.equipmentmanager.User.SecurityLevels;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Manages the Command Line User Interface, allowing a user to access the
 * equipment manager.
 *
 * @author mymys
 */
public class CliManager {

    private ItemManager itemManager;
    private UserManager userManager;
    private BookingManager bookingManager;
    private LocationManager locationManager;
    private MaintenanceManager maintenanceManager;

    private InputHandler inputHandler;

    private CliMenu menu_L1;
    private CliMenu menu_L2_manageEquipment;
    private CliMenu menu_L2_manageUsers;
    private CliMenu menu_L2_itemBookings;
    private CliMenu menu_L2_itemSearch;

    public enum State {
	LOGIN, EXIT_PROGRAM, LOGOUT, MENU_L1,
	MENU_L2_ManageEquipment, MENU_L2_ManageUsers, MENU_L2_ItemBookings, MENU_L2_ItemSearch,
	ACTION_AddServiceNote, ACTION_FlagCalibration, ACTION_AddItem, ACTION_DeleteItem, ACTION_MoveItemLocation, ACTION_AddNewLocation, ACTION_CalibrateItem,
	ACTION_CreateNewUser, ACTION_RemoveUser, ACTION_ViewUserDetails,
	ACTION_BookItem, ACTION_ReturnItem, ACTION_ViewBookings,
	ACTION_SearchItemByCode, ACTION_SearchItemByName, ACTION_SearchItemByType
    }

    State currentState = State.LOGIN;

    public CliManager(ItemManager itemManager, UserManager userManager, BookingManager bookingManager, LocationManager locationManager, MaintenanceManager maintenanceManager) {
	this.itemManager = itemManager;
	this.userManager = userManager;
	this.bookingManager = bookingManager;
	this.locationManager = locationManager;
	this.maintenanceManager = maintenanceManager;
	this.inputHandler = new InputHandler(itemManager, userManager, bookingManager, locationManager, maintenanceManager);
    }

    public void runCli() {

	initialiseMenuDefinitions();

	System.out.println("------- Welcome to Equipment Manager! -------");
	System.out.println("Enter 'x' at any time to exit out of an action. \nReturn to the main menu at any time to exit the program.");

	runCliLoop();

    }

    private void runCliLoop() {

	State newState;
	State nextAction;

	while (true) {
	    switch (currentState) {
		case LOGIN:
		    login();
		    break;
		case EXIT_PROGRAM:
		    System.out.println("Program Quit");
		    return;
		case LOGOUT:
		    userManager.logout();
		    System.out.println("You have logged out.");
		    currentState = State.LOGIN;
		    break;
		case MENU_L1:
		    newState = processMenu(menu_L1); // Handle option selection and save the next menu (state) to move to
		    currentState = newState;
		    break;
		case MENU_L2_ManageEquipment:
		    nextAction = processMenu(menu_L2_manageEquipment); // Handle option selection and save the selected next action (state) to move to
		    processAction_manageEquipment(nextAction);
		    break;
		case MENU_L2_ManageUsers:
		    nextAction = processMenu(menu_L2_manageUsers); // Handle option selection and save the selected next action (state) to move to
		    processAction_manageUsers(nextAction);
		    break;
		case MENU_L2_ItemBookings:
		    nextAction = processMenu(menu_L2_itemBookings); // Handle option selection and save the selected next action (state) to move to
		    processAction_itemBookings(nextAction);
		    break;
		case MENU_L2_ItemSearch:
		    nextAction = processMenu(menu_L2_itemSearch); // Handle option selection and save the selected next action (state) to move to
		    processAction_itemSearch(nextAction);
		    break;
		default:
		    newState = processMenu(menu_L1); // Handle option selection and save the next menu (state) to move to
		    currentState = newState;
	    }
	}
    }

    /**
     * Handle user login
     */
    private void login() {
	try {
	    System.out.println("To begin please enter your ID number (or enter x to quit the program):");

	    if (userManager.login(inputHandler.getUserInput_userID())) {
		System.out.println("Login successful!");
		currentState = State.MENU_L1;
	    } else {
		System.out.println("Sorry, your ID is not recognised. Please try again.");
		login();
	    }
	} catch (AbortActionException e) {
	    currentState = State.EXIT_PROGRAM; // If x is entered for the initial login page, end the program
	}
    }

    /**
     *
     * @param menu
     * @return the next state the system should go into based on the selected
     * option
     */
    private State processMenu(CliMenu menu) {
	try {
	    int chosenOption;

	    // Display the main menu (Only the options available for the current users security level)
	    menu.dispMenu(userManager.getActiveUser());

	    // Prompt for user input until entry is valid
	    chosenOption = inputHandler.getUserInput_menuOption(menu, userManager.getActiveUser());

	    return menu.getMenuOptionState(chosenOption);
	} catch (AbortActionException e) { // User has aborted the active action
	    System.out.println(e.getMessage());
	    return processMenu(menu);
	}
    }

    /**
     * Processes a chosen action to do with item bookings
     *
     * @param nextAction
     */
    private void processAction_itemBookings(State nextAction) {
	try {
	    switch (nextAction) {
		case ACTION_BookItem:
		    System.out.println("Please enter the item ID of the item you wish to book:");
		    String bookItemID = inputHandler.getUserInput_itemID();
		    ZonedDateTime bookReturnDate = inputHandler.getUserInput_date();
		    if (bookingManager.issueItem(userManager.getActiveUser().getUserID(), bookItemID, ZonedDateTime.now(), bookReturnDate)) {
			System.out.println("Item booked successfully.");
		    } else { // Item is already booked.
			System.out.println("You cannot book this item. It is currently booked.");
		    }
		    break;
		case ACTION_ReturnItem:
		    ArrayList<Booking> bookings = bookingManager.getBookingsForUser(userManager.getActiveUser().getUserID());
		    System.out.println("You have " + bookings.size() + " active bookings.");
		    for (Booking i : bookings) {
			System.out.println(i.toString());
		    }
		    System.out.println("Please enter the booking ID for the item you are returning:");

		    if (bookingManager.returnItem(inputHandler.getUserInput_bookingID())) {
			System.out.println("Item returned successfully.");
		    } else {
			System.out.println("Failed to return the item. Check the item and booking ID are correct.");
		    }
		    break;
		case ACTION_ViewBookings:
		    ArrayList<Booking> bookingList = bookingManager.getBookingsForUser(userManager.getActiveUser().getUserID());
		    System.out.println("You have " + bookingList.size() + " active bookings.");
		    for (Booking i : bookingList) {
			System.out.println(i.toString());
		    }
		    break;
		default:
		    currentState = State.MENU_L1; // Return to main menu
		    break;
	    }
	} catch (AbortActionException e) { // User has aborted the active action
	    System.out.println(e.getMessage());
	}
    }

    /**
     * Processes a chosen action to do with user management
     *
     * @param nextAction
     */
    private void processAction_manageUsers(State nextAction) {
	try {
	    switch (nextAction) {
		case ACTION_CreateNewUser:
		    System.out.println("Please enter the new users ID number:");
		    String newId = inputHandler.getUserInput_newUserID(); // Get new ID
		    System.out.println("Please enter the users name:");
		    String name = inputHandler.getUserInput_alphabeticString(); // Get users name
		    SecurityLevels level = inputHandler.getUserInput_securityLevel(); // Get security level
		    if (userManager.createUser(newId, name, level)) {
			System.out.println("User created successfully.");
		    } else {
			System.out.println("That user ID is already in the system.");
		    }
		    break;
		case ACTION_RemoveUser:
		    System.out.println("Please enter the ID number of the user you wish to remove from the system:");
		    if (userManager.removeUser(inputHandler.getUserInput_userID())) {
			System.out.println("User removed successfully.");
		    } else {
			System.out.println("Failed to remove the user. Check the ID is valid.");
		    }
		    break;
		case ACTION_ViewUserDetails:
		    System.out.println("Please enter the ID number of the user you wish to see details of:");
		    System.out.println(userManager.getUserFromID(inputHandler.getUserInput_userID()).toString()); // Get users id and display their details
		    break;
		default:
		    currentState = State.MENU_L1; // Return to main menu
		    break;
	    }
	} catch (AbortActionException e) { // User has aborted the active action
	    System.out.println(e.getMessage());
	}
    }

    /**
     * Processes a chosen action to do with equipment management
     *
     * @param nextAction
     */
    private void processAction_manageEquipment(State nextAction) {
	try {
	    switch (nextAction) {
		case ACTION_AddServiceNote:
		    System.out.println("Please enter the item ID number:");
		    String id = inputHandler.getUserInput_itemID(); // Get item ID
		    System.out.println("Please enter the service note:");
		    String note = inputHandler.getUserInput_string(); // Get the service note
		    maintenanceManager.addServiceNote(id, note);
		    System.out.println("Service note added successfully.");
		    break;
		case ACTION_FlagCalibration:
		    System.out.println("Please enter the item ID number of the item to flag: ");
		    maintenanceManager.flagItemForCalibration(inputHandler.getUserInput_itemID());
		    System.out.println("Item flagged for calibration successfully.");
		    break;
		case ACTION_AddItem:
		    System.out.println("Please enter the new items name (eg, 'R9000 Universal Laser Cutter') :");
		    String newItemName = inputHandler.getUserInput_string();
		    System.out.println("Locations: " + locationManager.getLocations()); // Print available locations
		    System.out.println("Please enter the new items location (see valid locations above):");
		    String newItemLocation = inputHandler.getUserInput_location();
		    System.out.println("Please enter the new items type (eg, electrical/measurement/multimeters):"); // !!!
		    String newItemType = inputHandler.getUserInput_string();
		    String newItemID = itemManager.addItem(newItemName, newItemLocation, newItemType);
		    if (newItemID == null) { // 
			System.out.println("Failed to create the item. Check the location is valid.");
		    } else {
			System.out.println("Item added successfully. Item ID: " + newItemID);
		    }
		    break;
		case ACTION_DeleteItem:
		    System.out.println("Please enter the item ID number of the item to delete: ");
		    if (itemManager.removeItem(inputHandler.getUserInput_itemID())) {
			System.out.println("Item deleted successfully.");
		    } else {
			System.out.println("Error removing that item. Check the details are correct.");
		    }
		    break;
		case ACTION_MoveItemLocation:
		    System.out.println("Please enter the item ID number of the item you are moving: ");
		    String moveItemID = inputHandler.getUserInput_itemID();
		    System.out.println("Locations: " + locationManager.getLocations()); // Print available locations
		    System.out.println("The item is currently at " + itemManager.getItemFromID(moveItemID).getLocation() + ". Enter its new location:");
		    String moveItemLocation = inputHandler.getUserInput_location();
		    locationManager.moveItem(moveItemID, moveItemLocation);
		    System.out.println("Item location adjusted.");
		    break;
		case ACTION_AddNewLocation:
		    System.out.println("Please enter the name of the location you wish to add:");
		    if (locationManager.addLocation(inputHandler.getUserInput_string())) {
			System.out.println("Location added successfully.");
		    } else {
			System.out.println("Failed to create location. It already exists.");
		    }
		    break;

		case ACTION_CalibrateItem:
		    System.out.println("Enter the item ID to record its calibration:");
		    if (maintenanceManager.calibrateItem(inputHandler.getUserInput_itemID())) {
			System.out.println("Calibration recorded.");
		    } else {
			System.out.println("Failed to record calibration.");
		    }
		    break;
		default:
		    currentState = State.MENU_L1; // Return to main menu
		    break;
	    }
	} catch (AbortActionException e) { // User has aborted the active action
	    System.out.println(e.getMessage());
	}
    }

    /**
     * Processes a chosen action to do with user management
     *
     * @param nextAction
     */
    private void processAction_itemSearch(State nextAction) {
	ArrayList<Item> itemList;
	try {
	    switch (nextAction) {
		case ACTION_SearchItemByCode:
		    System.out.println("Please enter item ID number or partial ID:");
		    String id = inputHandler.getUserInput_string(); // Get item ID
		    itemList = itemManager.getItemsFromID(id);
		    System.out.println("Items found: " + itemList.size());
		    for (Item item : itemList) {
			System.out.println(item.toString());
		    }
		    break;
		case ACTION_SearchItemByName:
		    System.out.println("Please enter item name number or partial name:");
		    String name = inputHandler.getUserInput_string(); // Get item name
		    itemList = itemManager.getItemsFromName(name);
		    System.out.println("Items found: " + itemList.size());
		    for (Item item : itemList) {
			System.out.println(item.toString());
		    }
		    break;
		case ACTION_SearchItemByType:
		    System.out.println("Please enter item type or a any type keyword (eg, 'electrical'):");
		    String type = inputHandler.getUserInput_string(); // Get item type
		    itemList = itemManager.getItemsFromType(type);
		    System.out.println("Items found: " + itemList.size());
		    for (Item item : itemList) {
			System.out.println(item.toString());
		    }
		    break;
		default:
		    currentState = State.MENU_L1; // Return to main menu
		    break;
	    }
	} catch (AbortActionException e) { // User has aborted the active action
	    System.out.println(e.getMessage());
	}
    }

    /**
     * Defines all Menus
     */
    private void initialiseMenuDefinitions() { // Defines all CLI menu options

	// The state variable correcsponds to the state that should be moved into if the option is selected
	menu_L1 = new CliMenu("MAIN MENU");
	menu_L1.addMenuOption("Exit Program", State.EXIT_PROGRAM, SecurityLevels.GUEST);
	menu_L1.addMenuOption("Logout", State.LOGOUT, SecurityLevels.GUEST);
	menu_L1.addMenuOption("Item Search", State.MENU_L2_ItemSearch, SecurityLevels.GUEST);
	menu_L1.addMenuOption("Item Bookings", State.MENU_L2_ItemBookings, SecurityLevels.EMPLOYEE);
	menu_L1.addMenuOption("Manage Users", State.MENU_L2_ManageUsers, SecurityLevels.MANAGER);
	menu_L1.addMenuOption("Manage Equipment", State.MENU_L2_ManageEquipment, SecurityLevels.MANAGER);

	menu_L2_manageEquipment = new CliMenu("MANAGE EQUIPMENT MENU");
	menu_L2_manageEquipment.addMenuOption("Return to Homescreen ->", State.MENU_L1, SecurityLevels.GUEST);
	menu_L2_manageEquipment.addMenuOption("Add Service Note", State.ACTION_AddServiceNote, SecurityLevels.MANAGER);
	menu_L2_manageEquipment.addMenuOption("Flag Item for Calibraton", State.ACTION_FlagCalibration, SecurityLevels.MANAGER);
	menu_L2_manageEquipment.addMenuOption("Add Item", State.ACTION_AddItem, SecurityLevels.MANAGER);
	menu_L2_manageEquipment.addMenuOption("Delete Item", State.ACTION_DeleteItem, SecurityLevels.MANAGER);
	menu_L2_manageEquipment.addMenuOption("Move Item Location", State.ACTION_MoveItemLocation, SecurityLevels.MANAGER);
	menu_L2_manageEquipment.addMenuOption("Add new Location", State.ACTION_AddNewLocation, SecurityLevels.MANAGER);
	menu_L2_manageEquipment.addMenuOption("Calibrate Item", State.ACTION_CalibrateItem, SecurityLevels.MANAGER);

	menu_L2_manageUsers = new CliMenu("MANAGE USERS MENU");
	menu_L2_manageUsers.addMenuOption("Return to Homescreen ->", State.MENU_L1, SecurityLevels.GUEST);
	menu_L2_manageUsers.addMenuOption("Create New User", State.ACTION_CreateNewUser, SecurityLevels.MANAGER);
	menu_L2_manageUsers.addMenuOption("Remove User", State.ACTION_RemoveUser, SecurityLevels.MANAGER);
	menu_L2_manageUsers.addMenuOption("View User Details", State.ACTION_ViewUserDetails, SecurityLevels.MANAGER);

	menu_L2_itemBookings = new CliMenu("ITEM BOOKINGS MENU");
	menu_L2_itemBookings.addMenuOption("Return to Homescreen ->", State.MENU_L1, SecurityLevels.GUEST);
	menu_L2_itemBookings.addMenuOption("Book Item", State.ACTION_BookItem, SecurityLevels.EMPLOYEE);
	menu_L2_itemBookings.addMenuOption("Return Item", State.ACTION_ReturnItem, SecurityLevels.EMPLOYEE);
	menu_L2_itemBookings.addMenuOption("View my Bookings", State.ACTION_ViewBookings, SecurityLevels.EMPLOYEE);

	menu_L2_itemSearch = new CliMenu("ITEM SEARCH MENU");
	menu_L2_itemSearch.addMenuOption("Return to Homescreen ->", State.MENU_L1, SecurityLevels.GUEST);
	menu_L2_itemSearch.addMenuOption("Search by Item Code", State.ACTION_SearchItemByCode, SecurityLevels.GUEST);
	menu_L2_itemSearch.addMenuOption("Search by Item Name", State.ACTION_SearchItemByName, SecurityLevels.GUEST);
	menu_L2_itemSearch.addMenuOption("Search by Item Type", State.ACTION_SearchItemByType, SecurityLevels.GUEST);

    }

}
