// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.player;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventPostAttackSilent extends EventCancellable
{
    private final vg targetEntity;
    
    public EventPostAttackSilent(final vg targetEntity) {
        this.targetEntity = targetEntity;
    }
    
    public vg getTargetEntity() {
        return this.targetEntity;
    }
}
