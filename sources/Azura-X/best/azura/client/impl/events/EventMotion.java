package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventMotion implements NamedEvent {

    public double x, y, z;
    public float yaw, pitch;
    public boolean sneaking, sprinting, onGround;
    private String state;

    public EventMotion(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sneaking, boolean sprinting) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.sneaking = sneaking;
        this.sprinting = sprinting;
        this.state = "Pre";
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isPre() {
        return state.equals("Pre");
    }

    public boolean isUpdate() {
        return state.equals("Update");
    }

    public boolean isPost() {
        return state.equals("Post");
    }

    public void apply(float[] rotations) {
        this.yaw = rotations[0];
        this.pitch = rotations[1];
    }

    @Override
    public String name() {
        return "motion";
    }
}
