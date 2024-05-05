package xyz.cucumber.base.events.ext;

import xyz.cucumber.base.events.Event;

public class EventRenderRotation extends Event{

	private float yaw;
	private float pitch;
	
	public EventRenderRotation(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
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
