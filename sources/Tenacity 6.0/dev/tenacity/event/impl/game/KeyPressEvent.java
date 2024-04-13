package dev.tenacity.event.impl.game;

import dev.tenacity.event.Event;


public class KeyPressEvent extends Event {

    private final int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

}
