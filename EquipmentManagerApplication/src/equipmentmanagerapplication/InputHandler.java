package equipmentmanagerapplication;

import equipmentmanagerapplication.User.SecurityLevels;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author ppj1707
 */
public class InputHandler {

    private String getUserEntry() throws AbortActionException {
        Scanner scan = new Scanner(System.in);

        try {
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("x")) {
                throw (new AbortActionException());
            } else {
                return input;
            }
        } catch (NoSuchElementException e) { // No line found
            System.out.println("Error! " + e.getMessage() + ". Please retry (or enter 'x' to exit the action)");
            return getUserEntry();
        } catch (IllegalStateException e) { // Scanner closed
            System.out.println("Error! " + e.getMessage() + ". Please retry (or enter 'x' to exit the action)");
            return getUserEntry();
        }
    }

    /**
     * Requests user input until the user enters an integer
     *
     * @return an integer entered by the user
     * @throws equipmentmanagerapplication.AbortActionException
     */
    public int getUserInput_integer() throws AbortActionException {
        String entry = getUserEntry();
        try {
            return Integer.parseInt(entry);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number: ");
            return getUserInput_integer();
        }
    }

    public String getUserInput_itemID() throws AbortActionException {

        System.out.println("Please enter the item ID: ");

        String entry = getUserEntry().trim().toUpperCase();
        if (ItemManager.verifyID(entry)) {
            return entry;
        } else {
            System.out.println("Invalid ID! If unknown, use the search function on the homepage to find it.");
            return getUserInput_itemID();
        }
    }

    public String getUserInput_userID() {

        System.out.println("Please enter the user ID: ");

        Scanner scan = new Scanner(System.in);
        try {
            String id = String.valueOf(scan.nextInt());
            if (UserManager.verifyID(id)) {
                return id;
            } else {
                System.out.println("Invalid input! Please enter a valid ID number: ");
            }
        } catch (Exception E) {
            System.out.println("Invalid input! Please enter a number: ");
            return getUserInput_userID();
        }
        return getUserInput_userID();
    }

    public ZonedDateTime getUserInput_date() {

        System.out.println("Please enter the date you will return the item in the format 'dd-MM-yyyy HH:mm:ss' : ");

        Scanner scan = new Scanner(System.in);

        try {
            String input = scan.next();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            return ZonedDateTime.parse(input, formatter);

        } catch (Exception E) {
            System.out.println("Invalid input! Please ensure you use the correct format.");
            return getUserInput_date();
        }
    }

    public SecurityLevels getUserInput_securityLevel() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the new users security level (MANAGER, EMPLOYEE, GUEST): ");
        String chosenLevel;
        try {
            String input = scan.nextLine().trim().toUpperCase();
            return SecurityLevels.valueOf(input);
        } catch (Exception E) {
            System.out.println("Invalid input!");
            return getUserInput_securityLevel();
        }
    }

}
