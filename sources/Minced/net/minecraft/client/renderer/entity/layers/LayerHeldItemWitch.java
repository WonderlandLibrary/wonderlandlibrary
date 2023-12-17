// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Items;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraft.entity.monster.EntityWitch;

public class LayerHeldItemWitch implements LayerRenderer<EntityWitch>
{
    private final RenderWitch witchRenderer;
    
    public LayerHeldItemWitch(final RenderWitch witchRendererIn) {
        this.witchRenderer = witchRendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityWitch entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final ItemStack itemstack = entitylivingbaseIn.getHeldItemMainhand();
        if (!itemstack.isEmpty()) {
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            GlStateManager.pushMatrix();
            if (this.witchRenderer.getMainModel().isChild) {
                GlStateManager.translate(0.0f, 0.625f, 0.0f);
                GlStateManager.rotate(-20.0f, -1.0f, 0.0f, 0.0f);
                final float f = 0.5f;
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
            }
            this.witchRenderer.getMainModel().villagerNose.postRender(0.0625f);
            GlStateManager.translate(-0.0625f, 0.53125f, 0.21875f);
            final Item item = itemstack.getItem();
            final Minecraft minecraft = Minecraft.getMinecraft();
            if (Block.getBlockFromItem(item).getDefaultState().getRenderType() == EnumBlockRenderType.ENTITYBLOCK_ANIMATED) {
                GlStateManager.translate(0.0f, 0.0625f, -0.25f);
                GlStateManager.rotate(30.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(-5.0f, 0.0f, 1.0f, 0.0f);
                final float f2 = 0.375f;
                GlStateManager.scale(0.375f, -0.375f, 0.375f);
            }
            else if (item == Items.BOW) {
                GlStateManager.translate(0.0f, 0.125f, -0.125f);
                GlStateManager.rotate(-45.0f, 0.0f, 1.0f, 0.0f);
                final float f3 = 0.625f;
                GlStateManager.scale(0.625f, -0.625f, 0.625f);
                GlStateManager.rotate(-100.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(-20.0f, 0.0f, 1.0f, 0.0f);
            }
            else if (item.isFull3D()) {
                if (item.shouldRotateAroundWhenRendering()) {
                    GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.translate(0.0f, -0.0625f, 0.0f);
                }
                this.witchRenderer.transformHeldFull3DItemLayer();
                GlStateManager.translate(0.0625f, -0.125f, 0.0f);
                final float f4 = 0.625f;
                GlStateManager.scale(0.625f, -0.625f, 0.625f);
                GlStateManager.rotate(0.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
            }
            else {
                GlStateManager.translate(0.1875f, 0.1875f, 0.0f);
                final float f5 = 0.875f;
                GlStateManager.scale(0.875f, 0.875f, 0.875f);
                GlStateManager.rotate(-20.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(-60.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(-30.0f, 0.0f, 0.0f, 1.0f);
            }
            GlStateManager.rotate(-15.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(40.0f, 0.0f, 0.0f, 1.0f);
            minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
