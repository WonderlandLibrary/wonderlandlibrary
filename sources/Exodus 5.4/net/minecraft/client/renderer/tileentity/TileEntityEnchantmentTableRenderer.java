/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TileEntityEnchantmentTableRenderer
extends TileEntitySpecialRenderer<TileEntityEnchantmentTable> {
    private ModelBook field_147541_c = new ModelBook();
    private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");

    @Override
    public void renderTileEntityAt(TileEntityEnchantmentTable tileEntityEnchantmentTable, double d, double d2, double d3, float f, int n) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)d + 0.5f, (float)d2 + 0.75f, (float)d3 + 0.5f);
        float f2 = (float)tileEntityEnchantmentTable.tickCount + f;
        GlStateManager.translate(0.0f, 0.1f + MathHelper.sin(f2 * 0.1f) * 0.01f, 0.0f);
        float f3 = tileEntityEnchantmentTable.bookRotation - tileEntityEnchantmentTable.bookRotationPrev;
        while (f3 >= (float)Math.PI) {
            f3 -= (float)Math.PI * 2;
        }
        while (f3 < (float)(-Math.PI)) {
            f3 += (float)Math.PI * 2;
        }
        float f4 = tileEntityEnchantmentTable.bookRotationPrev + f3 * f;
        GlStateManager.rotate(-f4 * 180.0f / (float)Math.PI, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(80.0f, 0.0f, 0.0f, 1.0f);
        this.bindTexture(TEXTURE_BOOK);
        float f5 = tileEntityEnchantmentTable.pageFlipPrev + (tileEntityEnchantmentTable.pageFlip - tileEntityEnchantmentTable.pageFlipPrev) * f + 0.25f;
        float f6 = tileEntityEnchantmentTable.pageFlipPrev + (tileEntityEnchantmentTable.pageFlip - tileEntityEnchantmentTable.pageFlipPrev) * f + 0.75f;
        f5 = (f5 - (float)MathHelper.truncateDoubleToInt(f5)) * 1.6f - 0.3f;
        f6 = (f6 - (float)MathHelper.truncateDoubleToInt(f6)) * 1.6f - 0.3f;
        if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        if (f6 < 0.0f) {
            f6 = 0.0f;
        }
        if (f5 > 1.0f) {
            f5 = 1.0f;
        }
        if (f6 > 1.0f) {
            f6 = 1.0f;
        }
        float f7 = tileEntityEnchantmentTable.bookSpreadPrev + (tileEntityEnchantmentTable.bookSpread - tileEntityEnchantmentTable.bookSpreadPrev) * f;
        GlStateManager.enableCull();
        this.field_147541_c.render(null, f2, f5, f6, f7, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
    }
}

