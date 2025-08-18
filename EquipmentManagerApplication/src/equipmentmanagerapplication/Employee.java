package equipmentmanagerapplication;

/**
 *
 * @author ppj1707
 */
public class Employee extends User {

    public Employee(String userID, String name) {
        super(userID, name, SecurityLevels.EMPLOYEE);
    }

}
