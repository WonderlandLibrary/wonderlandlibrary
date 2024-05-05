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

    public LayerHeldBlock(RenderEnderman endermanRendererIn) {
        this.endermanRenderer = endermanRendererIn;
    }

    public void doRenderLayer(EntityEnderman entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        IBlockState iblockstate = entitylivingbaseIn.getHeldBlockState();
        if (iblockstate.getBlock().getMaterial() != Material.air) {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)0.0f, (float)0.6875f, (float)-0.75f);
            GlStateManager.rotate((float)20.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.translate((float)0.25f, (float)0.1875f, (float)0.25f);
            float f = 0.5f;
            GlStateManager.scale((float)(-f), (float)(-f), (float)f);
            int i = entitylivingbaseIn.getBrightnessForRender(partialTicks);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)((float)j / 1.0f), (float)((float)k / 1.0f));
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.endermanRenderer.bindTexture(TextureMap.locationBlocksTexture);
            blockrendererdispatcher.renderBlockBrightness(iblockstate, 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
