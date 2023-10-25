/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelIllager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderIllusionIllager
extends RenderLiving<EntityMob> {
    private static final ResourceLocation field_193121_a = new ResourceLocation("textures/entity/illager/illusionist.png");

    public RenderIllusionIllager(RenderManager p_i47477_1_) {
        super(p_i47477_1_, new ModelIllager(0.0f, 0.0f, 64, 64), 0.5f);
        this.addLayer(new LayerHeldItem(this){

            @Override
            public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
                if (((EntityIllusionIllager)entitylivingbaseIn).func_193082_dl() || ((EntityIllusionIllager)entitylivingbaseIn).func_193096_dj()) {
                    super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }

            @Override
            protected void func_191361_a(EnumHandSide p_191361_1_) {
                ((ModelIllager)this.livingEntityRenderer.getMainModel()).func_191216_a(p_191361_1_).postRender(0.0625f);
            }
        });
        ((ModelIllager)this.getMainModel()).field_193775_b.showModel = true;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMob entity) {
        return field_193121_a;
    }

    @Override
    protected void preRenderCallback(EntityMob entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375f;
        GlStateManager.scale(0.9375f, 0.9375f, 0.9375f);
    }

    @Override
    public void doRender(EntityMob entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.isInvisible()) {
            Vec3d[] avec3d = ((EntityIllusionIllager)entity).func_193098_a(partialTicks);
            float f = this.handleRotationFloat(entity, partialTicks);
            for (int i = 0; i < avec3d.length; ++i) {
                super.doRender(entity, x + avec3d[i].x + (double)MathHelper.cos((float)i + f * 0.5f) * 0.025, y + avec3d[i].y + (double)MathHelper.cos((float)i + f * 0.75f) * 0.0125, z + avec3d[i].z + (double)MathHelper.cos((float)i + f * 0.7f) * 0.025, entityYaw, partialTicks);
            }
        } else {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    public void renderName(EntityMob entity, double x, double y, double z) {
        super.renderName(entity, x, y, z);
    }

    @Override
    protected boolean func_193115_c(EntityMob p_193115_1_) {
        return true;
    }
}

