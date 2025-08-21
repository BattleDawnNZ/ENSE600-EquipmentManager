package grp.twentytwo.equipmentmanagerapplication;

import grp.twentytwo.equipmentmanager.BookingManager;
import grp.twentytwo.equipmentmanager.ItemManager;
import grp.twentytwo.equipmentmanager.LocationManager;
import grp.twentytwo.equipmentmanager.MaintenanceManager;
import grp.twentytwo.equipmentmanager.UserManager;

/**
 *
 * @author fmw5088
 */
public class EquipmentManagerApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	UserManager userManager = new UserManager();
	ItemManager itemManager = new ItemManager();
	BookingManager bookingManager = new BookingManager(itemManager);
	LocationManager locationManager = new LocationManager(itemManager);
	MaintenanceManager maintenanceManager = new MaintenanceManager(itemManager);
	itemManager.setLocationManager(locationManager);

	// Load all manager data from file
	itemManager.load();
	bookingManager.load();
	locationManager.load();
	userManager.load();

	CliManager cliManager = new CliManager(itemManager, userManager, bookingManager, locationManager, maintenanceManager);
	cliManager.runCli();

	// Save all managers to files
	itemManager.save();
	bookingManager.save();
	locationManager.save();
	userManager.save();
    }

}
