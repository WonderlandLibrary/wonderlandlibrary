package de.tired.base.event.events;

import de.tired.base.event.Event;

public class EventLook extends Event {

    private float[] rotations;

    public EventLook(final float[] rotations) {
        this.rotations = rotations;
    }

    public final float[] getRotations() {
        return rotations;
    }

    public final void setRotations(final float[] rotations) {
        this.rotations = rotations;
    }

    public final void setYaw(final float yaw) {
        this.rotations[0] = yaw;
    }

    public final void setPitch(final float pitch) {
        this.rotations[1] = pitch;
    }

}

