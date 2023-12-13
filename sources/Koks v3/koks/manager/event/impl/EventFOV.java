package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author kroko
 * @created on 26.09.2020 : 11:57
 */
public class EventFOV extends Event{
    float fov;

    public float getFOV() {
        return fov;
    }

    public void setFOV(float fov) {
        this.fov = fov;
    }

    public EventFOV(float fov) {
        this.fov = fov;
    }
}
