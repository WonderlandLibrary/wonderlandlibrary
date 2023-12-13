package summer.cheat.eventsystem.events.player;

import summer.cheat.eventsystem.Event;

public class EventStep extends Event {

	private float stepHeight;
	private double heightStepped;
	private boolean pre;

	public EventStep(float stepHeight) {
		this.stepHeight = stepHeight;
		pre = true;
	}

	public double getHeightStepped() {
		return heightStepped;
	}

	public void setHeightStepped(double heightStepped) {
		this.heightStepped = heightStepped;
	}

	public boolean isPre() {
		return pre;
	}

	public void setPost() {
		pre = false;
	}

	public float getStepHeight() {
		return stepHeight;
	}

	public void setStepHeight(float stepHeight) {
		this.stepHeight = stepHeight;
	}

}
