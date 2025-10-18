package grp.twentytwo.equipmentmanager;

import grp.twentytwo.equipmentmanager.User.SecurityLevels;

/**
 *
 * @author ppj1707
 */
public class TestData {

    private static ModelManager m = new ModelManager();

    public static void main(String[] args) {
        TestData.m.SetupManagers();
        TestData.dropTables();
        TestData.m.SetupManagers();
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

    }

    public static void printAll() {
        m.itemManager.printTable();
        m.locationManager.printTable();
        m.userManager.printTable();

    }

    public static void dropTables() {
        //m.databaseManager.dropTableIfExists("HISTORYTABLE");
        m.databaseManager.dropTableIfExists("LOCATIONTABLE");
        //m.databaseManager.dropTableIfExists("BOOKINGTABLE");
        m.databaseManager.dropTableIfExists("ITEMTABLE");
        m.databaseManager.dropTableIfExists("USERTABLE");
        m = new ModelManager();
    }

}
