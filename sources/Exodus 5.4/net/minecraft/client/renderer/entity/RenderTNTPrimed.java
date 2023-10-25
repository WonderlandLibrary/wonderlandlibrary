/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderTNTPrimed
extends Render<EntityTNTPrimed> {
    @Override
    protected ResourceLocation getEntityTexture(EntityTNTPrimed entityTNTPrimed) {
        return TextureMap.locationBlocksTexture;
    }

    @Override
    public void doRender(EntityTNTPrimed entityTNTPrimed, double d, double d2, double d3, float f, float f2) {
        float f3;
        BlockRendererDispatcher blockRendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)d, (float)d2 + 0.5f, (float)d3);
        if ((float)entityTNTPrimed.fuse - f2 + 1.0f < 10.0f) {
            f3 = 1.0f - ((float)entityTNTPrimed.fuse - f2 + 1.0f) / 10.0f;
            f3 = MathHelper.clamp_float(f3, 0.0f, 1.0f);
            f3 *= f3;
            f3 *= f3;
            float f4 = 1.0f + f3 * 0.3f;
            GlStateManager.scale(f4, f4, f4);
        }
        f3 = (1.0f - ((float)entityTNTPrimed.fuse - f2 + 1.0f) / 100.0f) * 0.8f;
        this.bindEntityTexture(entityTNTPrimed);
        GlStateManager.translate(-0.5f, -0.5f, 0.5f);
        blockRendererDispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), entityTNTPrimed.getBrightness(f2));
        GlStateManager.translate(0.0f, 0.0f, 1.0f);
        if (entityTNTPrimed.fuse / 5 % 2 == 0) {
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0f, 1.0f, 1.0f, f3);
            GlStateManager.doPolygonOffset(-3.0f, -3.0f);
            GlStateManager.enablePolygonOffset();
            blockRendererDispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0f);
            GlStateManager.doPolygonOffset(0.0f, 0.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
        GlStateManager.popMatrix();
        super.doRender(entityTNTPrimed, d, d2, d3, f, f2);
    }

    public RenderTNTPrimed(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.5f;
    }
}

