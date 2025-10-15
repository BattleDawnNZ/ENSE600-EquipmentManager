package grp.twentytwo.equipmentmanagerapplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.AbstractButton;

/**
 *
 * @author fmw5088
 */
public class Speaker<T> {

    private LinkedList<Listener<T>> listeners;

    public Speaker() {
    }

    public Speaker(AbstractButton button) {
	button.addActionListener(passthrough());
    }

    private LinkedList<Listener<T>> getListeners() {
	if (listeners == null) {
	    listeners = new LinkedList<>();
	}
	return listeners;
    }

    public boolean addListener(Listener<T> newListener) {
	return getListeners().add(newListener);
    }

    public boolean removeListener(Listener<T> oldListener) {
	return getListeners().remove(oldListener);
    }

    public void notifyListeners(T newValue) {
	for (Listener<T> listener : getListeners()) {
	    listener.onNotify(newValue);
	}
    }

    public ActionListener passthrough() {
	return (ActionEvent e) -> {
	    notifyListeners((T) e);
	};
    }
}
