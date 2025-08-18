package equipmentmanagerapplication;

/**
 *
 * @author ppj1707
 */
// Custom exception used to completely abort an action
public class AbortActionException extends Exception {

    public AbortActionException() {
        super("Action aborted\n");
    }
}
