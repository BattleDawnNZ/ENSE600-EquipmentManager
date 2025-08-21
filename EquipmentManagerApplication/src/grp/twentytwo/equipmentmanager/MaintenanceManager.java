package grp.twentytwo.equipmentmanager;

/**
 *
 * @author fmw5088
 */
public class MaintenanceManager {

    private ItemManager itemManager;

    public MaintenanceManager(ItemManager itemManager) {
	this.itemManager = itemManager;
    }

    /**
     *
     * @param itemID
     */
    public void calibrateItem(String itemID) {
	itemManager.getItemFromID(itemID).calibrate();
	itemManager.save();
    }

    /**
     *
     * @param itemID
     */
    public void flagItemForCalibration(String itemID) {
	itemManager.getItemFromID(itemID).flagForCalibration();
	itemManager.save();
    }

    /**
     *
     * @param itemID
     * @param serviceNote
     */
    public void addServiceNote(String itemID, String serviceNote) {
	itemManager.getItemFromID(itemID).addHistory(serviceNote);
    }
}
