package xyz.cucumber.base.events.ext;

import xyz.cucumber.base.events.Event;

public class EventLook extends Event{
	private float yaw,pitch;

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

	public EventLook(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	
}
