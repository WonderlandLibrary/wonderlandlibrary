/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.monster.EntityEnderman;

public class LayerHeldBlock
implements LayerRenderer<EntityEnderman> {
    private final RenderEnderman endermanRenderer;

    @Override
    public void doRenderLayer(EntityEnderman entityEnderman, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        IBlockState iBlockState = entityEnderman.getHeldBlockState();
        if (iBlockState.getBlock().getMaterial() != Material.air) {
            BlockRendererDispatcher blockRendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, 0.6875f, -0.75f);
            GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.25f, 0.1875f, 0.25f);
            float f8 = 0.5f;
            GlStateManager.scale(-f8, -f8, f8);
            int n = entityEnderman.getBrightnessForRender(f3);
            int n2 = n % 65536;
            int n3 = n / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)n2 / 1.0f, (float)n3 / 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.endermanRenderer.bindTexture(TextureMap.locationBlocksTexture);
            blockRendererDispatcher.renderBlockBrightness(iBlockState, 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
        }
    }

    public LayerHeldBlock(RenderEnderman renderEnderman) {
        this.endermanRenderer = renderEnderman;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}

