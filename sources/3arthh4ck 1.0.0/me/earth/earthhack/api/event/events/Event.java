package me.earth.earthhack.api.event.events;

public abstract class Event
{
    boolean cancelled;

    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

    public boolean isCancelled()
    {
        return cancelled;
    }

}

