package Reality.Realii.event.events.world;

import Reality.Realii.event.Event;

public class EventJumpFix extends Event {
    private float yaw;

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public EventJumpFix(float yaw) {
        this.yaw = yaw;
    }
}
