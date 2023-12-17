// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import java.util.Random;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.item.EntityItem;

public class RenderEntityItem extends Render<EntityItem>
{
    private final RenderItem itemRenderer;
    private final Random random;
    
    public RenderEntityItem(final RenderManager renderManagerIn, final RenderItem p_i46167_2_) {
        super(renderManagerIn);
        this.random = new Random();
        this.itemRenderer = p_i46167_2_;
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }
    
    private int transformModelCount(final EntityItem itemIn, final double p_177077_2_, final double p_177077_4_, final double p_177077_6_, final float p_177077_8_, final IBakedModel p_177077_9_) {
        final ItemStack itemstack = itemIn.getItem();
        final Item item = itemstack.getItem();
        if (item == null) {
            return 0;
        }
        final boolean flag = p_177077_9_.isGui3d();
        final int i = this.getModelCount(itemstack);
        final float f = 0.25f;
        final float f2 = MathHelper.sin((itemIn.getAge() + p_177077_8_) / 10.0f + itemIn.hoverStart) * 0.1f + 0.1f;
        final float f3 = p_177077_9_.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
        GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + f2 + 0.25f * f3, (float)p_177077_6_);
        if (flag || this.renderManager.options != null) {
            final float f4 = ((itemIn.getAge() + p_177077_8_) / 20.0f + itemIn.hoverStart) * 57.295776f;
            GlStateManager.rotate(f4, 0.0f, 1.0f, 0.0f);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return i;
    }
    
    private int getModelCount(final ItemStack stack) {
        int i = 1;
        if (stack.getCount() > 48) {
            i = 5;
        }
        else if (stack.getCount() > 32) {
            i = 4;
        }
        else if (stack.getCount() > 16) {
            i = 3;
        }
        else if (stack.getCount() > 1) {
            i = 2;
        }
        return i;
    }
    
    @Override
    public void doRender(final EntityItem entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        final ItemStack itemstack = entity.getItem();
        final int i = itemstack.isEmpty() ? 187 : (Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata());
        this.random.setSeed(i);
        boolean flag = false;
        if (this.bindEntityTexture(entity)) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).setBlurMipmap(false, false);
            flag = true;
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        final IBakedModel ibakedmodel = this.itemRenderer.getItemModelWithOverrides(itemstack, entity.world, null);
        final int j = this.transformModelCount(entity, x, y, z, partialTicks, ibakedmodel);
        final float f = ibakedmodel.getItemCameraTransforms().ground.scale.x;
        final float f2 = ibakedmodel.getItemCameraTransforms().ground.scale.y;
        final float f3 = ibakedmodel.getItemCameraTransforms().ground.scale.z;
        final boolean flag2 = ibakedmodel.isGui3d();
        if (!flag2) {
            final float f4 = -0.0f * (j - 1) * 0.5f * f;
            final float f5 = -0.0f * (j - 1) * 0.5f * f2;
            final float f6 = -0.09375f * (j - 1) * 0.5f * f3;
            GlStateManager.translate(f4, f5, f6);
        }
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        for (int k = 0; k < j; ++k) {
            if (flag2) {
                GlStateManager.pushMatrix();
                if (k > 0) {
                    final float f7 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f8 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float f9 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.translate(f7, f8, f9);
                }
                ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
            }
            else {
                GlStateManager.pushMatrix();
                if (k > 0) {
                    final float f10 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    final float f11 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    GlStateManager.translate(f10, f11, 0.0f);
                }
                ibakedmodel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
                GlStateManager.translate(0.0f * f, 0.0f * f2, 0.09375f * f3);
            }
        }
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entity);
        if (flag) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entity)).restoreLastBlurMipmap();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityItem entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
