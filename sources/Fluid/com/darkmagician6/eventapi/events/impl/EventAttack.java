// 
// Decompiled by Procyon v0.6.0
// 

package com.darkmagician6.eventapi.events.impl;

import net.minecraft.entity.Entity;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventAttack extends EventCancellable
{
    public Entity entity;
    
    public EventAttack(final Entity entity) {
        this.entity = entity;
    }
}
