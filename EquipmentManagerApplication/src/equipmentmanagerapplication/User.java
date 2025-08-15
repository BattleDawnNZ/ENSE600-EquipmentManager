package equipmentmanagerapplication;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ppj1707
 */
public abstract class User implements Serializable {

    private String userID; // Cannot be changed once initialised
    private ArrayList<String> bookingIDs;
    protected SecurityLevels securityLevel;

    protected static enum SecurityLevels {
        GUEST, EMPLOYEE, MANAGER
    };

    public User(String userID, SecurityLevels securityLevel) {
        this.userID = userID;
        this.bookingIDs = new ArrayList<String>();
        this.securityLevel = securityLevel;
    }

    public int getSecurityLevel() {
        return this.securityLevel.ordinal();
    }

    public String getUserID() {
        return this.userID;
    }

    /**
     *
     * @return an array of active booking IDs
     */
    public ArrayList<String> getBookings() {
        return this.bookingIDs;
    }

    ////////CHECK NEW ID IS VALID
}
