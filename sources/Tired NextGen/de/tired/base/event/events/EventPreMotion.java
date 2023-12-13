package de.tired.base.event.events;

import de.tired.base.event.Event;

public class EventPreMotion extends Event {

    public double y;
    public float yaw;
    public float pitch;
    public boolean onGround;
    public boolean cancel;

    public EventPreMotion(double y, float yaw, float pitch, boolean onGround) {
        this.y = y;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancel = state;
    }
}
