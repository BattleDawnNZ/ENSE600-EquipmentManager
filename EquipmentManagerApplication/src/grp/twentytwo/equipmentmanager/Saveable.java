package grp.twentytwo.equipmentmanager;

import java.io.Serializable;

/**
 *
 * @author cwitt
 */
public interface Saveable extends Serializable {

    public void save();

    public void load();
}
