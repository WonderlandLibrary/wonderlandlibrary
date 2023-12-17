// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;

public class RenderHusk extends RenderZombie
{
    private static final ResourceLocation HUSK_ZOMBIE_TEXTURES;
    
    public RenderHusk(final RenderManager p_i47204_1_) {
        super(p_i47204_1_);
    }
    
    @Override
    protected void preRenderCallback(final EntityZombie entitylivingbaseIn, final float partialTickTime) {
        final float f = 1.0625f;
        GlStateManager.scale(1.0625f, 1.0625f, 1.0625f);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityZombie entity) {
        return RenderHusk.HUSK_ZOMBIE_TEXTURES;
    }
    
    static {
        HUSK_ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/husk.png");
    }
}
