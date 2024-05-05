// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.render;

import ru.fluger.client.event.events.Event;

public class EventRender2D implements Event
{
    private final bit resolution;
    
    public EventRender2D(final bit resolution) {
        this.resolution = resolution;
    }
    
    public bit getResolution() {
        return this.resolution;
    }
}
