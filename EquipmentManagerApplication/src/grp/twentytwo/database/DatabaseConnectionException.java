package grp.twentytwo.database;

/**
 *
 * @author ppj1707
 */
public class DatabaseConnectionException extends Exception {

    private static final String message = "Connection to the database failed.\n You may already have the application open.";

    public DatabaseConnectionException() {
        super(message);
    }

}
