package wtf.diablo.events.impl;

import lombok.Getter;
import lombok.Setter;
import wtf.diablo.events.Event;

@Getter@Setter
public class KeyEvent extends Event {
    private int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
