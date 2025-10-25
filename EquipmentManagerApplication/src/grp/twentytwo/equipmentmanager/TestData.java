package grp.twentytwo.equipmentmanager;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Run this class to regenerate the DB tables and test data from scratch
 *
 * @author ppj1707
 */
public class TestData {

    private static ModelManager model = new ModelManager();

    public static void main(String[] args) {
	TestData.model.setupManagers();
	TestData.dropTables();
	TestData.model.setupManagers();
	TestData.generate();
	TestData.printAll();
    }

    public static void generate() {
	try {

	    model.addLocation("WORKSHOP 1");
	    model.addLocation("WORKSHOP 2");
	    model.addLocation("WORKSHOP 3");
	    model.addLocation("WORKSHOP 4");
	    model.addLocation("WORKSHOP 5");
	    model.addLocation("ELECTRICAL LAB 1");
	    model.addLocation("ELECTRICAL LAB 2");
	    model.addLocation("ELECTRICAL LAB 3");
	    model.addLocation("ELECTRICAL LAB 4");

	    User user = new Manager("111", "Bob", "123123");
	    model.addUser(user);
	    model.addUser(new Employee("222", "Sally", "123123"));
	    model.addUser(new Guest("333", "Fred", "123123"));

	    model.login(user);

	    model.addItem(new Item("R9000 Universal Laser Cutter", "WORKSHOP 1", "Manufacturing/Cutting"));
	    model.addItem(new Item("R9000 Universal Laser Cutter", "WORKSHOP 2", "Manufacturing/Cutting"));
	    model.addItem(new Item("Ender 3D Printer", "WORKSHOP 2", "Manufacturing/Additive"));
	    model.addItem(new Item("Ender 3D Printer", "WORKSHOP 2", "Manufacturing/Additive"));
	    model.addItem(new Item("Ender 3D Printer", "WORKSHOP 3", "Manufacturing/Additive"));
	    model.addItem(new Item("Ender 3D Printer", "WORKSHOP 3", "Manufacturing/Additive"));
	    model.addItem(new Item("Ender 3D Printer", "WORKSHOP 3", "Manufacturing/Additive"));
	    model.addItem(new Item("Duratech True RMS Multimeter", "Electrical Lab 1", "Electrical/Measurement"));
	    model.addItem(new Item("Duratech True RMS Multimeter", "Electrical Lab 1", "Electrical/Measurement"));
	    model.addItem(new Item("Duratech True RMS Multimeter", "Electrical Lab 1", "Electrical/Measurement"));
	    model.addItem(new Item("Duratech True RMS Multimeter", "WORKSHOP 3", "Electrical/Measurement"));
	    model.addItem(new Item("Duratech True RMS Multimeter", "WORKSHOP 4", "Electrical/Measurement"));
	    model.addItem(new Item("Duratech True RMS Multimeter", "WORKSHOP 4", "Electrical/Measurement"));
	    model.addItem(new Item("Soldering Iron", "ELECTRICAL LAB 1", "Electrical/Soldering"));
	    model.addItem(new Item("Soldering Iron", "ELECTRICAL LAB 1", "Electrical/Soldering"));
	    model.addItem(new Item("Soldering Iron", "ELECTRICAL LAB 1", "Electrical/Soldering"));
	    model.addItem(new Item("Soldering Iron", "ELECTRICAL LAB 1", "Electrical/Soldering"));
	    model.addItem(new Item("Soldering Iron", "ELECTRICAL LAB 1", "Electrical/Soldering"));
	    model.addItem(new Item("Soldering Iron", "WORKSHOP 4", "Electrical/Soldering"));
	    model.addItem(new Item("Soldering Iron", "WORKSHOP 5", "Electrical/Soldering"));

	    model.addNote("MC00000", "Test note");
	    model.addNote("MC00000", "Test note 2");
	    model.addNote("EM00001", "Test note");
	    model.addNote("ES00001", "Test note");

	    LocalDateTime dateBooked = LocalDateTime.of(2025, 10, 5, 8, 0); // 5/10/2025 8:00am 
	    LocalDateTime dateReturned = LocalDateTime.of(2025, 10, 5, 8, 30); // 5/10/2025 8:30am
	    model.addBooking(new Booking("111", "MC00000", dateBooked, dateReturned));
	    model.addBooking(new Booking("111", "MC00000", dateReturned.plusDays(2), dateReturned.plusDays(3)));
	    model.addBooking(new Booking("222", "EM00001", dateBooked, dateReturned));
	    model.addBooking(new Booking("222", "ES00001", dateBooked, dateReturned));
	} catch (Exception ex) {
	    Logger.getLogger(TestData.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    public static void printAll() {
	model.itemManager.printTable();
	model.locationManager.printTable();
	model.userManager.printTable();
	model.bookingManager.printTable();

    }

    public static void dropTables() {
	model.databaseManager.dropTableIfExists("HISTORYTABLE");
	model.databaseManager.dropTableIfExists("BOOKINGTABLE");
	model.databaseManager.dropTableIfExists("LOCATIONTABLE");
	model.databaseManager.dropTableIfExists("ITEMTABLE");
	model.databaseManager.dropTableIfExists("USERTABLE");
	model = new ModelManager();
    }

}
