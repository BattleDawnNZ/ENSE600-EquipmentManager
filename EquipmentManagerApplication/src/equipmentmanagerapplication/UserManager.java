package equipmentmanagerapplication;

import java.util.HashMap; // Import the HashSet class

/**
 *
 * @author ppj1707
 */
public class UserManager {
    
    private HashMap<String, User> users; // BAD Practice to have type as abstract class????????????? OR OKAY??
    private User activeUser;
    
    
    public UserManager()
    {
        Manager root = new Manager("root"); // DEBUG!!!
        users = new HashMap<String, User>(); // DEBUG!!!
        users.put("root", root); // DEBUG!!!
    }
    
    
    
    
    
    public User getActiveUser(){
        return activeUser;
    }
    
    public User getUserFromID(String userID){
        if(users.containsKey(userID)){
            return users.get(userID);
        } else{
            return null;
        }
    }
    
    // Return True if user created successfully
    public boolean createUser(String userID){
        
        //users.add(e);
        return false;
    }
    
    public boolean login(String userID){
        if(users.containsKey(userID)){
            this.activeUser = users.get(userID);
            return true;
        }
        else{
            return false;
        }
    }
       
    
    

}
