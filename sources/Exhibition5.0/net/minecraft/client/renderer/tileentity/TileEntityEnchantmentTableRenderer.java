// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.client.model.ModelBook;
import net.minecraft.util.ResourceLocation;

public class TileEntityEnchantmentTableRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation field_147540_b;
    private ModelBook field_147541_c;
    private static final String __OBFID = "CL_00002470";
    
    public TileEntityEnchantmentTableRenderer() {
        this.field_147541_c = new ModelBook();
    }
    
    public void func_180537_a(final TileEntityEnchantmentTable p_180537_1_, final double p_180537_2_, final double p_180537_4_, final double p_180537_6_, final float p_180537_8_, final int p_180537_9_) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180537_2_ + 0.5f, (float)p_180537_4_ + 0.75f, (float)p_180537_6_ + 0.5f);
        final float var10 = p_180537_1_.tickCount + p_180537_8_;
        GlStateManager.translate(0.0f, 0.1f + MathHelper.sin(var10 * 0.1f) * 0.01f, 0.0f);
        float var11;
        for (var11 = p_180537_1_.bookRotation - p_180537_1_.bookRotationPrev; var11 >= 3.1415927f; var11 -= 6.2831855f) {}
        while (var11 < -3.1415927f) {
            var11 += 6.2831855f;
        }
        final float var12 = p_180537_1_.bookRotationPrev + var11 * p_180537_8_;
        GlStateManager.rotate(-var12 * 180.0f / 3.1415927f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(80.0f, 0.0f, 0.0f, 1.0f);
        this.bindTexture(TileEntityEnchantmentTableRenderer.field_147540_b);
        float var13 = p_180537_1_.pageFlipPrev + (p_180537_1_.pageFlip - p_180537_1_.pageFlipPrev) * p_180537_8_ + 0.25f;
        float var14 = p_180537_1_.pageFlipPrev + (p_180537_1_.pageFlip - p_180537_1_.pageFlipPrev) * p_180537_8_ + 0.75f;
        var13 = (var13 - MathHelper.truncateDoubleToInt(var13)) * 1.6f - 0.3f;
        var14 = (var14 - MathHelper.truncateDoubleToInt(var14)) * 1.6f - 0.3f;
        if (var13 < 0.0f) {
            var13 = 0.0f;
        }
        if (var14 < 0.0f) {
            var14 = 0.0f;
        }
        if (var13 > 1.0f) {
            var13 = 1.0f;
        }
        if (var14 > 1.0f) {
            var14 = 1.0f;
        }
        final float var15 = p_180537_1_.bookSpreadPrev + (p_180537_1_.bookSpread - p_180537_1_.bookSpreadPrev) * p_180537_8_;
        GlStateManager.enableCull();
        this.field_147541_c.render(null, var10, var13, var14, var15, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
    }
    
    @Override
    public void renderTileEntityAt(final TileEntity p_180535_1_, final double p_180535_2_, final double p_180535_4_, final double p_180535_6_, final float p_180535_8_, final int p_180535_9_) {
        this.func_180537_a((TileEntityEnchantmentTable)p_180535_1_, p_180535_2_, p_180535_4_, p_180535_6_, p_180535_8_, p_180535_9_);
    }
    
    static {
        field_147540_b = new ResourceLocation("textures/entity/enchanting_table_book.png");
    }
}
