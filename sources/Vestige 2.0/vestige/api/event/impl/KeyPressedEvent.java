package vestige.api.event.impl;

import lombok.Getter;
import vestige.api.event.Event;

@Getter
public class KeyPressedEvent extends Event {

    private int key;

    public KeyPressedEvent(int key) {
        this.key = key;
    }

}
