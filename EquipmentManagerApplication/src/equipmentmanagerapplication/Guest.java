package equipmentmanagerapplication;

/**
 *
 * @author ppj1707
 */
public class Guest extends User {

    public Guest(String userID) {
        super(userID, SecurityLevels.GUEST);
    }

}
