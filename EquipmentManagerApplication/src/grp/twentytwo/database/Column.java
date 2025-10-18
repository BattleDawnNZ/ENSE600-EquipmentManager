package grp.twentytwo.database;

/**
 *
 * @author ppj1707
 */
public class Column {

    private final String name;
    private final String type;
    public String data;

    public Column(String name, String type, String data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
