// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import moonsense.features.modules.type.hud.UHCOverlayModule;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import moonsense.features.SCModule;
import moonsense.features.modules.type.mechanic.ItemPhysicsModule;
import moonsense.config.ModuleConfig;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import java.util.Random;

public class RenderEntityItem extends Render
{
    private final RenderItem field_177080_a;
    private Random field_177079_e;
    private static final String __OBFID = "CL_00002442";
    
    public RenderEntityItem(final RenderManager p_i46167_1_, final RenderItem p_i46167_2_) {
        super(p_i46167_1_);
        this.field_177079_e = new Random();
        this.field_177080_a = p_i46167_2_;
        this.shadowSize = 0.15f;
        this.shadowOpaque = 0.75f;
    }
    
    private int func_177077_a(final EntityItem entity, final double x, final double y, final double z, final float partialTicks, final IBakedModel model) {
        final ItemStack var10 = entity.getEntityItem();
        final Item var11 = var10.getItem();
        if (var11 == null) {
            return 0;
        }
        if (!ModuleConfig.INSTANCE.isEnabled(ItemPhysicsModule.INSTANCE)) {
            final boolean var12 = model.isAmbientOcclusionEnabled();
            final int var13 = this.func_177078_a(var10);
            final float var14 = 0.25f;
            final float var15 = MathHelper.sin((entity.func_174872_o() + partialTicks) / 10.0f + entity.hoverStart) * 0.1f + 0.1f;
            GlStateManager.translate((float)x, (float)y + var15 + 0.25f, (float)z);
            if (var12 || (this.renderManager.options != null && this.renderManager.options.fancyGraphics)) {
                final float var16 = ((entity.func_174872_o() + partialTicks) / 20.0f + entity.hoverStart) * 57.295776f;
                GlStateManager.rotate(var16, 0.0f, 1.0f, 0.0f);
            }
            if (!var12) {
                final float var16 = -0.0f * (var13 - 1) * 0.5f;
                final float var17 = -0.0f * (var13 - 1) * 0.5f;
                final float var18 = -0.046875f * (var13 - 1) * 0.5f;
                GlStateManager.translate(var16, var17, var18);
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            return var13;
        }
        if (var11 != null) {
            final boolean is3d = model.isGui3d();
            final int clumpSize = this.getClumpSize(var10.stackSize);
            final float f = 0.25f;
            final float f2 = MathHelper.sin((entity.func_174872_o() + partialTicks) / 10.0f + entity.hoverStart) * 0.1f + 0.1f;
            GlStateManager.translate((float)x, (float)y + 0.1, (float)z);
            if ((var11.equals(Items.gold_nugget) || var11.equals(Items.gold_ingot) || var11.equals(Item.getItemFromBlock(Blocks.gold_ore)) || var11.equals(Item.getItemFromBlock(Blocks.gold_block)) || var11.equals(Items.golden_carrot) || var11 instanceof ItemAppleGold) && ModuleConfig.INSTANCE.isEnabled(UHCOverlayModule.INSTANCE)) {
                final double scale = UHCOverlayModule.INSTANCE.itemSize.getFloat();
                GlStateManager.scale(scale, 1.0, scale);
            }
            final float hover = ((entity.func_174872_o() + partialTicks) / 20.0f + entity.hoverStart) * 57.295776f;
            final long now = System.nanoTime();
            final ItemPhysicsModule.ItemData data = ItemPhysicsModule.INSTANCE.getItemDataMap().computeIfAbsent(entity, itemStack -> new ItemPhysicsModule.ItemData(System.nanoTime()));
            final long since = now - data.lastUpdate;
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 1.0f);
            GlStateManager.rotate(entity.rotationYaw, 0.0f, 0.0f, 1.0f);
            if (!Minecraft.getMinecraft().isGamePaused()) {
                if (!entity.onGround) {
                    int divisor = 2500000;
                    if (entity.isInWeb) {
                        divisor *= 10;
                    }
                    final ItemPhysicsModule.ItemData itemData = data;
                    itemData.rotation += since / (float)divisor * (10.0f * ItemPhysicsModule.INSTANCE.rotationSpeed.getFloat() / 100.0f);
                }
                else if (data.rotation != 0.0f) {
                    data.rotation = 0.0f;
                }
            }
            GlStateManager.rotate(data.rotation, 0.0f, 1.0f, 0.0f);
            data.lastUpdate = now;
            if (!is3d) {
                final float rotationXAndY = -0.0f * (clumpSize - 1) * 0.5f;
                final float rotationZ = -0.046875f * (clumpSize - 1) * 0.5f;
                GlStateManager.translate(rotationXAndY, rotationXAndY, rotationZ);
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            return ItemPhysicsModule.INSTANCE.stackDroppedItems.getBoolean() ? clumpSize : 1;
        }
        return 0;
    }
    
    private int getClumpSize(final int size) {
        if (size > 48) {
            return 5;
        }
        if (size > 32) {
            return 4;
        }
        if (size > 16) {
            return 3;
        }
        if (size > 1) {
            return 2;
        }
        return 1;
    }
    
    private int func_177078_a(final ItemStack p_177078_1_) {
        byte var2 = 1;
        if (p_177078_1_.stackSize > 48) {
            var2 = 5;
        }
        else if (p_177078_1_.stackSize > 32) {
            var2 = 4;
        }
        else if (p_177078_1_.stackSize > 16) {
            var2 = 3;
        }
        else if (p_177078_1_.stackSize > 1) {
            var2 = 2;
        }
        return var2;
    }
    
    public void func_177075_a(final EntityItem p_177075_1_, final double p_177075_2_, final double p_177075_4_, final double p_177075_6_, final float p_177075_8_, final float p_177075_9_) {
        final ItemStack var10 = p_177075_1_.getEntityItem();
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
        final IBakedModel var12 = this.field_177080_a.getItemModelMesher().getItemModel(var10);
        for (int var13 = this.func_177077_a(p_177075_1_, p_177075_2_, p_177075_4_, p_177075_6_, p_177075_9_, var12), var14 = 0; var14 < var13; ++var14) {
            if (var12.isAmbientOcclusionEnabled()) {
                GlStateManager.pushMatrix();
                if (var14 > 0) {
                    final float var15 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float var16 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    final float var17 = (this.field_177079_e.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    GlStateManager.translate(var15, var16, var17);
                }
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
                this.field_177080_a.func_180454_a(var10, var12);
                GlStateManager.popMatrix();
            }
            else {
                this.field_177080_a.func_180454_a(var10, var12);
                GlStateManager.translate(0.0f, 0.0f, 0.046875f);
            }
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
    
    protected ResourceLocation func_177076_a(final EntityItem p_177076_1_) {
        return TextureMap.locationBlocksTexture;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.func_177076_a((EntityItem)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_177075_a((EntityItem)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
