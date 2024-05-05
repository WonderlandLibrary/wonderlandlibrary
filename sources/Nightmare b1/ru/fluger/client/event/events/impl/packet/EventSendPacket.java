// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.packet;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventSendPacket extends EventCancellable
{
    private final ht<?> packet;
    
    public EventSendPacket(final ht packet) {
        this.packet = (ht<?>)packet;
    }
    
    public ht getPacket() {
        return this.packet;
    }
}
