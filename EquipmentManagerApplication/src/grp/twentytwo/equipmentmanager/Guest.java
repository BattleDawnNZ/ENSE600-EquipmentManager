package grp.twentytwo.equipmentmanager;

/**
 * The guest user type.
 *
 * @author ppj1707
 */
public class Guest extends User {

    public Guest(String userID, String name, String password) {
        super(userID, name, SecurityLevels.GUEST, password);
    }

    public Guest(String userID, String name) {
        super(userID, name, SecurityLevels.GUEST, null);
    }

}
