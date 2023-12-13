// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import javax.annotation.Nullable;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import com.elementars.eclient.event.Event;

public class EventRenderEntity extends Event
{
    RenderLivingBase renderer;
    EntityLivingBase entity;
    ModelBase model;
    double x;
    double y;
    double z;
    float entityYaw;
    float partialTicks;
    
    public EventRenderEntity(@Nullable final RenderLivingBase renderer, final EntityLivingBase entity, @Nullable final ModelBase model, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        this.renderer = renderer;
        this.entity = entity;
        this.model = model;
        this.x = x;
        this.y = y;
        this.z = z;
        this.entityYaw = entityYaw;
        this.partialTicks = partialTicks;
    }
    
    public RenderLivingBase getRenderer() {
        return this.renderer;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public EntityLivingBase getEntity() {
        return this.entity;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public ModelBase getModel() {
        return this.model;
    }
    
    public float getEntityYaw() {
        return this.entityYaw;
    }
    
    @Override
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
