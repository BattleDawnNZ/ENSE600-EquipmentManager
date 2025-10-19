package grp.twentytwo.equipmentmanager;

/**
 *
 * @author ppj1707
 */
public class InvalidLocationException extends Exception {

    private static final String message = "Invalid location name. That name location does not exist\n";

    public InvalidLocationException() {
        super(message);
    }

}
