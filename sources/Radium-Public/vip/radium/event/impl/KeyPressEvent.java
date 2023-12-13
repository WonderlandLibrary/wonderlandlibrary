package vip.radium.event.impl;

import vip.radium.event.Event;

public final class KeyPressEvent implements Event {

    private final int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

}
