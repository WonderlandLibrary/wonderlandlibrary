// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBook;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityEnchantmentTable;

public class TileEntityEnchantmentTableRenderer extends TileEntitySpecialRenderer<TileEntityEnchantmentTable>
{
    private static final ResourceLocation TEXTURE_BOOK;
    private final ModelBook modelBook;
    
    public TileEntityEnchantmentTableRenderer() {
        this.modelBook = new ModelBook();
    }
    
    @Override
    public void render(final TileEntityEnchantmentTable te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.5f, (float)y + 0.75f, (float)z + 0.5f);
        final float f = te.tickCount + partialTicks;
        GlStateManager.translate(0.0f, 0.1f + MathHelper.sin(f * 0.1f) * 0.01f, 0.0f);
        float f2;
        for (f2 = te.bookRotation - te.bookRotationPrev; f2 >= 3.1415927f; f2 -= 6.2831855f) {}
        while (f2 < -3.1415927f) {
            f2 += 6.2831855f;
        }
        final float f3 = te.bookRotationPrev + f2 * partialTicks;
        GlStateManager.rotate(-f3 * 57.295776f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(80.0f, 0.0f, 0.0f, 1.0f);
        this.bindTexture(TileEntityEnchantmentTableRenderer.TEXTURE_BOOK);
        float f4 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.25f;
        float f5 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * partialTicks + 0.75f;
        f4 = (f4 - MathHelper.fastFloor(f4)) * 1.6f - 0.3f;
        f5 = (f5 - MathHelper.fastFloor(f5)) * 1.6f - 0.3f;
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        if (f5 > 1.0f) {
            f5 = 1.0f;
        }
        final float f6 = te.bookSpreadPrev + (te.bookSpread - te.bookSpreadPrev) * partialTicks;
        GlStateManager.enableCull();
        this.modelBook.render(null, f, f4, f5, f6, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
    }
    
    static {
        TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
    }
}
