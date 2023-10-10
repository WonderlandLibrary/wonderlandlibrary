// 
// Decompiled by Procyon v0.5.36
// 

package net.lenni0451.eventapi.events.premade;

import net.lenni0451.eventapi.events.types.IStoppable;
import net.lenni0451.eventapi.events.IEvent;

public class EventStoppable implements IEvent, IStoppable
{
    private boolean stopped;
    
    public EventStoppable() {
        this.stopped = false;
    }
    
    @Override
    public boolean isStopped() {
        return this.stopped;
    }
    
    @Override
    public void setStopped(final boolean stopped) {
        this.stopped = stopped;
    }
}
