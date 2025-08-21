package grp.twentytwo.equipmentmanagerapplication;

/**
 *
 * @author ppj1707
 */
// Custom exception used to completely abort an action
public class AbortActionException extends Exception {

    private static final String message = "Action aborted\n";

    public AbortActionException() {
	super(message);
    }
}
