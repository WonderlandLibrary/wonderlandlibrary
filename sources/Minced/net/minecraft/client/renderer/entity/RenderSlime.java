// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerSlimeGel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntitySlime;

public class RenderSlime extends RenderLiving<EntitySlime>
{
    private static final ResourceLocation SLIME_TEXTURES;
    
    public RenderSlime(final RenderManager p_i47193_1_) {
        super(p_i47193_1_, new ModelSlime(16), 0.25f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerSlimeGel(this));
    }
    
    @Override
    public void doRender(final EntitySlime entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        this.shadowSize = 0.25f * entity.getSlimeSize();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected void preRenderCallback(final EntitySlime entitylivingbaseIn, final float partialTickTime) {
        final float f = 0.999f;
        GlStateManager.scale(0.999f, 0.999f, 0.999f);
        final float f2 = (float)entitylivingbaseIn.getSlimeSize();
        final float f3 = (entitylivingbaseIn.prevSquishFactor + (entitylivingbaseIn.squishFactor - entitylivingbaseIn.prevSquishFactor) * partialTickTime) / (f2 * 0.5f + 1.0f);
        final float f4 = 1.0f / (f3 + 1.0f);
        GlStateManager.scale(f4 * f2, 1.0f / f4 * f2, f4 * f2);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntitySlime entity) {
        return RenderSlime.SLIME_TEXTURES;
    }
    
    static {
        SLIME_TEXTURES = new ResourceLocation("textures/entity/slime/slime.png");
    }
}
