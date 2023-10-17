package graphite.impl.event.game;

import graphite.impl.event.Event;

public class KeyPressEvent extends Event {
    private int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }

    public int getKeyPressed() {
        return key;
    }
}