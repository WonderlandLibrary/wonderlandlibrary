package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author kroko
 * @created on 20.10.2020 : 15:24
 */
public class EventOutline extends Event {

    boolean isOutline;
    public EventOutline(boolean isOutline) {
        this.isOutline = isOutline;
    }

    public boolean isOutline() {
        return isOutline;
    }

    public void setOutline(boolean outline) {
        isOutline = outline;
    }
}
