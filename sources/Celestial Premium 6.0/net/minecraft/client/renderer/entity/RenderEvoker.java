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
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpellcasterIllager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

public class RenderEvoker
extends RenderLiving<EntityMob> {
    private static final ResourceLocation field_191338_a = new ResourceLocation("textures/entity/illager/evoker.png");

    public RenderEvoker(RenderManager p_i47207_1_) {
        super(p_i47207_1_, new ModelIllager(0.0f, 0.0f, 64, 64), 0.5f);
        this.addLayer(new LayerHeldItem(this){

            @Override
            public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
                if (((EntitySpellcasterIllager)entitylivingbaseIn).func_193082_dl()) {
                    super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                }
            }

            @Override
            protected void func_191361_a(EnumHandSide p_191361_1_) {
                ((ModelIllager)this.livingEntityRenderer.getMainModel()).func_191216_a(p_191361_1_).postRender(0.0625f);
            }
        });
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMob entity) {
        return field_191338_a;
    }

    @Override
    protected void preRenderCallback(EntityMob entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375f;
        GlStateManager.scale(0.9375f, 0.9375f, 0.9375f);
    }
}

