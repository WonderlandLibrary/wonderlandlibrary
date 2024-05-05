// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.events.impl;

import net.minecraft.entity.Entity;
import vip.Resolute.events.Event;

public class EventStep extends Event<EventStep>
{
    public float stepHeight;
    public Entity entity;
    
    public EventStep(final float stepHeight, final Entity entity) {
        this.stepHeight = stepHeight;
        this.entity = entity;
    }
    
    public float getStepHeight() {
        return this.stepHeight;
    }
    
    public void setStepHeight(final float stepHeight) {
        this.stepHeight = stepHeight;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }
}
