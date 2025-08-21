package grp.twentytwo.equipmentmanager;

/**
 *
 * @author ppj1707
 */
public class Guest extends User {

    public Guest(String userID, String name) {
        super(userID, name, SecurityLevels.GUEST);
    }

}
