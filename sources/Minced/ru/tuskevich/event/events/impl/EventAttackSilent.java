// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.entity.Entity;
import ru.tuskevich.event.events.callables.EventCancellable;

public class EventAttackSilent extends EventCancellable
{
    private final Entity targetEntity;
    
    public EventAttackSilent(final Entity targetEntity) {
        this.targetEntity = targetEntity;
    }
    
    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}
