// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.packet;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventAttackSilent extends EventCancellable
{
    private final vg targetEntity;
    
    public EventAttackSilent(final vg targetEntity) {
        this.targetEntity = targetEntity;
    }
    
    public vg getTargetEntity() {
        return this.targetEntity;
    }
}
