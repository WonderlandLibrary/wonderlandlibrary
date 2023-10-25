package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;

public class RenderFireball
extends Render<EntityFireball> {
    private float scale;

    public RenderFireball(RenderManager renderManagerIn, float scaleIn) {
        super(renderManagerIn);
        this.scale = scaleIn;
    }

    public void doRender(EntityFireball entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture((Entity)entity);
        GlStateManager.translate((float)((float)x), (float)((float)y), (float)((float)z));
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale((float)this.scale, (float)this.scale, (float)this.scale);
        TextureAtlasSprite textureatlassprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Items.fire_charge);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMaxU();
        float f2 = textureatlassprite.getMinV();
        float f3 = textureatlassprite.getMaxV();
        float f4 = 1.0f;
        float f5 = 0.5f;
        float f6 = 0.25f;
        GlStateManager.rotate((float)(180.0f - this.renderManager.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-this.renderManager.playerViewX), (float)1.0f, (float)0.0f, (float)0.0f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        worldrenderer.pos(-0.5, -0.25, 0.0).tex((double)f, (double)f3).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(0.5, -0.25, 0.0).tex((double)f1, (double)f3).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(0.5, 0.75, 0.0).tex((double)f1, (double)f2).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldrenderer.pos(-0.5, 0.75, 0.0).tex((double)f, (double)f2).normal(0.0f, 1.0f, 0.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender((Entity)entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityFireball entity) {
        return TextureMap.locationBlocksTexture;
    }
}
