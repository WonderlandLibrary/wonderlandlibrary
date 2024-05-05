package club.dortware.client.event.impl;

import club.dortware.client.event.Event;

public class KeyboardEvent extends Event {

    private final int key;

    public KeyboardEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
