package grp.twentytwo.database;

/**
 *
 * @author ppj1707
 */
public class NonNumericKeyClashException extends Exception {

    private static final String message = "A fully non-numeric key was found in the database.\nKey auto-generation failed. Please only use autogeneration or manual setting.";

    public NonNumericKeyClashException() {
        super(message);
    }
}
