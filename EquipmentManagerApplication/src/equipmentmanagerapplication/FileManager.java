package equipmentmanagerapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author fmw5088
 */
public class FileManager {

    private static final String saveDirectory = "data/";

    public static void loadAll() {
	ItemManager.setInstance(loadItemManager());
	LocationManager.setInstance(loadLocationManager());
	BookingManager.setInstance(loadBookingManager());
	UserManager.setInstance(loadUserManager());
    }

    public static void saveAll() {
	saveItemManager();
	saveLocationManager();
	saveBookingManager();
    }

    public static ItemManager loadItemManager() {
	return (ItemManager) loadFile("item_manager.bin");
    }

    public static void saveItemManager() {
	saveFile(ItemManager.getInstance(), "item_manager.bin");
    }

    public static LocationManager loadLocationManager() {
	return (LocationManager) loadFile("location_manager.bin");
    }

    public static void saveLocationManager() {
	saveFile(LocationManager.getInstance(), "location_manager.bin");
    }

    public static BookingManager loadBookingManager() {
	return (BookingManager) loadFile("booking_manager.bin");
    }

    public static void saveBookingManager() {
	saveFile(BookingManager.getInstance(), "booking_manager.bin");
    }

    public static UserManager loadUserManager() {
	return (UserManager) loadFile("user_manager.bin");
    }

    public static void saveUserManager() {
	saveFile(UserManager.getInstance(), "user_manager.bin");
    }

    /**
     *
     * @param saveObject
     * @param fileName
     */
    public static void saveFile(Serializable saveObject, String fileName) {
	try {
	    // Create the save Directory
	    new File(saveDirectory).mkdir();
	    // Open Output Stream
	    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveDirectory + fileName));
	    // Write Object to File
	    oos.writeObject(saveObject);
	    //Close Output Stream
	    oos.close();
	} catch (Exception err) {
	    System.out.println(err.toString());
	}
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static Object loadFile(String fileName) {
	Object data = null;
	try {
	    // Open Input Stream
	    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveDirectory + fileName));
	    // Read Object from File
	    data = ois.readObject();
	    ois.close();
	} catch (Exception err) {
	    System.out.println(err.toString());
	}
	return data;
    }
}
