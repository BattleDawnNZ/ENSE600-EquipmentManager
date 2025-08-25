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
    public boolean calibrateItem(String itemID) {
	Item item = itemManager.getItemFromID(itemID);
	if (item != null) {
	    item.calibrate();
	    itemManager.save();
	    return true;
	} else {
	    return false;
	}
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
