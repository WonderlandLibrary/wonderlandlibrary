// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.entity.EntityLivingBase;
import vip.Resolute.events.Event;

public class EventEntitySwing extends Event<EventEntitySwing>
{
    private final int entityId;
    
    public EventEntitySwing(final EntityLivingBase entity) {
        this.entityId = entity.getEntityId();
    }
    
    public int getEntityId() {
        return this.entityId;
    }
}
