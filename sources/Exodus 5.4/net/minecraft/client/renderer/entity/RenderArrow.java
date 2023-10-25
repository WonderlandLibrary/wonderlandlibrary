/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderArrow
extends Render<EntityArrow> {
    private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");

    @Override
    public void doRender(EntityArrow entityArrow, double d, double d2, double d3, float f, float f2) {
        this.bindEntityTexture(entityArrow);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)d, (float)d2, (float)d3);
        GlStateManager.rotate(entityArrow.prevRotationYaw + (entityArrow.rotationYaw - entityArrow.prevRotationYaw) * f2 - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(entityArrow.prevRotationPitch + (entityArrow.rotationPitch - entityArrow.prevRotationPitch) * f2, 0.0f, 0.0f, 1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        int n = 0;
        float f3 = 0.0f;
        float f4 = 0.5f;
        float f5 = (float)(0 + n * 10) / 32.0f;
        float f6 = (float)(5 + n * 10) / 32.0f;
        float f7 = 0.0f;
        float f8 = 0.15625f;
        float f9 = (float)(5 + n * 10) / 32.0f;
        float f10 = (float)(10 + n * 10) / 32.0f;
        float f11 = 0.05625f;
        GlStateManager.enableRescaleNormal();
        float f12 = (float)entityArrow.arrowShake - f2;
        if (f12 > 0.0f) {
            float f13 = -MathHelper.sin(f12 * 3.0f) * f12;
            GlStateManager.rotate(f13, 0.0f, 0.0f, 1.0f);
        }
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(f11, f11, f11);
        GlStateManager.translate(-4.0f, 0.0f, 0.0f);
        GL11.glNormal3f((float)f11, (float)0.0f, (float)0.0f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(-7.0, -2.0, -2.0).tex(f7, f9).endVertex();
        worldRenderer.pos(-7.0, -2.0, 2.0).tex(f8, f9).endVertex();
        worldRenderer.pos(-7.0, 2.0, 2.0).tex(f8, f10).endVertex();
        worldRenderer.pos(-7.0, 2.0, -2.0).tex(f7, f10).endVertex();
        tessellator.draw();
        GL11.glNormal3f((float)(-f11), (float)0.0f, (float)0.0f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(-7.0, 2.0, -2.0).tex(f7, f9).endVertex();
        worldRenderer.pos(-7.0, 2.0, 2.0).tex(f8, f9).endVertex();
        worldRenderer.pos(-7.0, -2.0, 2.0).tex(f8, f10).endVertex();
        worldRenderer.pos(-7.0, -2.0, -2.0).tex(f7, f10).endVertex();
        tessellator.draw();
        int n2 = 0;
        while (n2 < 4) {
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glNormal3f((float)0.0f, (float)0.0f, (float)f11);
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-8.0, -2.0, 0.0).tex(f3, f5).endVertex();
            worldRenderer.pos(8.0, -2.0, 0.0).tex(f4, f5).endVertex();
            worldRenderer.pos(8.0, 2.0, 0.0).tex(f4, f6).endVertex();
            worldRenderer.pos(-8.0, 2.0, 0.0).tex(f3, f6).endVertex();
            tessellator.draw();
            ++n2;
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entityArrow, d, d2, d3, f, f2);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityArrow entityArrow) {
        return arrowTextures;
    }

    public RenderArrow(RenderManager renderManager) {
        super(renderManager);
    }
}

