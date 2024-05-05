package xyz.cucumber.base.events.ext;

import xyz.cucumber.base.events.Event;

public class EventMoveFlying extends Event{
	private float yaw;
	 private float strafe, forward, friction;

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
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

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}

	public EventMoveFlying(float yaw, float strafe, float forward, float friction) {
		super();
		this.yaw = yaw;
		this.strafe = strafe;
		this.forward = forward;
		this.friction = friction;
	}

	
	
	 
}
