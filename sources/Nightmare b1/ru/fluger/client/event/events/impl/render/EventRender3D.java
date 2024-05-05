// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.render;

import ru.fluger.client.event.events.Event;

public class EventRender3D implements Event
{
    private final float partialTicks;
    
    public EventRender3D(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
