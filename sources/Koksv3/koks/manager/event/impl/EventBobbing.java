package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author kroko
 * @created on 20.10.2020 : 14:53
 */
public class EventBobbing extends Event {
    float bobbing;

    public EventBobbing(float bobbing) {
        this.bobbing = bobbing;
    }

    public float getBobbing() {
        return bobbing;
    }

    public void setBobbing(float bobbing) {
        this.bobbing = bobbing;
    }
}
