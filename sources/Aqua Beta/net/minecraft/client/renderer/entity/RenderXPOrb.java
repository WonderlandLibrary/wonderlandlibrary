package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.src.Config;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;

public class RenderXPOrb
extends Render<EntityXPOrb> {
    private static final ResourceLocation experienceOrbTextures = new ResourceLocation("textures/entity/experience_orb.png");

    public RenderXPOrb(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }

    public void doRender(EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks) {
        int j2;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)((float)x), (float)((float)y), (float)((float)z));
        this.bindEntityTexture((Entity)entity);
        int i = entity.getTextureByXP();
        float f = (float)(i % 4 * 16 + 0) / 64.0f;
        float f1 = (float)(i % 4 * 16 + 16) / 64.0f;
        float f2 = (float)(i / 4 * 16 + 0) / 64.0f;
        float f3 = (float)(i / 4 * 16 + 16) / 64.0f;
        float f4 = 1.0f;
        float f5 = 0.5f;
        float f6 = 0.25f;
        int j = entity.getBrightnessForRender(partialTicks);
        int k = j % 65536;
        int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)((float)k / 1.0f), (float)((float)l / 1.0f));
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        float f7 = 255.0f;
        float f8 = ((float)entity.xpColor + partialTicks) / 2.0f;
        if (Config.isCustomColors()) {
            f8 = CustomColors.getXpOrbTimer((float)f8);
        }
        l = (int)((MathHelper.sin((float)(f8 + 0.0f)) + 1.0f) * 0.5f * 255.0f);
        int i1 = 255;
        int j1 = (int)((MathHelper.sin((float)(f8 + 4.1887903f)) + 1.0f) * 0.1f * 255.0f);
        GlStateManager.rotate((float)(180.0f - this.renderManager.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-this.renderManager.playerViewX), (float)1.0f, (float)0.0f, (float)0.0f);
        float f9 = 0.3f;
        GlStateManager.scale((float)0.3f, (float)0.3f, (float)0.3f);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        int k1 = l;
        int l1 = 255;
        int i2 = j1;
        if (Config.isCustomColors() && (j2 = CustomColors.getXpOrbColor((float)f8)) >= 0) {
            k1 = j2 >> 16 & 0xFF;
            l1 = j2 >> 8 & 0xFF;
            i2 = j2 >> 0 & 0xFF;
        }
        worldrenderer.pos((double)(0.0f - f5), (double)(0.0f - f6), 0.0).tex((double)f, (double)f3).color(k1, l1, i2, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos((double)(f4 - f5), (double)(0.0f - f6), 0.0).tex((double)f1, (double)f3).color(k1, l1, i2, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos((double)(f4 - f5), (double)(1.0f - f6), 0.0).tex((double)f1, (double)f2).color(k1, l1, i2, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos((double)(0.0f - f5), (double)(1.0f - f6), 0.0).tex((double)f, (double)f2).color(k1, l1, i2, 128).normal(0.0f, 1.0f, 0.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender((Entity)entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityXPOrb entity) {
        return experienceOrbTextures;
    }
}
