// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.Entity;
import com.elementars.eclient.event.Event;

public class EventThrow extends Event
{
    private Entity thrower;
    private EntityThrowable entity;
    private float rotation;
    
    public EventThrow(final Entity entityLivingBase, final EntityThrowable entityThrowable, final float rotation) {
        this.thrower = entityLivingBase;
        this.entity = entityThrowable;
        this.rotation = rotation;
    }
    
    public Entity getThrower() {
        return this.thrower;
    }
    
    public EntityThrowable getEntity() {
        return this.entity;
    }
    
    public float getRotation() {
        return this.rotation;
    }
}
