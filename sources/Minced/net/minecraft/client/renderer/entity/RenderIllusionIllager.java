// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelIllager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityMob;

public class RenderIllusionIllager extends RenderLiving<EntityMob>
{
    private static final ResourceLocation ILLUSIONIST;
    
    public RenderIllusionIllager(final RenderManager p_i47477_1_) {
        super(p_i47477_1_, new ModelIllager(0.0f, 0.0f, 64, 64), 0.5f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerHeldItem(this) {
            @Override
            public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
                if (((EntityIllusionIllager)entitylivingbaseIn).isSpellcasting() || ((EntityIllusionIllager)entitylivingbaseIn).isAggressive()) {
                    super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }
            
            @Override
            protected void translateToHand(final EnumHandSide p_191361_1_) {
                ((ModelIllager)this.livingEntityRenderer.getMainModel()).getArm(p_191361_1_).postRender(0.0625f);
            }
        });
        ((ModelIllager)this.getMainModel()).hat.showModel = true;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityMob entity) {
        return RenderIllusionIllager.ILLUSIONIST;
    }
    
    @Override
    protected void preRenderCallback(final EntityMob entitylivingbaseIn, final float partialTickTime) {
        final float f = 0.9375f;
        GlStateManager.scale(0.9375f, 0.9375f, 0.9375f);
    }
    
    @Override
    public void doRender(final EntityMob entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (entity.isInvisible()) {
            final Vec3d[] avec3d = ((EntityIllusionIllager)entity).getRenderLocations(partialTicks);
            final float f = this.handleRotationFloat(entity, partialTicks);
            for (int i = 0; i < avec3d.length; ++i) {
                super.doRender(entity, x + avec3d[i].x + MathHelper.cos(i + f * 0.5f) * 0.025, y + avec3d[i].y + MathHelper.cos(i + f * 0.75f) * 0.0125, z + avec3d[i].z + MathHelper.cos(i + f * 0.7f) * 0.025, entityYaw, partialTicks);
            }
        }
        else {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }
    
    @Override
    public void renderName(final EntityMob entity, final double x, final double y, final double z) {
        super.renderName(entity, x, y, z);
    }
    
    @Override
    protected boolean isVisible(final EntityMob p_193115_1_) {
        return true;
    }
    
    static {
        ILLUSIONIST = new ResourceLocation("textures/entity/illager/illusionist.png");
    }
}
