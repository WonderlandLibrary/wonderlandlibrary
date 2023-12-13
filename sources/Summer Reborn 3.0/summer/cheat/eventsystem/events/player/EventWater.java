package summer.cheat.eventsystem.events.player;

import summer.cheat.eventsystem.Event;

public class EventWater extends Event {

	private boolean canBePushed;
	
	public EventWater() {
		super();
	}
	
	public EventWater(boolean pushed) {
		super();
	}
	
	public boolean canBePushed() {
		return canBePushed;
	}

	public void setCanBePushed(boolean canBePushed) {
		this.canBePushed = canBePushed;
	}
	
}


