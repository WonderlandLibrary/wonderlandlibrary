// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.entity.Entity;
import vip.Resolute.events.Event;

public class EventLivingUpdate extends Event<EventLivingUpdate>
{
    private Entity entity;
    
    public EventLivingUpdate(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
