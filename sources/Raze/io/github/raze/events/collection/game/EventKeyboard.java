package io.github.raze.events.collection.game;

import io.github.raze.events.system.Event;

public class EventKeyboard extends Event {

    public int keyCode;

    public EventKeyboard(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }
}
