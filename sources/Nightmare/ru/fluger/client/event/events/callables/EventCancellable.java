// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.callables;

import ru.fluger.client.event.events.Cancellable;
import ru.fluger.client.event.events.Event;

public abstract class EventCancellable implements Event, Cancellable
{
    private boolean cancelled;
    
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(final boolean state) {
        this.cancelled = state;
    }
}
