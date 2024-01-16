// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.packet;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventReceivePacket extends EventCancellable
{
    private ht<?> packet;
    
    public EventReceivePacket(final ht<?> packet) {
        this.packet = packet;
    }
    
    public ht<?> getPacket() {
        return this.packet;
    }
    
    public void setPacket(final ht<?> packet) {
        this.packet = packet;
    }
}
