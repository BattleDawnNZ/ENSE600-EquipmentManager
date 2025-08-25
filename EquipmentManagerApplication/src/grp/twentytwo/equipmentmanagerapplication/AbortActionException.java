package grp.twentytwo.equipmentmanagerapplication;

/**
 * Custom exception used to completely abort an action
 *
 * @author ppj1707
 */
public class AbortActionException extends Exception {

    private static final String message = "Action aborted\n";

    public AbortActionException() {
	super(message);
    }
}
