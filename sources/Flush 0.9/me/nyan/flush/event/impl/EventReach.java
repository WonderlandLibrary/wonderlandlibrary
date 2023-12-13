package me.nyan.flush.event.impl;

import me.nyan.flush.event.Event;

public class EventReach extends Event {
    private double reach;

    public EventReach(double reach) {
        this.reach = reach;
    }

    public double getReach() {
        return reach;
    }

    public void setReach(double reach) {
        this.reach = reach;
    }
}
