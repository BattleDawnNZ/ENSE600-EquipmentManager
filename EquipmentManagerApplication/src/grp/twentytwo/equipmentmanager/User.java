package grp.twentytwo.equipmentmanager;

import java.io.Serializable;

/**
 * Abstract user class implements the functionality for all user types.
 *
 * @author ppj1707
 */
public abstract class User implements Serializable {

    private String userID; // Cannot be changed once initialised
    private String name;
    protected SecurityLevels securityLevel;

    public static enum SecurityLevels {
	GUEST, EMPLOYEE, MANAGER
    };

    User(String userID, String name, SecurityLevels securityLevel) {
	this.userID = userID;
	this.name = name;
	this.securityLevel = securityLevel;
    }

    public SecurityLevels getSecurityLevel() {
	return this.securityLevel;
    }

    public String getUserID() {
	return this.userID;
    }

    public String getName() {
	return this.name;
    }

    @Override
    public String toString() {
	return "UserID: " + userID + ", Name: " + name + ", Security Level: " + securityLevel + ".";
    }
}
