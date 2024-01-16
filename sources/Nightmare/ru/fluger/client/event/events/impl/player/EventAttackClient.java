// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.event.events.impl.player;

import ru.fluger.client.event.events.callables.EventCancellable;

public class EventAttackClient extends EventCancellable
{
    private final vg entity;
    private final boolean preAttack;
    
    public EventAttackClient(final vg targetEntity, final boolean preAttack) {
        this.entity = targetEntity;
        this.preAttack = preAttack;
    }
    
    public vg getTargetEntity() {
        return this.entity;
    }
    
    public boolean isPreAttack() {
        return this.preAttack;
    }
    
    public boolean isPostAttack() {
        return !this.preAttack;
    }
}
