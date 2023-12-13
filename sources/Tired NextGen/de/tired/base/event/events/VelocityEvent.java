package de.tired.base.event.events;

import de.tired.base.event.Event;

public class VelocityEvent extends Event {

	public boolean flag, sprint;
	public double motion;

	public VelocityEvent(boolean flag, double motion, boolean sprint) {
		this.flag = flag;
		this.sprint = sprint;
		this.motion = motion;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public boolean isSprint() {
		return sprint;
	}
	public void setSprint(boolean sprint) {
		this.sprint = sprint;
	}
	public double getMotion() {
		return motion;
	}
	public void setMotion(double motion) {
		this.motion = motion;
	}
}
