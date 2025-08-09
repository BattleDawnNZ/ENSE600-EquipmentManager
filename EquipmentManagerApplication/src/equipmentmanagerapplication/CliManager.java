package equipmentmanagerapplication;

import equipmentmanagerapplication.User.SecurityLevels;
import java.util.Scanner;  // Import the Scanner class

/**
 *
 * @author mymys
 */
public class CliManager {

    static Scanner scan = new Scanner(System.in);
    static UserManager userManager = new UserManager();

    private static CliMenu menu_L1;
    private static CliMenu menu_L2_manageEquipment;
    private static CliMenu menu_L2_manageUsers;
    private static CliMenu menu_L2_itemBookings;
    private static CliMenu menu_L2_itemSearch;

    public enum State {
        LOGIN, LOGOUT, MENU_L1,
        MENU_L2_ManageEquipment, MENU_L2_ManageUsers, MENU_L2_ItemBookings, MENU_L2_ItemSearch,
        ACTION_AddServiceNote, ACTION_FlagCalibration, ACTION_AddItem, ACTION_DeleteItem, ACTION_MoveItemLocation, ACTION_AddNewLocation,
        ACTION_CreateNewUser, ACTION_RemoveUser, ACTION_ViewUserDetails,
        ACTION_BookItem, ACTION_ReturnItem, ACTION_ViewBookings,
        ACTION_SearchItemByCode, ACTION_SearchItemByName, ACTION_SearchItemByType
    }

    static State currentState = State.LOGIN;

    public static void runCli() {

        initialiseMenuDefinitions();

        State newState;

        System.out.println("Welcome to Equipment Manager!");

        while (true) {
            switch (currentState) {
                case LOGIN:
                    login();
                    currentState = State.MENU_L1;
                    break;
                case MENU_L1:
                    newState = processMenu(menu_L1);
                    currentState = newState;
                    break;
                case MENU_L2_ManageEquipment:
                    newState = processMenu(menu_L2_manageEquipment);
                    currentState = newState;
                    break;
                case MENU_L2_ManageUsers:
                    newState = processMenu(menu_L2_manageUsers);
                    currentState = newState;
                    break;
                case MENU_L2_ItemBookings:
                    newState = processMenu(menu_L2_itemBookings);
                    currentState = newState;
                    break;
                case MENU_L2_ItemSearch:
                    newState = processMenu(menu_L2_itemSearch);
                    currentState = newState;
                    break;
            }
        }
    }

    static void login() {

        System.out.println("To begin please enter your ID number:");

        if (userManager.login(scan.nextLine().trim())) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Sorry, your ID is not recognised. Please try again.");
            login();
        }
    }

    private static State processMenu(CliMenu menu) {

        int chosenOption;

        // Display the main menu (Only the options available for the current users security level)
        menu.dispMenu(userManager.getActiveUser());

        do { // Prompt for user input until entry is valid
            chosenOption = getUserIntegerInput();
        } while (!menu.verifyValidMenuOption(userManager.getActiveUser(), chosenOption));

        return menu.getMenuOptionState(chosenOption); // Return the next state the system should go into based on the selected option

    }

    static int getUserIntegerInput() { // Request selection until user enters an integer
        Scanner scan = new Scanner(System.in);
        try {
            return scan.nextInt();
        } catch (Exception E) {
            System.out.println("Invalid input! Please enter a valid number: ");
            return getUserIntegerInput();
        }
    }

    public static void initialiseMenuDefinitions() { // Defines all CLI menu options

        // The state variable correcsponds to the state that should be moved into if the option is selected
        menu_L1 = new CliMenu("MAIN MENU");
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
