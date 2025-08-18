package equipmentmanagerapplication;

import equipmentmanagerapplication.User.SecurityLevels;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    /**
     *
     * @return the line typed into the console
     * @throws AbortActionException (if the user enters 'x' or 'X')
     */
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
     * @throws equipmentmanagerapplication.AbortActionException (if the user
     * enters 'x' or 'X')
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

    /**
     *
     * @return a string that is alphabetic
     * @throws AbortActionException (if the user enters 'x' or 'X')
     */
    public String getUserInput_alphabeticString() throws AbortActionException {
        String entry = getUserEntry();
        if (entry.chars().allMatch(c -> Character.isLetter(c) || c == ' ')) {
            return entry.toUpperCase();
        } else {
            System.out.println("Invalid input! All characters must be letters. Please enter a valid string: ");
            return getUserInput_string();
        }
    }

    /**
     *
     * @return any input in the console
     * @throws AbortActionException (if the user enters 'x' or 'X')
     */
    public String getUserInput_string() throws AbortActionException {
        return getUserEntry();
    }

    /**
     *
     * @return a valid item ID that exists
     * @throws AbortActionException (if the user enters 'x' or 'X')
     */
    public String getUserInput_itemID() throws AbortActionException {

        String entry = getUserEntry().trim().toUpperCase();
        if (ItemManager.verifyID(entry)) {
            return entry;
        } else {
            System.out.println("Invalid ID! If unknown, use the search function on the homepage to find it.");
            return getUserInput_itemID(); // Reprompt for valid user input.
        }
    }

    /**
     *
     * @return a valid user ID that exists
     * @throws AbortActionException (if the user enters 'x' or 'X')
     */
    public String getUserInput_userID() throws AbortActionException {

        String entry = getUserEntry();
        try {
            int id = Integer.parseInt(entry); // Check the input is valid (a number)
            if (UserManager.verifyID(entry)) { // Check that id exists
                return entry;
            } else {
                System.out.println("User not found! Please enter a valid ID number: ");
            }
            return getUserInput_userID();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! All valid ID numbers are numeric: ");
            return getUserInput_userID();
        }
    }

    /**
     *
     * @return a valid new ID (an integer number)
     * @throws AbortActionException (if the user enters 'x' or 'X')
     */
    public String getUserInput_newUserID() throws AbortActionException {

        String entry = getUserEntry();
        try {
            int id = Integer.parseInt(entry); // Check the input is valid (a number)
            if (!UserManager.verifyID(entry)) { // Check that id exists
                return entry;
            } else {
                System.out.println("User ID already in the system! Please enter a valid, new ID number: ");
                return getUserInput_newUserID();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! All valid ID numbers are numeric: ");
            return getUserInput_userID();
        }
    }

    /**
     *
     * @return a zoned date-time object
     * @throws AbortActionException (if the user enters 'x' or 'X')
     */
    public ZonedDateTime getUserInput_date() throws AbortActionException {

        System.out.println("Please enter the date you will return the item in the format 'dd-MM-yyyy HH:mm' : ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String entry = getUserEntry() + ":00";

        try {
            ZonedDateTime returnDate = LocalDateTime.parse(entry, formatter).atZone(ZoneId.systemDefault());

            if (ZonedDateTime.now().isBefore(returnDate)) {
                return returnDate;
            } else {
                System.out.println("Invalid input! That time is in the past.");
                return getUserInput_date();
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid input! Please ensure you use the correct format.");
            return getUserInput_date();
        }
    }

    /**
     *
     * @return a valid option from the SecurityLevel enum
     * @throws AbortActionException (if the user enters 'x' or 'X')
     */
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

    /**
     *
     * @return a valid location (as a string)
     * @throws AbortActionException (if the user enters 'x' or 'X')
     */
    public String getUserInput_location() throws AbortActionException {

        String entry = getUserEntry().trim().toUpperCase();
        if (LocationManager.isValidLocation(entry)) {
            return entry;
        } else {
            System.out.println("Invalid Location! Choose from the options, or exit this action (x) and create a new location.");
            return getUserInput_location(); // Reprompt for valid user input.
        }
    }

    /**
     *
     * @return a valid booking ID (as a string) if the booking belongs to the
     * user
     * @throws AbortActionException (if the user enters 'x' or 'X')
     */
    public String getUserInput_bookingID() throws AbortActionException {

        String entry = getUserEntry();
        if (BookingManager.verifyBookingOwner(entry, UserManager.getActiveUser())) {
            return entry;
        } else {
            System.out.println("Invalid Booking ID! Check your entry. You can only return items issued to you.");
            return getUserInput_bookingID(); // Reprompt for valid user input.
        }
    }

}
