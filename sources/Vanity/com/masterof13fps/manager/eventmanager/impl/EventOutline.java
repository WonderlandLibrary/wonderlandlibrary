package com.masterof13fps.manager.eventmanager.impl;

import com.masterof13fps.manager.eventmanager.Event;

public class EventOutline extends Event {

    public boolean outline;

    public boolean isOutline() {
        return outline;
    }

    public void setOutline(boolean outline) {
        this.outline = outline;
    }

    public EventOutline(boolean outline) {
        this.outline = outline;
    }
}
