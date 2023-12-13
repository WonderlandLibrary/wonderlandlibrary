package Scov.events.player;

import Scov.events.Cancellable;

public class EventKeyPress extends Cancellable {
	
    private int key;

    public EventKeyPress(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}
