package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;

public class EventKey extends Event {
    private final int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
