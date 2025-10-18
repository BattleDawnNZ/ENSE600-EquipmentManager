package grp.twentytwo.equipmentmanager;

/**
 * Abstract user class implements the functionality for all user types.
 *
 * @author ppj1707
 */
public abstract class User {

    private String userID; // Cannot be changed once initialised
    private String name;
    protected SecurityLevels securityLevel;
    protected String password;

    public static enum SecurityLevels {
        GUEST, EMPLOYEE, MANAGER
    };

    public User(String userID, String name, SecurityLevels securityLevel, String password) {
        this.userID = userID;
        this.name = name;
        this.securityLevel = securityLevel;
        this.password = password;
    }

    public String getID() {
        return this.userID;
    }

    public void setID(String userID) {
        this.userID = userID;
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

    public String getPassword() { // Unofficial password. Used only for the user entry. Actual password remains in the DB.
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserID: " + userID + ", Name: " + name + ", Security Level: " + securityLevel + ".";
    }
}
