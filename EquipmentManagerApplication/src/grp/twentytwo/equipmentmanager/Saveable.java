package grp.twentytwo.equipmentmanager;

import java.io.Serializable;

/**
 * Extends the Serializable interface and requires an object to have a save and
 * load function.
 *
 * @author fmw5088
 */
public interface Saveable extends Serializable {

    public void save();

    public void load();
}
