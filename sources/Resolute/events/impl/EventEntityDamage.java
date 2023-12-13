// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.entity.Entity;
import vip.Resolute.events.Event;

public class EventEntityDamage extends Event<EventEntityDamage>
{
    Entity entity;
    private final double damage;
    
    public EventEntityDamage(final Entity entity, final double damage) {
        this.entity = entity;
        this.damage = damage;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public double getDamage() {
        return this.damage;
    }
}
