package grp.twentytwo.database;

/**
 *
 * @author ppj1707
 */
public class NullColumnValueException extends Exception {

    private static final String message = "The id or one of the column values is null.\n";

    public NullColumnValueException() {
        super(message);
    }

}
