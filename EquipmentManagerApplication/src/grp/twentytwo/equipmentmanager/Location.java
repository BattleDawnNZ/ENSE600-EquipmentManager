package grp.twentytwo.equipmentmanager;

/**
 *
 * @author ppj1707
 */
public class Location {

    private final String id;
    private String name;

    Location(String id, String name) { // Package private for official locations only
        this.id = id;
        this.name = name.toUpperCase();
    }

    public Location(String name) {
        this(null, name);
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
