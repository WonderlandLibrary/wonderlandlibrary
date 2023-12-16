// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.client.model.ModelBiped;
import moonsense.features.modules.type.mechanic.OldAnimationsModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;

public class LayerHeldItem implements LayerRenderer
{
    private final RendererLivingEntity field_177206_a;
    private static final String __OBFID = "CL_00002416";
    
    public LayerHeldItem(final RendererLivingEntity p_i46115_1_) {
        this.field_177206_a = p_i46115_1_;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase p_177141_1_, final float p_177141_2_, final float p_177141_3_, final float p_177141_4_, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float p_177141_8_) {
        ItemStack var9 = p_177141_1_.getHeldItem();
        if (var9 != null) {
            GlStateManager.pushMatrix();
            if (this.field_177206_a.getMainModel().isChild) {
                final float var10 = 0.5f;
                GlStateManager.translate(0.0f, 0.625f, 0.0f);
                GlStateManager.rotate(-20.0f, -1.0f, 0.0f, 0.0f);
                GlStateManager.scale(var10, var10, var10);
            }
            if (p_177141_1_ instanceof EntityPlayer) {
                if (OldAnimationsModule.INSTANCE.block.getBoolean()) {
                    if (((EntityPlayer)p_177141_1_).isBlocking()) {
                        if (p_177141_1_.isSneaking()) {
                            ((ModelBiped)this.field_177206_a.getMainModel()).postRenderHiddenArm(0.0325f);
                            GlStateManager.scale(1.05f, 1.05f, 1.05f);
                            GlStateManager.translate(-0.58f, 0.32f, -0.07f);
                            GlStateManager.rotate(-24405.0f, 137290.0f, -2009900.0f, -2654900.0f);
                        }
                        else {
                            ((ModelBiped)this.field_177206_a.getMainModel()).postRenderHiddenArm(0.0325f);
                            GlStateManager.scale(1.05f, 1.05f, 1.05f);
                            GlStateManager.translate(-0.45f, 0.25f, -0.07f);
                            GlStateManager.rotate(-24405.0f, 137290.0f, -2009900.0f, -2654900.0f);
                        }
                    }
                    else {
                        ((ModelBiped)this.field_177206_a.getMainModel()).postRenderHiddenArm(0.0625f);
                    }
                }
                else {
                    ((ModelBiped)this.field_177206_a.getMainModel()).postRenderHiddenArm(0.0625f);
                }
                GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
                if (((EntityPlayer)p_177141_1_).fishEntity != null) {
                    var9 = new ItemStack(Items.fishing_rod, 0);
                }
            }
            else {
                ((ModelBiped)this.field_177206_a.getMainModel()).postRenderHiddenArm(0.0625f);
                GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
            }
            final Item item = var9.getItem();
            final Minecraft minecraft = Minecraft.getMinecraft();
            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2) {
                GlStateManager.translate(0.0f, 0.1875f, -0.3125f);
                GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                final float f1 = 0.375f;
                GlStateManager.scale(-0.375f, -0.375f, 0.375f);
            }
            if (p_177141_1_.isSneaking()) {
                GlStateManager.translate(0.0f, 0.203125f, 0.0f);
            }
            minecraft.getItemRenderer().renderItem(p_177141_1_, var9, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
