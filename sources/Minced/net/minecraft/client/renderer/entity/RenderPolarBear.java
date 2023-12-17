// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPolarBear;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityPolarBear;

public class RenderPolarBear extends RenderLiving<EntityPolarBear>
{
    private static final ResourceLocation POLAR_BEAR_TEXTURE;
    
    public RenderPolarBear(final RenderManager p_i47197_1_) {
        super(p_i47197_1_, new ModelPolarBear(), 0.7f);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityPolarBear entity) {
        return RenderPolarBear.POLAR_BEAR_TEXTURE;
    }
    
    @Override
    public void doRender(final EntityPolarBear entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected void preRenderCallback(final EntityPolarBear entitylivingbaseIn, final float partialTickTime) {
        GlStateManager.scale(1.2f, 1.2f, 1.2f);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
    
    static {
        POLAR_BEAR_TEXTURE = new ResourceLocation("textures/entity/bear/polarbear.png");
    }
}
