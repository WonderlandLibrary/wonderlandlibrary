package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventStrafe implements NamedEvent {

    public float forward, strafe, yaw, friction;
    private boolean cancelled = false;

    public EventStrafe(float forward, float strafe, float yaw, float friction) {
        this.forward = forward;
        this.strafe = strafe;
        this.yaw = yaw;
        this.friction = friction;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String name() {
        return "strafe";
    }
}
