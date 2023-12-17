// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.client.renderer.entity.layers.LayerStrayClothing;
import net.minecraft.util.ResourceLocation;

public class RenderStray extends RenderSkeleton
{
    private static final ResourceLocation STRAY_SKELETON_TEXTURES;
    
    public RenderStray(final RenderManager p_i47191_1_) {
        super(p_i47191_1_);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerStrayClothing(this));
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final AbstractSkeleton entity) {
        return RenderStray.STRAY_SKELETON_TEXTURES;
    }
    
    static {
        STRAY_SKELETON_TEXTURES = new ResourceLocation("textures/entity/skeleton/stray.png");
    }
}
