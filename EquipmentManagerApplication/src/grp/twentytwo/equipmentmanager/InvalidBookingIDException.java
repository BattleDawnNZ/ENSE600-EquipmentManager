package grp.twentytwo.equipmentmanager;

/**
 *
 * @author ppj1707
 */
public class InvalidBookingIDException extends Exception {

    private static final String message = "Invalid Booking ID. That ID does not exist\n";

    public InvalidBookingIDException() {
        super(message);
    }

}
