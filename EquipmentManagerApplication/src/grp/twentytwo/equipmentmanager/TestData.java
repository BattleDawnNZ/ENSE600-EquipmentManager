package grp.twentytwo.equipmentmanager;

import grp.twentytwo.equipmentmanager.User.SecurityLevels;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ppj1707
 */
public class TestData {

    private static ModelManager m = new ModelManager();

    public static void main(String[] args) {
        TestData.m.setupManagers();
        TestData.dropTables();
        TestData.m.setupManagers();
        TestData.generate();
        TestData.printAll();
    }

    public static void generate() {

        m.locationManager.addLocation("WORKSHOP 1");
        m.locationManager.addLocation("Workshop 2");
        m.locationManager.addLocation("Electrical Lab 1");

        m.userManager.addUser(new Manager("111", "Bob", "123123"));
        m.userManager.addUser(new Employee("222", "Sally", "123123"));
        m.userManager.addUser(new Guest("333", "Fred", "123123"));

        m.itemManager.addItem(new Item("R9000 Universal Laser Cutter", "Workshop 1", "Manufacturing/Cutting"));
        m.itemManager.addItem(new Item("Ender 3D Printer", "Workshop 2", "Manufacturing/Additive"));
        m.itemManager.addItem(new Item("Duratech True RMS Multimeter", "Electrical Lab 1", "Electrical/Measurement"));
        m.itemManager.addItem(new Item("Soldering Iron", "Electrical Lab 1", "Electrical/Soldering"));

        m.historyManager.addHistory(new History("MC0", "Test note"));
        m.historyManager.addHistory(new History("MC0", "Test note2"));
        m.historyManager.addHistory(new History("EM1", "Test note"));
        m.historyManager.addHistory(new History("ES1", "Test note"));

        LocalDateTime dateBooked = LocalDateTime.of(2025, 10, 5, 8, 0); // 5/10/2025 8:00am 
        LocalDateTime dateReturned = LocalDateTime.of(2025, 10, 5, 8, 30); // 5/10/2025 8:30am
        try {
            m.bookingManager.issueItem(new Booking("111", "MC0", dateBooked, dateReturned));
            m.bookingManager.issueItem(new Booking("111", "MC0", dateReturned.plusDays(2), dateReturned.plusDays(3)));
            m.bookingManager.issueItem(new Booking("222", "EM1", dateBooked, dateReturned));
            m.bookingManager.issueItem(new Booking("222", "ES1", dateBooked, dateReturned));
        } catch (InvalidBookingRangeException ex) {
            Logger.getLogger(TestData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void printAll() {
        m.itemManager.printTable();
        m.locationManager.printTable();
        m.userManager.printTable();
        m.bookingManager.printTable();

    }

    public static void dropTables() {
        m.databaseManager.dropTableIfExists("HISTORYTABLE");
        m.databaseManager.dropTableIfExists("BOOKINGTABLE");
        m.databaseManager.dropTableIfExists("LOCATIONTABLE");
        m.databaseManager.dropTableIfExists("ITEMTABLE");
        m.databaseManager.dropTableIfExists("USERTABLE");
        m = new ModelManager();
    }

}
