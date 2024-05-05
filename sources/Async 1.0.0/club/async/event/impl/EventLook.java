package club.async.event.impl;

import club.async.event.Event;

public class EventLook extends Event {

    private float[] rotations;
    private float[] prevRotations;

    public EventLook(float[] rotations, float[] prevRotations) {
        this.rotations = rotations;
        this.prevRotations = prevRotations;
    }

    public final float[] getRotations() {
        return rotations;
    }
    public final void setRotations(float[] rotations) {
        this.rotations = rotations;
    }
    public final float[] getPrevRotations() {
        return prevRotations;
    }
    public final void setPrevRotations(float[] prevRotations) {
        this.prevRotations = prevRotations;
    }

}
