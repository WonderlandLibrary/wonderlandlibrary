package net.minecraft.client.renderer.entity.layers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LayerHeldItem
implements LayerRenderer<EntityLivingBase> {
    private final RendererLivingEntity<?> livingEntityRenderer;

    public LayerHeldItem(RendererLivingEntity<?> livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        ItemStack itemstack = entitylivingbaseIn.getHeldItem();
        if (itemstack != null) {
            GlStateManager.pushMatrix();
            if (this.livingEntityRenderer.getMainModel().isChild) {
                float f = 0.5f;
                GlStateManager.translate((float)0.0f, (float)0.625f, (float)0.0f);
                GlStateManager.rotate((float)-20.0f, (float)-1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.scale((float)f, (float)f, (float)f);
            }
            ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625f);
            GlStateManager.translate((float)-0.0625f, (float)0.4375f, (float)0.0625f);
            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer)entitylivingbaseIn).fishEntity != null) {
                itemstack = new ItemStack((Item)Items.fishing_rod, 0);
            }
            Item item = itemstack.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();
            if (item instanceof ItemBlock && Block.getBlockFromItem((Item)item).getRenderType() == 2) {
                GlStateManager.translate((float)0.0f, (float)0.1875f, (float)-0.3125f);
                GlStateManager.rotate((float)20.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GlStateManager.rotate((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                float f1 = 0.375f;
                GlStateManager.scale((float)(-f1), (float)(-f1), (float)f1);
            }
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate((float)0.0f, (float)0.203125f, (float)0.0f);
            }
            minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
