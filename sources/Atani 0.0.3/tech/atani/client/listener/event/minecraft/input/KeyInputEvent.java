package tech.atani.client.listener.event.minecraft.input;

import tech.atani.client.listener.event.Event;

public class KeyInputEvent extends Event {
    private final int key;

    public KeyInputEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
