// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events;

public abstract class EventStoppable implements Event
{
    private boolean stopped;
    
    public void stop() {
        this.stopped = true;
    }
    
    public boolean isStopped() {
        return this.stopped;
    }
}
