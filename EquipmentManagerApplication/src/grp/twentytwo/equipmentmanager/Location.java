package grp.twentytwo.equipmentmanager;

/**
 *
 * @author ppj1707
 */
public class Location {

    private String id;
    private String name;

    public Location(String id, String name) {
        this.id = id;
        this.name = name.toUpperCase();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
