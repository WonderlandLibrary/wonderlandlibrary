// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityMob;

public class RenderVindicator extends RenderLiving<EntityMob>
{
    private static final ResourceLocation VINDICATOR_TEXTURE;
    
    public RenderVindicator(final RenderManager p_i47189_1_) {
        super(p_i47189_1_, new ModelIllager(0.0f, 0.0f, 64, 64), 0.5f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerHeldItem(this) {
            @Override
            public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
                if (((EntityVindicator)entitylivingbaseIn).isAggressive()) {
                    super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
            
            @Override
            protected void translateToHand(final EnumHandSide p_191361_1_) {
                ((ModelIllager)this.livingEntityRenderer.getMainModel()).getArm(p_191361_1_).postRender(0.0625f);
            }
        });
    }
    
    @Override
    public void doRender(final EntityMob entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityMob entity) {
        return RenderVindicator.VINDICATOR_TEXTURE;
    }
    
    @Override
    protected void preRenderCallback(final EntityMob entitylivingbaseIn, final float partialTickTime) {
        final float f = 0.9375f;
        GlStateManager.scale(0.9375f, 0.9375f, 0.9375f);
    }
    
    static {
        VINDICATOR_TEXTURE = new ResourceLocation("textures/entity/illager/vindicator.png");
    }
}
