package grp.twentytwo.equipmentmanager;

/**
 *
 * @author ppj1707
 */
public class InvalidBookingRangeException extends Exception {

    private static final String message = "Invalid date range. The return date must be after the start of the booking.";

    public InvalidBookingRangeException() {
        super(message);
    }
}
