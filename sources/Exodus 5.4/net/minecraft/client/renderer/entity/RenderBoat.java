/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderBoat
extends Render<EntityBoat> {
    protected ModelBase modelBoat = new ModelBoat();
    private static final ResourceLocation boatTextures = new ResourceLocation("textures/entity/boat.png");

    @Override
    public void doRender(EntityBoat entityBoat, double d, double d2, double d3, float f, float f2) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)d, (float)d2 + 0.25f, (float)d3);
        GlStateManager.rotate(180.0f - f, 0.0f, 1.0f, 0.0f);
        float f3 = (float)entityBoat.getTimeSinceHit() - f2;
        float f4 = entityBoat.getDamageTaken() - f2;
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f3 > 0.0f) {
            GlStateManager.rotate(MathHelper.sin(f3) * f3 * f4 / 10.0f * (float)entityBoat.getForwardDirection(), 1.0f, 0.0f, 0.0f);
        }
        float f5 = 0.75f;
        GlStateManager.scale(f5, f5, f5);
        GlStateManager.scale(1.0f / f5, 1.0f / f5, 1.0f / f5);
        this.bindEntityTexture(entityBoat);
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        this.modelBoat.render(entityBoat, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender(entityBoat, d, d2, d3, f, f2);
    }

    public RenderBoat(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.5f;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBoat entityBoat) {
        return boatTextures;
    }
}

