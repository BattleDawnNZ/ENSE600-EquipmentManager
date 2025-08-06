package equipmentmanagerapplication;

/**
 *
 * @author ppj1707
 */

public class Employee extends User {
    
    public Employee(String userID){
        super(userID, SecurityLevels.EMPLOYEE);
    }
          
}
