package grp.twentytwo.equipmentmanager;

/**
 *
 * @author ppj1707
 */
public class InvalidItemIDException extends Exception {

    private static final String message = "Invalid Item ID. That ID does not exist\n";

    public InvalidItemIDException() {
        super(message);
    }

}
