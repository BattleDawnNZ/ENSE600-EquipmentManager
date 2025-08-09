package equipmentmanagerapplication;

/**
 *
 * @author fmw5088
 */
public class MaintenanceManager {

    /**
     *
     * @param itemID
     */
    public static void calibrateItem(String itemID) {
	ItemManager.getItemFromID(itemID).calibrate();
    }

    /**
     *
     * @param itemID
     */
    public static void flagItemForCalibration(String itemID) {
	ItemManager.getItemFromID(itemID).flagForCalibration();
    }

    /**
     *
     * @param itemID
     * @param serviceNote
     */
    public static void addServiceNote(String itemID, String serviceNote) {
	ItemManager.getItemFromID(itemID).addHistory(serviceNote);
    }
}
