// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityMagmaCube;

public class RenderMagmaCube extends RenderLiving<EntityMagmaCube>
{
    private static final ResourceLocation MAGMA_CUBE_TEXTURES;
    
    public RenderMagmaCube(final RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelMagmaCube(), 0.25f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityMagmaCube entity) {
        return RenderMagmaCube.MAGMA_CUBE_TEXTURES;
    }
    
    @Override
    protected void preRenderCallback(final EntityMagmaCube entitylivingbaseIn, final float partialTickTime) {
        final int i = entitylivingbaseIn.getSlimeSize();
        final float f = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (i * 0.5f + 1.0f);
        final float f2 = 1.0f / (f + 1.0f);
        GlStateManager.scale(f2 * i, 1.0f / f2 * i, f2 * i);
    }
    
    static {
        MAGMA_CUBE_TEXTURES = new ResourceLocation("textures/entity/slime/magmacube.png");
    }
}
