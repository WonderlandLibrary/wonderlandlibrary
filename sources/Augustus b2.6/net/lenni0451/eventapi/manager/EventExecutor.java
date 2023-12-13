// 
// Decompiled by Procyon v0.5.36
// 

package net.lenni0451.eventapi.manager;

import net.lenni0451.eventapi.listener.IEventListener;

class EventExecutor
{
    private final IEventListener eventListener;
    private final byte priority;
    
    public EventExecutor(final IEventListener eventListener, final byte priority) {
        this.eventListener = eventListener;
        this.priority = priority;
    }
    
    public IEventListener getEventListener() {
        return this.eventListener;
    }
    
    public byte getPriority() {
        return this.priority;
    }
}
