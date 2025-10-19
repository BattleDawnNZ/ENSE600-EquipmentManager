package grp.twentytwo.database;

/**
 *
 * @author ppj1707
 */
public class UnfoundPrimaryKeyException extends Exception {

    private static final String message = "Invalid ID. ID not found in database";

    public UnfoundPrimaryKeyException() {
        super(message);
    }

}
