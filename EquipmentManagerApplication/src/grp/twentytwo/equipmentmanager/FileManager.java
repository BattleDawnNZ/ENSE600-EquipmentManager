package grp.twentytwo.equipmentmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author fmw5088
 */
public class FileManager {

    private static final String saveDirectory = "data/";

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
	} catch (IOException err) {
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
	} catch (ClassNotFoundException err) {
	    System.out.println(err.toString());
	} catch (IOException err) {
	    System.out.println("No loadable data found for " + fileName + ".");
	}
	return data;
    }
}
