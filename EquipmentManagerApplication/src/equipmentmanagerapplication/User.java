package equipmentmanagerapplication;

import java.util.ArrayList;

/**
 *
 * @author ppj1707
 */

public abstract class User {
    
    private String userID; // Cannot be changed once initialised
    private ArrayList<String> bookingIDs;
    protected SecurityLevels securityLevel;
    protected int[] validMenuOptions_L1;
    
    protected static enum SecurityLevels {GUEST, EMPLOYEE, MANAGER};
    
    public User(String userID, SecurityLevels securityLevel){
        this.userID = userID;
        this.bookingIDs = new ArrayList<String>();
        this.securityLevel = securityLevel;
    }
    
    public int getSecurityLevel(){
        return this.securityLevel.ordinal();
    }
    
    // Return an array of active booking IDs
    public ArrayList<String> getBookings(){
        return this.bookingIDs;
    }
    
    // Return an array of active booking IDs
    public int[] getValidMenuOptions_L1(){
        return this.validMenuOptions_L1;
    }
    
    
    ////////CHECK NEW ID IS VALID
            
}
