package equipmentmanagerapplication;

/**
 *
 * @author ppj1707
 */
public class Manager extends User {

    public Manager(String userID) {
        super(userID, SecurityLevels.MANAGER);
    }

}
