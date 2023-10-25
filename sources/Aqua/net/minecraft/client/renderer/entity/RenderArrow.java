package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderArrow
extends Render<EntityArrow> {
    private static final ResourceLocation arrowTextures = new ResourceLocation("textures/entity/arrow.png");

    public RenderArrow(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void doRender(EntityArrow entity, double x, double y, double z, float entityYaw, float partialTicks) {
        this.bindEntityTexture((Entity)entity);
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)((float)x), (float)((float)y), (float)((float)z));
        GlStateManager.rotate((float)(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks), (float)0.0f, (float)0.0f, (float)1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        int i = 0;
        float f = 0.0f;
        float f1 = 0.5f;
        float f2 = (float)(0 + i * 10) / 32.0f;
        float f3 = (float)(5 + i * 10) / 32.0f;
        float f4 = 0.0f;
        float f5 = 0.15625f;
        float f6 = (float)(5 + i * 10) / 32.0f;
        float f7 = (float)(10 + i * 10) / 32.0f;
        float f8 = 0.05625f;
        GlStateManager.enableRescaleNormal();
        float f9 = (float)entity.arrowShake - partialTicks;
        if (f9 > 0.0f) {
            float f10 = -MathHelper.sin((float)(f9 * 3.0f)) * f9;
            GlStateManager.rotate((float)f10, (float)0.0f, (float)0.0f, (float)1.0f);
        }
        GlStateManager.rotate((float)45.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)f8, (float)f8, (float)f8);
        GlStateManager.translate((float)-4.0f, (float)0.0f, (float)0.0f);
        GL11.glNormal3f((float)f8, (float)0.0f, (float)0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0, -2.0, -2.0).tex((double)f4, (double)f6).endVertex();
        worldrenderer.pos(-7.0, -2.0, 2.0).tex((double)f5, (double)f6).endVertex();
        worldrenderer.pos(-7.0, 2.0, 2.0).tex((double)f5, (double)f7).endVertex();
        worldrenderer.pos(-7.0, 2.0, -2.0).tex((double)f4, (double)f7).endVertex();
        tessellator.draw();
        GL11.glNormal3f((float)(-f8), (float)0.0f, (float)0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(-7.0, 2.0, -2.0).tex((double)f4, (double)f6).endVertex();
        worldrenderer.pos(-7.0, 2.0, 2.0).tex((double)f5, (double)f6).endVertex();
        worldrenderer.pos(-7.0, -2.0, 2.0).tex((double)f5, (double)f7).endVertex();
        worldrenderer.pos(-7.0, -2.0, -2.0).tex((double)f4, (double)f7).endVertex();
        tessellator.draw();
        for (int j = 0; j < 4; ++j) {
            GlStateManager.rotate((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GL11.glNormal3f((float)0.0f, (float)0.0f, (float)f8);
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(-8.0, -2.0, 0.0).tex((double)f, (double)f2).endVertex();
            worldrenderer.pos(8.0, -2.0, 0.0).tex((double)f1, (double)f2).endVertex();
            worldrenderer.pos(8.0, 2.0, 0.0).tex((double)f1, (double)f3).endVertex();
            worldrenderer.pos(-8.0, 2.0, 0.0).tex((double)f, (double)f3).endVertex();
            tessellator.draw();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender((Entity)entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityArrow entity) {
        return arrowTextures;
    }
}
