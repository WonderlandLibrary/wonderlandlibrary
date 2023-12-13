package today.flux.event;

import com.darkmagician6.eventapi.events.Event;

/**
 * Created by John on 2017/07/20.
 */
public class KeyPressEvent implements Event {

    public int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }
}
