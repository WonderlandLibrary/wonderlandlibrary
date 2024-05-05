// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.render;

import ru.fluger.client.event.types.EventType;
import ru.fluger.client.event.events.Event;

public class EventRenderScoreboard implements Event
{
    private EventType state;
    
    public EventRenderScoreboard(final EventType state) {
        this.state = state;
    }
    
    public EventType getState() {
        return this.state;
    }
    
    public void setState(final EventType state) {
        this.state = state;
    }
    
    public boolean isPre() {
        return this.state == EventType.PRE;
    }
}
