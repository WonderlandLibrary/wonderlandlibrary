package xyz.cucumber.base.events;

public class Event {
	private boolean cancelled;
	private EventType type = EventType.PRE;
	
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}

}
