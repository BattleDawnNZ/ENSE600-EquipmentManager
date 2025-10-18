package grp.twentytwo.equipmentmanager;

/**
 * The manager user type.
 *
 * @author ppj1707
 */
public class Manager extends User {

    public Manager(String userID, String name, String password) {
        super(userID, name, SecurityLevels.MANAGER, password);
    }

    public Manager(String userID, String name) {
        super(userID, name, SecurityLevels.MANAGER, null);
    }

}
