/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderEntityItem
extends Render<EntityItem> {
    private final RenderItem itemRenderer;
    private Random field_177079_e = new Random();

    @Override
    protected ResourceLocation getEntityTexture(EntityItem entityItem) {
        return TextureMap.locationBlocksTexture;
    }

    private int func_177078_a(ItemStack itemStack) {
        int n = 1;
        if (itemStack.stackSize > 48) {
            n = 5;
        } else if (itemStack.stackSize > 32) {
            n = 4;
        } else if (itemStack.stackSize > 16) {
            n = 3;
        } else if (itemStack.stackSize > 1) {
            n = 2;
        }
        return n;
    }

    private int func_177077_a(EntityItem entityItem, double d, double d2, double d3, float f, IBakedModel iBakedModel) {
        float f2;
        ItemStack itemStack = entityItem.getEntityItem();
        Item item = itemStack.getItem();
        if (item == null) {
            return 0;
        }
        boolean bl = iBakedModel.isGui3d();
        int n = this.func_177078_a(itemStack);
        float f3 = 0.25f;
        float f4 = MathHelper.sin(((float)entityItem.getAge() + f) / 10.0f + entityItem.hoverStart) * 0.1f + 0.1f;
        float f5 = iBakedModel.getItemCameraTransforms().getTransform((ItemCameraTransforms.TransformType)ItemCameraTransforms.TransformType.GROUND).scale.y;
        GlStateManager.translate((float)d, (float)d2 + f4 + 0.25f * f5, (float)d3);
        if (bl || this.renderManager.options != null) {
            f2 = (((float)entityItem.getAge() + f) / 20.0f + entityItem.hoverStart) * 57.295776f;
            GlStateManager.rotate(f2, 0.0f, 1.0f, 0.0f);
        }
        if (!bl) {
            f2 = -0.0f * (float)(n - 1) * 0.5f;
            float f6 = -0.0f * (float)(n - 1) * 0.5f;
            float f7 = -0.046875f * (float)(n - 1) * 0.5f;
            GlStateManager.translate(f2, f6, f7);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return n;
    }

    public RenderEntityItem(RenderManager renderManager, RenderItem renderItem) {
        super(renderManager);
        this.itemRenderer = renderItem;
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }

    @Override
    public void doRender(EntityItem entityItem, double d, double d2, double d3, float f, float f2) {
        ItemStack itemStack = entityItem.getEntityItem();
        this.field_177079_e.setSeed(187L);
        boolean bl = false;
        if (this.bindEntityTexture(entityItem)) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entityItem)).setBlurMipmap(false, false);
            bl = true;
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        IBakedModel iBakedModel = this.itemRenderer.getItemModelMesher().getItemModel(itemStack);
        int n = this.func_177077_a(entityItem, d, d2, d3, f2, iBakedModel);
        int n2 = 0;
        while (n2 < n) {
            float f3;
            float f4;
            float f5;
            if (iBakedModel.isGui3d()) {
                GlStateManager.pushMatrix();
                if (n2 > 0) {
                    f5 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    f4 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    f3 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.translate(f5, f4, f3);
                }
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
                iBakedModel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemStack, iBakedModel);
                GlStateManager.popMatrix();
            } else {
                GlStateManager.pushMatrix();
                iBakedModel.getItemCameraTransforms().applyTransform(ItemCameraTransforms.TransformType.GROUND);
                this.itemRenderer.renderItem(itemStack, iBakedModel);
                GlStateManager.popMatrix();
                f5 = iBakedModel.getItemCameraTransforms().ground.scale.x;
                f4 = iBakedModel.getItemCameraTransforms().ground.scale.y;
                f3 = iBakedModel.getItemCameraTransforms().ground.scale.z;
                GlStateManager.translate(0.0f * f5, 0.0f * f4, 0.046875f * f3);
            }
            ++n2;
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(entityItem);
        if (bl) {
            this.renderManager.renderEngine.getTexture(this.getEntityTexture(entityItem)).restoreLastBlurMipmap();
        }
        super.doRender(entityItem, d, d2, d3, f, f2);
    }
}

