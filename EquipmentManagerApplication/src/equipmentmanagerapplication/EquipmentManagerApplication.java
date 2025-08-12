package equipmentmanagerapplication;

/**
 *
 * @author fmw5088
 */
public class EquipmentManagerApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	loadAll();
	CliManager.runCli();
	saveAll();
    }

    /**
     * Saves all managers to files.
     */
    public static void saveAll() {
	ItemManager.getInstance().save();
	BookingManager.getInstance().save();
	LocationManager.getInstance().save();
	UserManager.getInstance().save();
    }

    /**
     * Loads all managers from files.
     */
    public static void loadAll() {
	ItemManager.getInstance().load();
	BookingManager.getInstance().load();
	LocationManager.getInstance().load();
	UserManager.getInstance().load();
    }

}
