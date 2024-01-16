// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.player;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventReceiveMessage extends EventCancellable
{
    public String message;
    public boolean cancelled;
    
    public EventReceiveMessage(final String chat) {
        this.message = chat;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public void setCancelled(final boolean b) {
        this.cancelled = b;
    }
}
