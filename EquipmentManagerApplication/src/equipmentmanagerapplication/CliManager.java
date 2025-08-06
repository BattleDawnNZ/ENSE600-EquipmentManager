package equipmentmanagerapplication;

import java.util.Scanner;  // Import the Scanner class


/**
 *
 * @author mymys
 */

public class CliManager {
    
    static Scanner scan = new Scanner(System.in);
    static UserManager userManager = new UserManager();
    
        
    public static void main(String[] args) {
        
        System.out.println("Welcome to Equipment Manager!");
        login();
        

        
    }
    
    
    static void login(){
        
        System.out.println("To begin please enter your ID number:");

        if(userManager.login(scan.nextLine())){
            System.out.println("Login successful!");
        } else {
            System.out.println("Sorry, your ID is not recognised. Please try again.");
            login();
        }
    }
    

}
