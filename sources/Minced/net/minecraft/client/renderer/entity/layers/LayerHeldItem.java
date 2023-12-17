// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;

public class LayerHeldItem implements LayerRenderer<EntityLivingBase>
{
    protected final RenderLivingBase<?> livingEntityRenderer;
    
    public LayerHeldItem(final RenderLivingBase<?> livingEntityRendererIn) {
        this.livingEntityRenderer = livingEntityRendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
        final ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
        final ItemStack itemstack2 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();
        if (!itemstack.isEmpty() || !itemstack2.isEmpty()) {
            GlStateManager.pushMatrix();
            if (this.livingEntityRenderer.getMainModel().isChild) {
                final float f = 0.5f;
                GlStateManager.translate(0.0f, 0.75f, 0.0f);
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
            }
            this.renderHeldItem(entitylivingbaseIn, itemstack2, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
            GlStateManager.popMatrix();
        }
    }
    
    private void renderHeldItem(final EntityLivingBase p_188358_1_, final ItemStack p_188358_2_, final ItemCameraTransforms.TransformType p_188358_3_, final EnumHandSide handSide) {
        if (!p_188358_2_.isEmpty()) {
            GlStateManager.pushMatrix();
            this.translateToHand(handSide);
            if (p_188358_1_.isSneaking()) {
                GlStateManager.translate(0.0f, 0.2f, 0.0f);
            }
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
            final boolean flag = handSide == EnumHandSide.LEFT;
            GlStateManager.translate((flag ? -1 : 1) / 16.0f, 0.125f, -0.625f);
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, flag);
            GlStateManager.popMatrix();
        }
    }
    
    protected void translateToHand(final EnumHandSide p_191361_1_) {
        ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625f, p_191361_1_);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
