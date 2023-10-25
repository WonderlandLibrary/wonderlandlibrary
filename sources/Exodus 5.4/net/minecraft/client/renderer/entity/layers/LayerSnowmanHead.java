/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderSnowMan;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class LayerSnowmanHead
implements LayerRenderer<EntitySnowman> {
    private final RenderSnowMan snowManRenderer;

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }

    public LayerSnowmanHead(RenderSnowMan renderSnowMan) {
        this.snowManRenderer = renderSnowMan;
    }

    @Override
    public void doRenderLayer(EntitySnowman entitySnowman, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (!entitySnowman.isInvisible()) {
            GlStateManager.pushMatrix();
            ((ModelSnowMan)this.snowManRenderer.getMainModel()).head.postRender(0.0625f);
            float f8 = 0.625f;
            GlStateManager.translate(0.0f, -0.34375f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.scale(f8, -f8, -f8);
            Minecraft.getMinecraft().getItemRenderer().renderItem(entitySnowman, new ItemStack(Blocks.pumpkin, 1), ItemCameraTransforms.TransformType.HEAD);
            GlStateManager.popMatrix();
        }
    }
}

