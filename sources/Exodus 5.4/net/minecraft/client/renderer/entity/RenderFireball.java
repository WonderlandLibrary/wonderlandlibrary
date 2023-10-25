/*
 * Decompiled with CFR 0.152.
 */
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
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;

public class RenderFireball
extends Render<EntityFireball> {
    private float scale;

    @Override
    public void doRender(EntityFireball entityFireball, double d, double d2, double d3, float f, float f2) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(entityFireball);
        GlStateManager.translate((float)d, (float)d2, (float)d3);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(this.scale, this.scale, this.scale);
        TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(Items.fire_charge);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        float f3 = textureAtlasSprite.getMinU();
        float f4 = textureAtlasSprite.getMaxU();
        float f5 = textureAtlasSprite.getMinV();
        float f6 = textureAtlasSprite.getMaxV();
        float f7 = 1.0f;
        float f8 = 0.5f;
        float f9 = 0.25f;
        GlStateManager.rotate(180.0f - RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        worldRenderer.pos(-0.5, -0.25, 0.0).tex(f3, f6).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldRenderer.pos(0.5, -0.25, 0.0).tex(f4, f6).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldRenderer.pos(0.5, 0.75, 0.0).tex(f4, f5).normal(0.0f, 1.0f, 0.0f).endVertex();
        worldRenderer.pos(-0.5, 0.75, 0.0).tex(f3, f5).normal(0.0f, 1.0f, 0.0f).endVertex();
        tessellator.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entityFireball, d, d2, d3, f, f2);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFireball entityFireball) {
        return TextureMap.locationBlocksTexture;
    }

    public RenderFireball(RenderManager renderManager, float f) {
        super(renderManager);
        this.scale = f;
    }
}

