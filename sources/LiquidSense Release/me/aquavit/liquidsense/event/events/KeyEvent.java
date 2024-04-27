package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.Event;

public class KeyEvent extends Event {
    private int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}