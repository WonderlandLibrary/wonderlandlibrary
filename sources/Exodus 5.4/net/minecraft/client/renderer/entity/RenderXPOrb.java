/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderXPOrb
extends Render<EntityXPOrb> {
    private static final ResourceLocation experienceOrbTextures = new ResourceLocation("textures/entity/experience_orb.png");

    @Override
    public void doRender(EntityXPOrb entityXPOrb, double d, double d2, double d3, float f, float f2) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)d, (float)d2, (float)d3);
        this.bindEntityTexture(entityXPOrb);
        int n = entityXPOrb.getTextureByXP();
        float f3 = (float)(n % 4 * 16 + 0) / 64.0f;
        float f4 = (float)(n % 4 * 16 + 16) / 64.0f;
        float f5 = (float)(n / 4 * 16 + 0) / 64.0f;
        float f6 = (float)(n / 4 * 16 + 16) / 64.0f;
        float f7 = 1.0f;
        float f8 = 0.5f;
        float f9 = 0.25f;
        int n2 = entityXPOrb.getBrightnessForRender(f2);
        int n3 = n2 % 65536;
        int n4 = n2 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)n3 / 1.0f, (float)n4 / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float f10 = 255.0f;
        float f11 = ((float)entityXPOrb.xpColor + f2) / 2.0f;
        n4 = (int)((MathHelper.sin(f11 + 0.0f) + 1.0f) * 0.5f * 255.0f);
        int n5 = 255;
        int n6 = (int)((MathHelper.sin(f11 + 4.1887903f) + 1.0f) * 0.1f * 255.0f);
        GlStateManager.rotate(180.0f - RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        float f12 = 0.3f;
        GlStateManager.scale(0.3f, 0.3f, 0.3f);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        worldRenderer.pos(0.0f - f8, 0.0f - f9, 0.0).tex(f3, f6).color(n4, 255, n6, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldRenderer.pos(f7 - f8, 0.0f - f9, 0.0).tex(f4, f6).color(n4, 255, n6, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldRenderer.pos(f7 - f8, 1.0f - f9, 0.0).tex(f4, f5).color(n4, 255, n6, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldRenderer.pos(0.0f - f8, 1.0f - f9, 0.0).tex(f3, f5).color(n4, 255, n6, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entityXPOrb, d, d2, d3, f, f2);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityXPOrb entityXPOrb) {
        return experienceOrbTextures;
    }

    public RenderXPOrb(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }
}

