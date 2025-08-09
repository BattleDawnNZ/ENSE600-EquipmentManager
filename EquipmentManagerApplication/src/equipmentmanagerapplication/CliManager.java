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

    private static CliMenu L1;
    private static CliMenu L2_ManageEquipment;

    public enum State {
        LOGIN, LOGOUT, MENU_L1,
        MENU_L2_ManageEquipment, MENU_L2_ManageUsers, MENU_L2_BookItems, MENU_L2_ItemSearch,
        ACTION_AddServiceNote, ACTION_FlagCalibration, ACTION_AddItem, ACTION_DeleteItem, ACTION_MoveItemLocation, ACTION_AddNewLocation
    }

    static State currentState = State.LOGIN;

    public static void runCli() {

        initialiseMenuDefinitions();

        System.out.println("Welcome to Equipment Manager!");

        while (true) {
            switch (currentState) {
                case LOGIN:
                    login();
                    currentState = State.MENU_L1;
                    break;
                case MENU_L1:
                    State newState = processMenu_L1();
                    currentState = newState;
                    break;
                case MENU_L2_ManageEquipment:

                    break;
                case MENU_L2_ManageUsers:
                    processMenu_L2_manageUsers();
                    break;
                case MENU_L2_BookItems:
                    break;
                case MENU_L2_ItemSearch:
                    break;

            }
        }
    }

//         switch(chosenOption){ // Process main menu selection
//            case 1: // Logout
//                userManager.logout();
//                // Return to main menu.
//                break;
//            case 2: // Manage equipment
//                System.out.println("------- USER MANAGEMENT MENU -------");
//                dispMenu_L1();
//                break;
//            case 3: // Item Bookings
//                break;
//            case 4:
//                break;
//            case 5:
//                break;
//            //default:
//                //invalid
//         }
    static void login() {

        System.out.println("To begin please enter your ID number:");

        if (userManager.login(scan.nextLine().trim())) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Sorry, your ID is not recognised. Please try again.");
            login();
        }
    }

    static boolean verifyMenuOption(int chosenOption, int[] validOptions) {

        for (int optionNum : userManager.getActiveUser().getValidMenuOptions_L1()) { // Check the entered option is allowed for the user type/security level
            if (chosenOption == optionNum) {
                return true;
            }
        }
        System.out.println("Invalid input! Please enter a valid number: ");
        return false;
    }

    static State processMenu_L1() {

        int chosenOption;

        System.out.println("Please enter a number to select an option:");

        System.out.println("------- MAIN MENU -------");
        // Display L1 Menu. Only display options available for the users security level
        CliMenu.dispMenu(L1, userManager.getActiveUser()); // Display the main menu (Only the options available for the current users security level)

        do { // Prompt for user input until entry is valid
            chosenOption = getUserIntegerInput();
        } while (!verifyMenuOption(chosenOption, userManager.getActiveUser().getValidMenuOptions_L1()));

        return L1.getMenuOptionState(chosenOption);
    }

    static void processMenu_L2_manageUsers() {

        int chosenOption;
        System.out.println("------- MANAGE USERS MENU -------");
        dispMenu_L2(MenuOptions.L2_ManageUsers);

        do { // Prompt for user input until entry is a valid option number
            chosenOption = getUserIntegerInput();
        } while ((chosenOption < 0) || (chosenOption > MenuOptions.L2_ManageUsers.length));

        // CALL PROCESSING FUNCTION!!!!!!
    }

    static void dispMenu_L2(String[] options) {
        System.out.println("Please enter a number to select an option:");
        for (int optionNum = 0; optionNum < options.length; optionNum++) {
            System.out.println("[" + optionNum + 1 + "] " + options[optionNum]);
        }
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

    public static class MenuOptions { // Stores all CLI menu options

        static final String[] L1 = {"Logout", "Item Search", "Item Bookings", "Manage Users", "Manage Equipment"};

        static final String[] L2_ManageEquipment = {"Add Calibration", "Add Service Note", "Flag Item for Calibraton", "Add New Item", "Delete Item", "Move Item Location", "Add new Location", "Return to Homescreen ->"};
        static final String[] L2_ManageUsers = {"Create New User", "Remove User", "View User Details"};
        static final String[] L2_BookItems = {"Book Item", "Return Item", "See my Bookings"};
        static final String[] L2_ItemSearch = {"Search by Item Code", "Search by Item Name", "Search by Item Type"};

        static final String[][] L2 = {L2_ManageEquipment, L2_ManageUsers, L2_BookItems, L2_ItemSearch};
    }

    public static void initialiseMenuDefinitions() {

        // The state variable correcsponds to the state that should be moved into if the option is selected
        L1 = new CliMenu();
        L1.addMenuOption("Logout", State.LOGOUT, SecurityLevels.GUEST);
        L1.addMenuOption("Item Search", State.MENU_L2_ItemSearch, SecurityLevels.GUEST);
        L1.addMenuOption("Item Bookings", State.MENU_L2_BookItems, SecurityLevels.EMPLOYEE);
        L1.addMenuOption("Manage Users", State.MENU_L2_ManageUsers, SecurityLevels.MANAGER);
        L1.addMenuOption("Manage Equipment", State.MENU_L2_ManageEquipment, SecurityLevels.MANAGER);

        L2_ManageEquipment = new CliMenu();
        L2_ManageEquipment.addMenuOption("Return to Homescreen ->", State.MENU_L1, SecurityLevels.GUEST);
        L2_ManageEquipment.addMenuOption("Add Service Note", State.ACTION_AddServiceNote, SecurityLevels.MANAGER);
        L2_ManageEquipment.addMenuOption("Flag Item for Calibraton", State.ACTION_FlagCalibration, SecurityLevels.MANAGER);
        L2_ManageEquipment.addMenuOption("Add New Item", State.ACTION_AddItem, SecurityLevels.MANAGER);
        L2_ManageEquipment.addMenuOption("Delete Item", State.ACTION_DeleteItem, SecurityLevels.MANAGER);
        L2_ManageEquipment.addMenuOption("Move Item Location", State.ACTION_MoveItemLocation, SecurityLevels.MANAGER);
        L2_ManageEquipment.addMenuOption("Add new Location", State.ACTION_AddNewLocation, SecurityLevels.MANAGER);

        //static final String[]  = {"Add Calibration", , , , ", , , "};
        //static final String[] L2_ManageUsers = {"Create New User", "Remove User", "View User Details"};
        //static final String[] L2_BookItems = {"Book Item", "Return Item", "See my Bookings"};
        //static final String[] L2_ItemSearch = {"Search by Item Code", "Search by Item Name", "Search by Item Type"};
        //static final String[][] L2 = {L2_ManageEquipment, L2_ManageUsers, L2_BookItems, L2_ItemSearch};
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
