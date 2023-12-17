// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.callables;

import ru.tuskevich.event.events.Cancellable;
import ru.tuskevich.event.events.Event;

public abstract class EventCancellable implements Event, Cancellable
{
    private boolean cancelled;
    
    @Override
    public boolean isCanceled() {
        return this.cancelled;
    }
    
    @Override
    public void cancel() {
        this.cancelled = true;
    }
}
