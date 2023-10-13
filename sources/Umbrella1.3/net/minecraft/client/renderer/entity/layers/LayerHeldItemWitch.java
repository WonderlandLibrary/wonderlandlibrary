/*
 * Decompiled with CFR 0.150.
 */
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
implements LayerRenderer {
    private final RenderWitch field_177144_a;
    private static final String __OBFID = "CL_00002407";

    public LayerHeldItemWitch(RenderWitch p_i46106_1_) {
        this.field_177144_a = p_i46106_1_;
    }

    public void func_177143_a(EntityWitch p_177143_1_, float p_177143_2_, float p_177143_3_, float p_177143_4_, float p_177143_5_, float p_177143_6_, float p_177143_7_, float p_177143_8_) {
        ItemStack var9 = p_177143_1_.getHeldItem();
        if (var9 != null) {
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            GlStateManager.pushMatrix();
            if (this.field_177144_a.getMainModel().isChild) {
                GlStateManager.translate(0.0f, 0.625f, 0.0f);
                GlStateManager.rotate(-20.0f, -1.0f, 0.0f, 0.0f);
                float var10 = 0.5f;
                GlStateManager.scale(var10, var10, var10);
            }
            ((ModelWitch)this.field_177144_a.getMainModel()).villagerNose.postRender(0.0625f);
            GlStateManager.translate(-0.0625f, 0.53125f, 0.21875f);
            Item var13 = var9.getItem();
            Minecraft var11 = Minecraft.getMinecraft();
            if (var13 instanceof ItemBlock && var11.getBlockRendererDispatcher().func_175021_a(Block.getBlockFromItem(var13), var9.getMetadata())) {
                GlStateManager.translate(0.0f, 0.1875f, -0.3125f);
                GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                float var12 = 0.375f;
                GlStateManager.scale(var12, -var12, var12);
            } else if (var13 == Items.bow) {
                GlStateManager.translate(0.0f, 0.125f, 0.3125f);
                GlStateManager.rotate(-20.0f, 0.0f, 1.0f, 0.0f);
                float var12 = 0.625f;
                GlStateManager.scale(var12, -var12, var12);
                GlStateManager.rotate(-100.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
            } else if (var13.isFull3D()) {
                if (var13.shouldRotateAroundWhenRendering()) {
                    GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.translate(0.0f, -0.125f, 0.0f);
                }
                this.field_177144_a.func_82422_c();
                float var12 = 0.625f;
                GlStateManager.scale(var12, -var12, var12);
                GlStateManager.rotate(-100.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
            } else {
                GlStateManager.translate(0.25f, 0.1875f, -0.1875f);
                float var12 = 0.375f;
                GlStateManager.scale(var12, var12, var12);
                GlStateManager.rotate(60.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(20.0f, 0.0f, 0.0f, 1.0f);
            }
            GlStateManager.rotate(-15.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(40.0f, 0.0f, 0.0f, 1.0f);
            var11.getItemRenderer().renderItem(p_177143_1_, var9, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    @Override
    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
        this.func_177143_a((EntityWitch)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}

