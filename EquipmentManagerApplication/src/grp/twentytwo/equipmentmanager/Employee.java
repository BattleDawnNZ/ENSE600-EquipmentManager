package grp.twentytwo.equipmentmanager;

/**
 * The employee user type.
 *
 * @author ppj1707
 */
public class Employee extends User {

    public Employee(String userID, String name, String password) {
        super(userID, name, SecurityLevels.EMPLOYEE, password);
    }

    public Employee(String userID, String name) {
        super(userID, name, SecurityLevels.EMPLOYEE, null);
    }

}
