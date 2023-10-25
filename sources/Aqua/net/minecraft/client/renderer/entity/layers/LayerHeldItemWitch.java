package net.minecraft.client.renderer.entity.layers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LayerHeldItemWitch
implements LayerRenderer<EntityWitch> {
    private final RenderWitch witchRenderer;

    public LayerHeldItemWitch(RenderWitch witchRendererIn) {
        this.witchRenderer = witchRendererIn;
    }

    public void doRenderLayer(EntityWitch entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        ItemStack itemstack = entitylivingbaseIn.getHeldItem();
        if (itemstack != null) {
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f);
            GlStateManager.pushMatrix();
            if (this.witchRenderer.getMainModel().isChild) {
                GlStateManager.translate((float)0.0f, (float)0.625f, (float)0.0f);
                GlStateManager.rotate((float)-20.0f, (float)-1.0f, (float)0.0f, (float)0.0f);
                float f = 0.5f;
                GlStateManager.scale((float)f, (float)f, (float)f);
            }
            ((ModelWitch)this.witchRenderer.getMainModel()).villagerNose.postRender(0.0625f);
            GlStateManager.translate((float)-0.0625f, (float)0.53125f, (float)0.21875f);
            Item item = itemstack.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();
            if (item instanceof ItemBlock && minecraft.getBlockRendererDispatcher().isRenderTypeChest(Block.getBlockFromItem((Item)item), itemstack.getMetadata())) {
                GlStateManager.translate((float)0.0f, (float)0.0625f, (float)-0.25f);
                GlStateManager.rotate((float)30.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)-5.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                float f4 = 0.375f;
                GlStateManager.scale((float)f4, (float)(-f4), (float)f4);
            } else if (item == Items.bow) {
                GlStateManager.translate((float)0.0f, (float)0.125f, (float)-0.125f);
                GlStateManager.rotate((float)-45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                float f1 = 0.625f;
                GlStateManager.scale((float)f1, (float)(-f1), (float)f1);
                GlStateManager.rotate((float)-100.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)-20.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            } else if (item.isFull3D()) {
                if (item.shouldRotateAroundWhenRendering()) {
                    GlStateManager.rotate((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                    GlStateManager.translate((float)0.0f, (float)-0.0625f, (float)0.0f);
                }
                this.witchRenderer.transformHeldFull3DItemLayer();
                GlStateManager.translate((float)0.0625f, (float)-0.125f, (float)0.0f);
                float f2 = 0.625f;
                GlStateManager.scale((float)f2, (float)(-f2), (float)f2);
                GlStateManager.rotate((float)0.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)0.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            } else {
                GlStateManager.translate((float)0.1875f, (float)0.1875f, (float)0.0f);
                float f3 = 0.875f;
                GlStateManager.scale((float)f3, (float)f3, (float)f3);
                GlStateManager.rotate((float)-20.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GlStateManager.rotate((float)-60.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)-30.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
            GlStateManager.rotate((float)-15.0f, (float)1.0f, (float)0.0f, (float)0.0f);
            GlStateManager.rotate((float)40.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            minecraft.getItemRenderer().renderItem((EntityLivingBase)entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
