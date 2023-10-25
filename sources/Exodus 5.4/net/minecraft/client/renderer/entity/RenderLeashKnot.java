/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.util.ResourceLocation;

public class RenderLeashKnot
extends Render<EntityLeashKnot> {
    private static final ResourceLocation leashKnotTextures = new ResourceLocation("textures/entity/lead_knot.png");
    private ModelLeashKnot leashKnotModel = new ModelLeashKnot();

    public RenderLeashKnot(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityLeashKnot entityLeashKnot) {
        return leashKnotTextures;
    }

    @Override
    public void doRender(EntityLeashKnot entityLeashKnot, double d, double d2, double d3, float f, float f2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translate((float)d, (float)d2, (float)d3);
        float f3 = 0.0625f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entityLeashKnot);
        this.leashKnotModel.render(entityLeashKnot, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, f3);
        GlStateManager.popMatrix();
        super.doRender(entityLeashKnot, d, d2, d3, f, f2);
    }
}

