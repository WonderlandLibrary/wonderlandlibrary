/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityEnchantmentTableRenderer
extends TileEntitySpecialRenderer<TileEntityEnchantmentTable> {
    private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private final ModelBook modelBook = new ModelBook();

    @Override
    public void func_192841_a(TileEntityEnchantmentTable p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_) {
        float f1;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_192841_2_ + 0.5f, (float)p_192841_4_ + 0.75f, (float)p_192841_6_ + 0.5f);
        float f = (float)p_192841_1_.tickCount + p_192841_8_;
        GlStateManager.translate(0.0f, 0.1f + MathHelper.sin(f * 0.1f) * 0.01f, 0.0f);
        for (f1 = p_192841_1_.bookRotation - p_192841_1_.bookRotationPrev; f1 >= (float)Math.PI; f1 -= (float)Math.PI * 2) {
        }
        while (f1 < (float)(-Math.PI)) {
            f1 += (float)Math.PI * 2;
        }
        float f2 = p_192841_1_.bookRotationPrev + f1 * p_192841_8_;
        GlStateManager.rotate(-f2 * 57.295776f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(80.0f, 0.0f, 0.0f, 1.0f);
        this.bindTexture(TEXTURE_BOOK);
        float f3 = p_192841_1_.pageFlipPrev + (p_192841_1_.pageFlip - p_192841_1_.pageFlipPrev) * p_192841_8_ + 0.25f;
        float f4 = p_192841_1_.pageFlipPrev + (p_192841_1_.pageFlip - p_192841_1_.pageFlipPrev) * p_192841_8_ + 0.75f;
        f3 = (f3 - (float)MathHelper.fastFloor(f3)) * 1.6f - 0.3f;
        f4 = (f4 - (float)MathHelper.fastFloor(f4)) * 1.6f - 0.3f;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        if (f4 < 0.0f) {
            f4 = 0.0f;
        }
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        float f5 = p_192841_1_.bookSpreadPrev + (p_192841_1_.bookSpread - p_192841_1_.bookSpreadPrev) * p_192841_8_;
        GlStateManager.enableCull();
        this.modelBook.render(null, f, f3, f4, f5, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
    }
}

