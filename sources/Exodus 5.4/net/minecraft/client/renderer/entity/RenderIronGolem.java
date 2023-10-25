/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerIronGolemFlower;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class RenderIronGolem
extends RenderLiving<EntityIronGolem> {
    private static final ResourceLocation ironGolemTextures = new ResourceLocation("textures/entity/iron_golem.png");

    @Override
    protected ResourceLocation getEntityTexture(EntityIronGolem entityIronGolem) {
        return ironGolemTextures;
    }

    @Override
    protected void rotateCorpse(EntityIronGolem entityIronGolem, float f, float f2, float f3) {
        super.rotateCorpse(entityIronGolem, f, f2, f3);
        if ((double)entityIronGolem.limbSwingAmount >= 0.01) {
            float f4 = 13.0f;
            float f5 = entityIronGolem.limbSwing - entityIronGolem.limbSwingAmount * (1.0f - f3) + 6.0f;
            float f6 = (Math.abs(f5 % f4 - f4 * 0.5f) - f4 * 0.25f) / (f4 * 0.25f);
            GlStateManager.rotate(6.5f * f6, 0.0f, 0.0f, 1.0f);
        }
    }

    public RenderIronGolem(RenderManager renderManager) {
        super(renderManager, new ModelIronGolem(), 0.5f);
        this.addLayer(new LayerIronGolemFlower(this));
    }
}

