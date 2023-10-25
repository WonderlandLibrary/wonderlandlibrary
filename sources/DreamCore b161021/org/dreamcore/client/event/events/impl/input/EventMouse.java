package org.dreamcore.client.event.events.impl.input;

import org.dreamcore.client.event.events.callables.EventCancellable;

public class EventMouse extends EventCancellable {

    private int key;

    public EventMouse(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
