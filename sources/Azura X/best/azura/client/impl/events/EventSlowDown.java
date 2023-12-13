package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventSlowDown implements NamedEvent {

    private float strafe, forward;
    private boolean stopSprint;

    public EventSlowDown(float strafe, float forward, boolean stopSprint) {
        this.strafe = strafe;
        this.forward = forward;
        this.stopSprint = stopSprint;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getForward() {
        return forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public boolean isStopSprint() {
        return stopSprint;
    }

    public void setStopSprint(boolean stopSprint) {
        this.stopSprint = stopSprint;
    }

    @Override
    public String name() {
        return "slowDown";
    }
}