// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event.impl.render;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import dev.tenacity.event.Event;

public class RendererLivingEntityEvent extends StateEvent
{
    private final EntityLivingBase entity;
    private final RendererLivingEntity<?> renderer;
    private final float partialTicks;
    private final double x;
    private final double y;
    private final double z;
    
    public EntityLivingBase getEntity() {
        return this.entity;
    }
    
    public RendererLivingEntity<?> getRenderer() {
        return this.renderer;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public RendererLivingEntityEvent(final EntityLivingBase entity, final RendererLivingEntity<?> renderer, final float partialTicks, final double x, final double y, final double z) {
        this.entity = entity;
        this.renderer = renderer;
        this.partialTicks = partialTicks;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
