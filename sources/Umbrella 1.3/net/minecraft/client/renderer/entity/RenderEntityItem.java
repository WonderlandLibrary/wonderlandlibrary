/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderEntityItem
extends Render {
    private final RenderItem field_177080_a;
    private Random field_177079_e = new Random();
    private static final String __OBFID = "CL_00002442";

    public RenderEntityItem(RenderManager p_i46167_1_, RenderItem p_i46167_2_) {
        super(p_i46167_1_);
        this.field_177080_a = p_i46167_2_;
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }

    private int func_177077_a(EntityItem p_177077_1_, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_) {
        float var16;
        ItemStack var10 = p_177077_1_.getEntityItem();
        Item var11 = var10.getItem();
        if (var11 == null) {
            return 0;
        }
        boolean var12 = p_177077_9_.isAmbientOcclusionEnabled();
        int var13 = this.func_177078_a(var10);
        float var14 = 0.25f;
        float var15 = MathHelper.sin(((float)p_177077_1_.func_174872_o() + p_177077_8_) / 10.0f + p_177077_1_.hoverStart) * 0.1f + 0.1f;
        GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + var15 + 0.25f, (float)p_177077_6_);
        if (var12 || this.renderManager.options != null && this.renderManager.options.fancyGraphics) {
            var16 = (((float)p_177077_1_.func_174872_o() + p_177077_8_) / 20.0f + p_177077_1_.hoverStart) * 57.295776f;
            GlStateManager.rotate(var16, 0.0f, 1.0f, 0.0f);
        }
        if (!var12) {
            var16 = -0.0f * (float)(var13 - 1) * 0.5f;
            float var17 = -0.0f * (float)(var13 - 1) * 0.5f;
            float var18 = -0.046875f * (float)(var13 - 1) * 0.5f;
            GlStateManager.translate(var16, var17, var18);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return var13;
    }

    private int func_177078_a(ItemStack p_177078_1_) {
        int var2 = 1;
        if (p_177078_1_.stackSize > 48) {
            var2 = 5;
        } else if (p_177078_1_.stackSize > 32) {
            var2 = 4;
        } else if (p_177078_1_.stackSize > 16) {
            var2 = 3;
        } else if (p_177078_1_.stackSize > 1) {
            var2 = 2;
        }
        return var2;
    }

    public void func_177075_a(EntityItem p_177075_1_, double p_177075_2_, double p_177075_4_, double p_177075_6_, float p_177075_8_, float p_177075_9_) {
        ItemStack var10 = p_177075_1_.getEntityItem();
        this.field_177079_e.setSeed(187L);
        boolean var11 = false;
        if (this.bindEntityTexture(p_177075_1_)) {
            this.renderManager.renderEngine.getTexture(this.func_177076_a(p_177075_1_)).func_174936_b(false, false);
            var11 = true;
        }
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        IBakedModel var12 = this.field_177080_a.getItemModelMesher().getItemModel(var10);
        int var13 = this.func_177077_a(p_177075_1_, p_177075_2_, p_177075_4_, p_177075_6_, p_177075_9_, var12);
        for (int var14 = 0; var14 < var13; ++var14) {
            if (var12.isAmbientOcclusionEnabled()) {
                GlStateManager.pushMatrix();
                if (var14 > 0) {
                    float var15 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    float var16 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    float var17 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.translate(var15, var16, var17);
                }
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
                this.field_177080_a.func_180454_a(var10, var12);
                GlStateManager.popMatrix();
                continue;
            }
            this.field_177080_a.func_180454_a(var10, var12);
            GlStateManager.translate(0.0f, 0.0f, 0.046875f);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(p_177075_1_);
        if (var11) {
            this.renderManager.renderEngine.getTexture(this.func_177076_a(p_177075_1_)).func_174935_a();
        }
        super.doRender(p_177075_1_, p_177075_2_, p_177075_4_, p_177075_6_, p_177075_8_, p_177075_9_);
    }

    protected ResourceLocation func_177076_a(EntityItem p_177076_1_) {
        return TextureMap.locationBlocksTexture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.func_177076_a((EntityItem)p_110775_1_);
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.func_177075_a((EntityItem)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}

