package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderTNTPrimed
extends Render<EntityTNTPrimed> {
    public RenderTNTPrimed(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5f;
    }

    public void doRender(EntityTNTPrimed entity, double x, double y, double z, float entityYaw, float partialTicks) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)((float)x), (float)((float)y + 0.5f), (float)((float)z));
        if ((float)entity.fuse - partialTicks + 1.0f < 10.0f) {
            float f = 1.0f - ((float)entity.fuse - partialTicks + 1.0f) / 10.0f;
            f = MathHelper.clamp_float((float)f, (float)0.0f, (float)1.0f);
            f *= f;
            f *= f;
            float f1 = 1.0f + f * 0.3f;
            GlStateManager.scale((float)f1, (float)f1, (float)f1);
        }
        float f2 = (1.0f - ((float)entity.fuse - partialTicks + 1.0f) / 100.0f) * 0.8f;
        this.bindEntityTexture((Entity)entity);
        GlStateManager.translate((float)-0.5f, (float)-0.5f, (float)0.5f);
        blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), entity.getBrightness(partialTicks));
        GlStateManager.translate((float)0.0f, (float)0.0f, (float)1.0f);
        if (entity.fuse / 5 % 2 == 0) {
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc((int)770, (int)772);
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)f2);
            GlStateManager.doPolygonOffset((float)-3.0f, (float)-3.0f);
            GlStateManager.enablePolygonOffset();
            blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0f);
            GlStateManager.doPolygonOffset((float)0.0f, (float)0.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
        GlStateManager.popMatrix();
        super.doRender((Entity)entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(EntityTNTPrimed entity) {
        return TextureMap.locationBlocksTexture;
    }
}
