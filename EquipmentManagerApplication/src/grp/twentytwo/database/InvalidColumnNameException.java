package grp.twentytwo.database;

/**
 * Custom exception used for invalid data input
 *
 * @author ppj1707
 */
public class InvalidColumnNameException extends Exception {

    private static final String message = "Invalid column names. They do not match the format used to initialise the table.\n";

    public InvalidColumnNameException() {
        super(message);
    }
}
