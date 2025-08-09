package equipmentmanagerapplication;

/**
 *
 * @author ppj1707
 */
import equipmentmanagerapplication.CliManager.State; // Import application state definitions
import equipmentmanagerapplication.User.SecurityLevels; // Import user security level definitions
import java.util.ArrayList;

public class CliMenu {

    ArrayList<MenuOption> menu = new ArrayList<>();

    public static void dispMenu(CliMenu chosenMenu) {
        for (int i = 0; i < chosenMenu.menu.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + chosenMenu.menu.get(i).option);
        }
    }

    public static void dispMenu(CliMenu chosenMenu, User currentUser) {
        for (int i = 0; i < chosenMenu.menu.size(); i++) {
            if (currentUser.securityLevel.compareTo(chosenMenu.menu.get(i).securityLevel) >= 0) {
                System.out.println("[" + (i + 1) + "] " + chosenMenu.menu.get(i).option);
            }
        }
    }

    public void addMenuOption(String title, State programState, SecurityLevels securityLevel) {
        menu.add(new MenuOption(title, programState, securityLevel));
    }

    public State getMenuOptionState(int optionNum) {
        return this.menu.get(optionNum).programState;
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
