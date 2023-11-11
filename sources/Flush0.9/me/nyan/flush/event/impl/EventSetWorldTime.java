package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;

public class EventSetWorldTime extends Event {
    private long time;

    public EventSetWorldTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}