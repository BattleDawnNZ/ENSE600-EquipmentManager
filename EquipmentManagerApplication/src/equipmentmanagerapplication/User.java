package equipmentmanagerapplication;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ppj1707
 */
public abstract class User implements Serializable {

    private String userID; // Cannot be changed once initialised
    private String name;
    protected SecurityLevels securityLevel;

    protected static enum SecurityLevels {
	GUEST, EMPLOYEE, MANAGER
    };

    public User(String userID, String name, SecurityLevels securityLevel) {
	this.userID = userID;
	this.name = name;
	this.securityLevel = securityLevel;
    }

    public int getSecurityLevel() {
	return this.securityLevel.ordinal();
    }

    public String getUserID() {
	return this.userID;
    }

    @Override
    public String toString() {
	return "UserID: " + userID + ", Name: " + name + ", Security Level: " + securityLevel + ".";
    }
}
