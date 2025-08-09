package equipmentmanagerapplication;

/**
 *
 * @author ppj1707
 */
import equipmentmanagerapplication.CliManager.State; // Import application state definitions
import equipmentmanagerapplication.User.SecurityLevels; // Import user security level definitions
import java.util.ArrayList;

public class CliMenu {

    private ArrayList<MenuOption> menu;
    private String menuName;

    public CliMenu(String menuName) {
        this.menu = new ArrayList<>();
        this.menuName = menuName;
    }

    public void dispMenu(User currentUser) {
        System.out.println("------- " + this.menuName + " -------"); // Show menu title
        System.out.println("Please enter a number to select an option:");
        for (int i = 0; i < this.menu.size(); i++) { // Show menu accessible options (for the current users security level)
            if (currentUser.securityLevel.compareTo(this.menu.get(i).securityLevel) >= 0) {
                System.out.println("[" + (i + 1) + "] " + this.menu.get(i).option);
            }
        }
    }

    public void addMenuOption(String title, State programState, SecurityLevels securityLevel) {
        menu.add(new MenuOption(title, programState, securityLevel));
    }

    public State getMenuOptionState(int optionNum) {
        return this.menu.get(optionNum - 1).programState;
    }

    public boolean verifyValidMenuOption(User currentUser, int optionNum) {
        for (int i = 0; i < this.menu.size(); i++) {
            if (currentUser.securityLevel.compareTo(this.menu.get(i).securityLevel) >= 0) {
                if (optionNum == i + 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private class MenuOption {

        public String option;
        public State programState;
        public SecurityLevels securityLevel;

        public MenuOption(String option, State state, SecurityLevels securityLevel) {
            this.option = option;
            this.programState = state;
            this.securityLevel = securityLevel;

        }
    }

}
