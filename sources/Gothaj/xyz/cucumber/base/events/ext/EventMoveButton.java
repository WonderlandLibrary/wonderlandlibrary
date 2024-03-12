package xyz.cucumber.base.events.ext;

import xyz.cucumber.base.events.Event;

public final class EventMoveButton extends Event {
	public boolean left;
	public boolean right;
	public boolean backward;
	public boolean forward;
	public boolean sneak;
	public boolean jump;

	public EventMoveButton(boolean button, boolean button2, boolean button3, boolean button4, boolean sneak, boolean jump) {
		this.left = button;
		this.right = button2;
		this.backward = button3;
		this.forward = button4;
		this.sneak = sneak;
		this.jump = jump;
	}
}
