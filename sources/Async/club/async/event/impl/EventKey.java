package club.async.event.impl;

import club.async.event.Event;

public class EventKey extends Event {

    private final int key;

    public EventKey(int key) {
        this.key = key;
    }

    public final int getKey() {
        return key;
    }

}
