package grp.twentytwo.database;

/**
 *
 * @author ppj1707
 */
public class DatabaseConnectionException extends Exception {

    private static final String message = "Connection to the database failed.\nYou may already have the application open.\nThis instance of the application will now close.";

    public DatabaseConnectionException() {
        super(message);
    }

}
