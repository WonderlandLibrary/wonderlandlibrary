// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import net.minecraft.entity.EntityLivingBase;
import com.elementars.eclient.event.Event;

public class LocalPlayerUpdateEvent extends Event
{
    EntityLivingBase entityLivingBase;
    
    public LocalPlayerUpdateEvent(final EntityLivingBase entity) {
        this.entityLivingBase = entity;
    }
    
    public EntityLivingBase getEntityLivingBase() {
        return this.entityLivingBase;
    }
}
