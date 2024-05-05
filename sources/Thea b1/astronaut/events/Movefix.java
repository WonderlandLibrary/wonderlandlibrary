package astronaut.events;


import eventapi.events.Event;

public class Movefix implements Event {

    private float Yaw;

    private float friction;
    public boolean Cancellable;

    public Movefix(float rotationYaw, float friction) {
        Yaw = rotationYaw;
        this.friction = friction;
    }

    public boolean isCancellable() {
        return Cancellable;
    }

    public void setCancellable(boolean cancellable) {
        Cancellable = cancellable;
    }

    public float getFriction() {
        return friction;
    }

    public float getYaw() {
        return Yaw;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public void setYaw(float yaw) {
        Yaw = yaw;
    }
}
