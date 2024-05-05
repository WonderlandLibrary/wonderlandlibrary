package club.async.event.impl;

import club.async.event.Event;

public class EventRenderPitch extends Event {

    private float pitch, prevPitch;

    public EventRenderPitch(float pitch, float prevPitch) {
        this.pitch = pitch;
        this.prevPitch = prevPitch;
    }

    public float getPitch() {
        return pitch;
    }

    public float getPrevPitch() {
        return prevPitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        this.prevPitch = pitch;
    }

}
