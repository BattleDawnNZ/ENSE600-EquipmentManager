package equipmentmanagerapplication;

import java.util.Scanner;  // Import the Scanner class


/**
 *
 * @author mymys
 */

public class CliManager {
    
    static Scanner scan = new Scanner(System.in);
    static UserManager userManager = new UserManager();
    
    static final String[] MENU_L1_OPTIONS = {"Logout", "Item Search", "Item Bookings", "Manage Users", "Manage Equipment"};
    static final String[] MENU_L2_OPTIONS_ManageEquipment = {"Add Calibration", "Add Service Note", "Flag Item for Calibraton", "Add New Item", "Delete Item", "Move Item Location", "Add new Location", "Return to Homescreen ->"};
    static final String[] MENU_L2_OPTIONS_ManageUsers = {"Create New User", "Remove User", "View User Details"};
    static final String[] MENU_L2_OPTIONS_BookItems = {"Book Item", "Return Item", "See my Bookings"};
    static final String[] MENU_L2_OPTIONS_ItemSearch = {"Search by Item Code", "Search by Item Name", "Search by Item Type"};
    
        
    public static void runCli() {
        int chosenOption;
        
        System.out.println("Welcome to Equipment Manager!");
        login();
        System.out.println("Please enter a number to select an option:");
        dispMenu_L1();
        do { // Prompt for user input until entry is valid
            chosenOption = getUserIntegerInput();
        } while(!verifyMenuOption(chosenOption, userManager.getActiveUser().getValidMenuOptions_L1()));
        
        
        
  
    }
    
    
    static void login(){
        
        System.out.println("To begin please enter your ID number:");

        if(userManager.login(scan.nextLine().trim())){
            System.out.println("Login successful!");
        } else {
            System.out.println("Sorry, your ID is not recognised. Please try again.");
            login();
        }
    }
    
        
    static boolean verifyMenuOption(int chosenOption, int[] validOptions){

        for (int optionNum : userManager.getActiveUser().getValidMenuOptions_L1()) { // Check the entered option is allowed for the user type/security level
            if(chosenOption == optionNum) {
                return true;
            }
        }
        System.out.println("Invalid input! Please enter a valid number: ");
        return false;
    }
       
    
    static void dispMenu_L1(){
        
        for(int optionNum : userManager.getActiveUser().getValidMenuOptions_L1()){
            System.out.println("[" + optionNum + "] "+ MENU_L1_OPTIONS[optionNum-1]);
        }
    }
    
    static int getUserIntegerInput(){ // Request selection until user enters an integer
        Scanner scan = new Scanner(System.in);
        try {
            return scan.nextInt(); 
        } catch(Exception E){
            System.out.println("Invalid input! Please enter a valid number: ");
            return getUserIntegerInput();
        }
    }
    
//                switch(chosenOption){
//                case 0: // Logout
//                    System.out.println("------- USER MANAGEMENT ----------//heading");
//                    break;
//                case 1: // Item Search
//                    break;
//                case 2: // Item Bookings
//                    break;
//                case 3:
//                    break;
//                case 4:
//                    break;
//                //default:
//                    //invalid
//
//            }
    
    
    
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

}
