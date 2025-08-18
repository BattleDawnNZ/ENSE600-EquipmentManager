package equipmentmanagerapplication;

import equipmentmanagerapplication.User.SecurityLevels;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;  // Import the Scanner class

/**
 *
 * @author mymys
 */
public class CliManager {

    private static InputHandler inputHandler = new InputHandler();

    private static CliMenu menu_L1;
    private static CliMenu menu_L2_manageEquipment;
    private static CliMenu menu_L2_manageUsers;
    private static CliMenu menu_L2_itemBookings;
    private static CliMenu menu_L2_itemSearch;

    public enum State {
        LOGIN, EXIT_PROGRAM, LOGOUT, MENU_L1,
        MENU_L2_ManageEquipment, MENU_L2_ManageUsers, MENU_L2_ItemBookings, MENU_L2_ItemSearch,
        ACTION_AddServiceNote, ACTION_FlagCalibration, ACTION_AddItem, ACTION_DeleteItem, ACTION_MoveItemLocation, ACTION_AddNewLocation,
        ACTION_CreateNewUser, ACTION_RemoveUser, ACTION_ViewUserDetails,
        ACTION_BookItem, ACTION_ReturnItem, ACTION_ViewBookings,
        ACTION_SearchItemByCode, ACTION_SearchItemByName, ACTION_SearchItemByType
    }

    static State currentState = State.LOGIN;
    static State currentAction;

    public static void runCli() {

        initialiseMenuDefinitions();

        System.out.println("------- Welcome to Equipment Manager! -------");
        System.out.println("Enter 'x' at any time to exit out of an action. \nReturn to the main menu at any time to exit the program.");

        runCliLoop();

    }

    static void runCliLoop() {

        State newState;
        State nextAction;

        while (true) {
            switch (currentState) {
                case LOGIN:
                    login();
                    currentState = State.MENU_L1;
                    break;
                case EXIT_PROGRAM:
                    return;
                case LOGOUT:
                    UserManager.logout();
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
                    //processAction_itemSearch(nextAction);
                    break;
                default:
                    newState = processMenu(menu_L1); // Handle option selection and save the next menu (state) to move to
                    currentState = newState;
            }
        }
    }

    static void login() {
        try {
            System.out.println("To begin please enter your ID number:");

            if (UserManager.login(inputHandler.getUserInput_userID())) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Sorry, your ID is not recognised. Please try again.");
                login();
            }
        } catch (AbortActionException e) {
            System.out.println("Program Quit");
            currentState = State.EXIT_PROGRAM; // If x is entered for the initial login page, end the program
        }
    }

    /**
     *
     * @param menu
     * @return the next state the system should go into based on the selected
     * option
     */
    private static State processMenu(CliMenu menu) {
        try {
            int chosenOption;

            // Display the main menu (Only the options available for the current users security level)
            menu.dispMenu(UserManager.getActiveUser());

            do { // Prompt for user input until entry is valid
                chosenOption = inputHandler.getUserInput_integer();
            } while (!menu.verifyValidMenuOption(UserManager.getActiveUser(), chosenOption));

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
    private static void processAction_itemBookings(State nextAction) {
        try {
            switch (nextAction) {
                case ACTION_BookItem:
                    if (BookingManager.issueItem(UserManager.getActiveUser().getUserID(), inputHandler.getUserInput_itemID(), ZonedDateTime.now(), inputHandler.getUserInput_date())) {
                        System.out.println("Item booked successfully.");
                    } else { // Item is already booked.
                        System.out.println("You cannot book this item. It is currently booked.");
                    }
                    break; // Implemenet exit the action??? HOW!!
                case ACTION_ReturnItem:
                    if (BookingManager.returnItem(inputHandler.getUserInput_itemID())) {
                        System.out.println("Item returned successfully.");
                    } else {
                        System.out.println("Failed to return the item. Check the item and booking ID are correct.");
                    }
                    break;
                case ACTION_ViewBookings:
                    System.out.println("!!!!!!");
                    ArrayList<Booking> bookings = BookingManager.getBookingsForUser(UserManager.getActiveUser().getUserID());
                    System.out.println(bookings);
                    for (Booking i : bookings) {
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
    private static void processAction_manageUsers(State nextAction) {
        try {
            switch (nextAction) {
                case ACTION_CreateNewUser:
                    System.out.println("Please enter the new users ID number:");
                    String newId = inputHandler.getUserInput_newUserID(); // Get new ID
                    System.out.println("Please enter the users name:");
                    String name = inputHandler.getUserInput_alphabeticString(); // Get users name
                    SecurityLevels level = inputHandler.getUserInput_securityLevel(); // Get security level
                    if (UserManager.createUser(newId, name, level)) {
                        System.out.println("User created successfully.");
                    } else {
                        System.out.println("That user ID is already in the system.");
                    }
                    break;
                case ACTION_RemoveUser:
                    System.out.println("Please enter the ID number of the user you wish to remove from the system:");
                    if (UserManager.removeUser(inputHandler.getUserInput_userID())) {
                        System.out.println("User removed successfully.");
                    } else {
                        System.out.println("Failed to remove the user. Check the ID is valid.");
                    }
                    break;
                case ACTION_ViewUserDetails:
                    System.out.println("Please enter the ID number of the user you wish to see details of:");
                    System.out.println(UserManager.getUserFromID(inputHandler.getUserInput_userID()).toString()); // Get users id and display their details
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
    private static void processAction_manageEquipment(State nextAction) {
        try {
            switch (nextAction) {
                case ACTION_AddServiceNote:
                    System.out.println("Please enter the item ID number:");
                    String id = inputHandler.getUserInput_itemID(); // Get item ID
                    System.out.println("Please enter the service note:");
                    String note = inputHandler.getUserInput_string(); // Get the service note
                    ItemManager.getItemFromID(id).addHistory(note); // Add service note
                    System.out.println("Service note added successfully.");
                    break;
                case ACTION_FlagCalibration:
                    System.out.println("Please enter the item ID number of the item to flag: ");
                    ItemManager.getItemFromID(inputHandler.getUserInput_itemID()).flagForCalibration();
                    System.out.println("Item flagged for calibration successfully.");
                    break;
                case ACTION_AddItem:
                    System.out.println("Please enter the new items name (eg, 'R9000 Universal Laser Cutter') :");
                    String newItemName = inputHandler.getUserInput_string();
                    System.out.println(LocationManager.getAllAvailableLocations()); // Print available locations
                    System.out.println("Please enter the new items location (see valid locations above):");
                    String newItemLocation = inputHandler.getUserInput_location();
                    System.out.println("Please enter the new items type (eg, electrical/measurement/multimeters):"); // !!!
                    String newItemType = inputHandler.getUserInput_alphabeticString();
                    if (ItemManager.addItem(newItemName, newItemLocation, newItemType)) { // OR ITEM ALREADY EXISTS?? CHECK
                        System.out.println("Item added successfully.");
                    } else {
                        System.out.println("Failed to create the item. Check the details are correct."); // OR ITEM ALREADY EXISTS
                    }
                    break;
                case ACTION_DeleteItem:
                    System.out.println("Please enter the item ID number of the item to delete: ");
                    if (ItemManager.removeItem(inputHandler.getUserInput_itemID())) {
                        System.out.println("Item deleted successfully.");
                    } else {
                        System.out.println("Error removing that item. Check the details are correct.");
                    }
                    break;
                case ACTION_MoveItemLocation:
                    System.out.println("Please enter the item ID number of the item you are moving: ");
                    String moveItemID = inputHandler.getUserInput_itemID();
                    System.out.println("The item is currently at " + ItemManager.getItemFromID(moveItemID).getLocation() + ". Enter its new location:");
                    String moveItemLocation = inputHandler.getUserInput_location();
                    LocationManager.moveItem(moveItemID, moveItemLocation);
                    System.out.println("Item location adjusted.");
                    break;
                case ACTION_AddNewLocation:
                    System.out.println("Please enter the name of the locatoin you wish to add:");
                    if (LocationManager.addLocation(inputHandler.getUserInput_string())) {
                        System.out.println("Location added successfully.");
                    } else {
                        System.out.println("Failed to create location. It already exists.");
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
    private static void initialiseMenuDefinitions() { // Defines all CLI menu options

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
        menu_L2_manageEquipment.addMenuOption("Delete Item", State.ACTION_DeleteItem, SecurityLevels.MANAGER);
        menu_L2_manageEquipment.addMenuOption("Move Item Location", State.ACTION_MoveItemLocation, SecurityLevels.MANAGER);
        menu_L2_manageEquipment.addMenuOption("Add new Location", State.ACTION_AddNewLocation, SecurityLevels.MANAGER);

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
/// Search item should be history show
// !!!! Add New Item: Shows item ID
//                    USER MANAGER CHHECK ACTION???
//
/* PLAN
    Manage Equipment
    Manage Users
    !!!!Return to Homescreen ->
    
    Manage Users
            S3, Create New User, Remove User
            
    Search for Item Details (S1)

    Manage Items
        Maintenance: S3, Add Calibration, Add Service Note 
                     S2 Flag Item for Calibraton                 
        Admin:   S3, Add New Item: Shows item ID, Delete Item, Move Item Location, Add new Location
                    
    Book Items
            Issue, return, show my bookings

 */
// Quit Program
//        public States GetSelection() {
//        
//        
//        Scanner scanner = new Scanner(System.in);
//        int choice = scanner.nextInt();
//        return options[choice].state;

// Reciew errors handed when x entered on a menu. Think okay though
//View list of users
//Change security level of user?

// No type' error checking
