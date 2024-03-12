package xyz.cucumber.base.events.ext;

import xyz.cucumber.base.events.Event;

public class EventKnockBack extends Event {

	public double motion = 0.6;
	public boolean full, strong, reduceY;
	public int power, postPower;
	
	
	
	
	public int getPostPower() {
		return postPower;
	}

	public void setPostPower(int postPower) {
		this.postPower = postPower;
	}

	public boolean isReduceY() {
		return reduceY;
	}

	public void setReduceY(boolean reduceY) {
		this.reduceY = reduceY;
	}

	public boolean isStrong() {
		return strong;
	}

	public void setStrong(boolean strong) {
		this.strong = strong;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public boolean isFull() {
		return full;
	}

	public void setFull(boolean full) {
		this.full = full;
	}

	public double getMotion() {
		return motion;
	}

	public void setMotion(double motion) {
		this.motion = motion;
	}

	public EventKnockBack(double motion, boolean full, int power, int postPower, boolean strong, boolean reduceY) {
		this.motion = motion;
		this.full = full;
		this.power = power;
		this.postPower = postPower;
		this.strong = strong;
		this.reduceY = reduceY;
	}
}
