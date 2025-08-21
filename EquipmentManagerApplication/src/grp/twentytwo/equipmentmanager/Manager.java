package grp.twentytwo.equipmentmanager;

/**
 *
 * @author ppj1707
 */
public class Manager extends User {

    public Manager(String userID, String name) {
        super(userID, name, SecurityLevels.MANAGER);
    }

}
