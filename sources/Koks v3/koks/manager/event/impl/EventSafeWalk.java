package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 05:36
 */
public class EventSafeWalk extends Event {
    boolean safe;

    public boolean isSafe() {
        return safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public EventSafeWalk(boolean safe) {
        this.safe = safe;
    }
}
