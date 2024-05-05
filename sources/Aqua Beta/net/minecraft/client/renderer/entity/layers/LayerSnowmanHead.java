package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderSnowMan;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class LayerSnowmanHead
implements LayerRenderer<EntitySnowman> {
    private final RenderSnowMan snowManRenderer;

    public LayerSnowmanHead(RenderSnowMan snowManRendererIn) {
        this.snowManRenderer = snowManRendererIn;
    }

    public void doRenderLayer(EntitySnowman entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        if (!entitylivingbaseIn.isInvisible()) {
            GlStateManager.pushMatrix();
            this.snowManRenderer.getMainModel().head.postRender(0.0625f);
            float f = 0.625f;
            GlStateManager.translate((float)0.0f, (float)-0.34375f, (float)0.0f);
            GlStateManager.rotate((float)180.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.scale((float)f, (float)(-f), (float)(-f));
            Minecraft.getMinecraft().getItemRenderer().renderItem((EntityLivingBase)entitylivingbaseIn, new ItemStack(Blocks.pumpkin, 1), ItemCameraTransforms.TransformType.HEAD);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
