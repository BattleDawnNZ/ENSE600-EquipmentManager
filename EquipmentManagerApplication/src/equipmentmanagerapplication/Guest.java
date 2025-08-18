package equipmentmanagerapplication;

/**
 *
 * @author ppj1707
 */
public class Guest extends User {

    public Guest(String userID, String name) {
        super(userID, name, SecurityLevels.GUEST);
    }

}
