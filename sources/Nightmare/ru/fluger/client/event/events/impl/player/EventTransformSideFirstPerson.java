// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.player;

import ru.fluger.client.event.events.Event;

public class EventTransformSideFirstPerson implements Event
{
    private final vo enumHandSide;
    
    public EventTransformSideFirstPerson(final vo enumHandSide) {
        this.enumHandSide = enumHandSide;
    }
    
    public vo getEnumHandSide() {
        return this.enumHandSide;
    }
}
