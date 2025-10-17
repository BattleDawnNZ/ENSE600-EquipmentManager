package grp.twentytwo.equipmentmanager;

/**
 * Abstract user class implements the functionality for all user types.
 *
 * @author ppj1707
 */
public abstract class User {

    private final String userID; // Cannot be changed once initialised
    private String name;
    protected SecurityLevels securityLevel;

    public static enum SecurityLevels {
        GUEST, EMPLOYEE, MANAGER
    };

    public User(String userID, String name, SecurityLevels securityLevel) {
        this.userID = userID;
        this.name = name;
        this.securityLevel = securityLevel;
    }

    public String getUserID() {
        return this.userID;
    }

    public SecurityLevels getSecurityLevel() {
        return this.securityLevel;
    }

    public void setSecurityLevel(SecurityLevels level) {
        this.securityLevel = level;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserID: " + userID + ", Name: " + name + ", Security Level: " + securityLevel + ".";
    }
}
