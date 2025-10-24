package grp.twentytwo.guiapplication;

import java.util.LinkedList;

/**
 *
 * @author fmw5088
 */
public class Speaker<T> {

    private LinkedList<Listener<T>> listeners;

    public Speaker() {
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
}
