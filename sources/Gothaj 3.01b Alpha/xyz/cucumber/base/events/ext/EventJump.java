package xyz.cucumber.base.events.ext;

import xyz.cucumber.base.events.Event;

public class EventJump extends Event{
	private float yaw;
	private double motionY;
	
	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public EventJump(float yaw, double motionY) {
		this.yaw = yaw;
		this.motionY = motionY;
	}

	public double getMotionY() {
		return motionY;
	}

	public void setMotionY(double motionY) {
		this.motionY = motionY;
	}

	
}
