package equipmentmanagerapplication;

import equipmentmanagerapplication.User.SecurityLevels;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        } catch (NoSuchElementException | IllegalStateException e) { // No line found
            System.out.println("Error! " + e.getMessage() + ". Please retry (or enter 'x' to exit the action)");
            return getUserEntry();
        }
        // Scanner closed

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
            return getUserInput_itemID(); // Reprompt for valid user input.
        }
    }

    public String getUserInput_userID() throws AbortActionException {

        String entry = getUserEntry();
        try {
            int id = Integer.parseInt(entry); // Check the input is valid (a number)
            if (UserManager.verifyID(entry)) { // Check that id exists
                return entry;
            } else {
                System.out.println("User not found! Please enter a valid ID number: ");
            }
            return entry;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! All valid ID numbers are numeric: ");
            return getUserInput_userID();
        }
    }

    public ZonedDateTime getUserInput_date() throws AbortActionException {

        System.out.println("Please enter the date you will return the item in the format 'dd-MM-yyyy HH:mm:ss' : ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String entry = getUserEntry();

        try {
            return ZonedDateTime.parse(entry, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid input! Please ensure you use the correct format.");
            return getUserInput_date();
        }
    }

    public SecurityLevels getUserInput_securityLevel() throws AbortActionException {
        System.out.println("Please enter the new users security level (MANAGER, EMPLOYEE, GUEST): ");

        String entry = getUserEntry().trim().toUpperCase();
        try {
            return SecurityLevels.valueOf(entry);
        } catch (Exception E) {
            System.out.println("Invalid input! Ensure you enter a valid level");
            return getUserInput_securityLevel();
        }
    }

}
