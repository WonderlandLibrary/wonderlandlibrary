package appu26j.events;

public class Event
{
	private boolean cancelled;
	
	public boolean isCancelled()
	{
		return this.cancelled;
	}
	
	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
}
