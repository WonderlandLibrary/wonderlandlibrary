package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventJump implements NamedEvent {
    private float speed, jumpMotion, yaw;
    private boolean cancelled;
    public EventJump(float speed, float jumpMotion, float yaw) {
        this.speed = speed;
        this.jumpMotion = jumpMotion;
        this.yaw = yaw;
    }
    public boolean isCancelled() {
        return cancelled;
    }
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public float getJumpMotion() {
        return jumpMotion;
    }
    public void setJumpMotion(float jumpMotion) {
        this.jumpMotion = jumpMotion;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    @Override
    public String name() {
        return "jump";
    }
}