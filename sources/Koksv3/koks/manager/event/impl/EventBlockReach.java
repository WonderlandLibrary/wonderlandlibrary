package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author kroko
 * @created on 26.09.2020 : 13:41
 */
public class EventBlockReach extends Event {

    public float reach;

    public float getReach() {
        return reach;
    }

    public void setReach(float reach) {
        this.reach = reach;
    }

    public EventBlockReach(float reach) {
        this.reach = reach;
    }
}
