package wtf.monsoon.api.event.impl;

import wtf.monsoon.api.event.Event;

public class EventRenderPlayer extends Event {
	
	public float yaw;
    public float pitch;
    public float yawChange;
    private float partialTicks;

    public EventRenderPlayer(float yaw, float pitch, float yawChange, float partialTicks) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.yawChange = yawChange;
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

}
