package grp.twentytwo.equipmentmanager;

import java.io.Serializable;

/**
 * Extends the Serializable interface and requires an object to have a save and
 * load function.
 *
 * @author cwitt
 */
public interface Saveable extends Serializable {

    public void save();

    public void load();
}
