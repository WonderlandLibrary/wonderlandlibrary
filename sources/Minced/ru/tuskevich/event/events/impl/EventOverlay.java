// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.events.callables.EventCancellable;

public class EventOverlay extends EventCancellable implements Event
{
    private final OverlayType overlayType;
    
    public EventOverlay(final OverlayType overlayType) {
        this.overlayType = overlayType;
    }
    
    public OverlayType getOverlayType() {
        return this.overlayType;
    }
    
    public enum OverlayType
    {
        TotemAnimation, 
        Fire, 
        BossBar, 
        Fog, 
        Light;
    }
}
