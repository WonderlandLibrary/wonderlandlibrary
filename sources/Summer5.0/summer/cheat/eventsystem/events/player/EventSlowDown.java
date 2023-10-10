package summer.cheat.eventsystem.events.player;

import summer.cheat.eventsystem.Event;

public class EventSlowDown extends Event {

	private float forward;
	private float strafe;
	
	public EventSlowDown(float forward, float strafe) {
		super();
		this.forward = forward;
		this.strafe = strafe;
	}

	public float getForward() {
		return forward;
	}

	public void setForward(float forward) {
		this.forward = forward;
	}

	public float getStrafe() {
		return strafe;
	}

	public void setStrafe(float strafe) {
		this.strafe = strafe;
	}
	
}
