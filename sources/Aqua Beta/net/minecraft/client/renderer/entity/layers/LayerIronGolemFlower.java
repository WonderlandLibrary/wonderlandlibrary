package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.Blocks;

public class LayerIronGolemFlower
implements LayerRenderer<EntityIronGolem> {
    private final RenderIronGolem ironGolemRenderer;

    public LayerIronGolemFlower(RenderIronGolem ironGolemRendererIn) {
        this.ironGolemRenderer = ironGolemRendererIn;
    }

    public void doRenderLayer(EntityIronGolem entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        if (entitylivingbaseIn.getHoldRoseTick() != 0) {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            GlStateManager.rotate((float)(5.0f + 180.0f * ((ModelIronGolem)this.ironGolemRenderer.getMainModel()).ironGolemRightArm.rotateAngleX / (float)Math.PI), (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.translate((float)-0.9375f, (float)-0.625f, (float)-0.9375f);
            float f = 0.5f;
            GlStateManager.scale((float)f, (float)(-f), (float)f);
            int i = entitylivingbaseIn.getBrightnessForRender(partialTicks);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)((float)j / 1.0f), (float)((float)k / 1.0f));
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.ironGolemRenderer.bindTexture(TextureMap.locationBlocksTexture);
            blockrendererdispatcher.renderBlockBrightness(Blocks.red_flower.getDefaultState(), 1.0f);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
