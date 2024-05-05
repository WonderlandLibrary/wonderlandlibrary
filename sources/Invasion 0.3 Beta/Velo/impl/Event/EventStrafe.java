package Velo.impl.Event;

import Velo.api.Event.Event;

public class EventStrafe extends Event<EventStrafe> {
	
	public static float yaw;
	public static float strafe, forward, friction;
	
	public EventStrafe() {
		
	}

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
}
