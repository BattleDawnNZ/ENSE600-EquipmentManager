package grp.twentytwo.database;

/**
 *
 * @author ppj1707
 */
public class PrimaryKeyClashException extends Exception {

    private static final String message = "Invalid id. That id already exists in the database.\n";

    public PrimaryKeyClashException() {
        super(message);
    }

}
