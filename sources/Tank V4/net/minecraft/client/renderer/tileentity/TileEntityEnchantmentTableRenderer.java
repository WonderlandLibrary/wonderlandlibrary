package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TileEntityEnchantmentTableRenderer extends TileEntitySpecialRenderer {
   private static final ResourceLocation TEXTURE_BOOK = new ResourceLocation("textures/entity/enchanting_table_book.png");
   private ModelBook field_147541_c = new ModelBook();

   public void renderTileEntityAt(TileEntityEnchantmentTable var1, double var2, double var4, double var6, float var8, int var9) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4 + 0.75F, (float)var6 + 0.5F);
      float var10 = (float)var1.tickCount + var8;
      GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(var10 * 0.1F) * 0.01F, 0.0F);

      float var11;
      for(var11 = var1.bookRotation - var1.bookRotationPrev; var11 >= 3.1415927F; var11 -= 6.2831855F) {
      }

      while(var11 < -3.1415927F) {
         var11 += 6.2831855F;
      }

      float var12 = var1.bookRotationPrev + var11 * var8;
      GlStateManager.rotate(-var12 * 180.0F / 3.1415927F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
      this.bindTexture(TEXTURE_BOOK);
      float var13 = var1.pageFlipPrev + (var1.pageFlip - var1.pageFlipPrev) * var8 + 0.25F;
      float var14 = var1.pageFlipPrev + (var1.pageFlip - var1.pageFlipPrev) * var8 + 0.75F;
      var13 = (var13 - (float)MathHelper.truncateDoubleToInt((double)var13)) * 1.6F - 0.3F;
      var14 = (var14 - (float)MathHelper.truncateDoubleToInt((double)var14)) * 1.6F - 0.3F;
      if (var13 < 0.0F) {
         var13 = 0.0F;
      }

      if (var14 < 0.0F) {
         var14 = 0.0F;
      }

      if (var13 > 1.0F) {
         var13 = 1.0F;
      }

      if (var14 > 1.0F) {
         var14 = 1.0F;
      }

      float var15 = var1.bookSpreadPrev + (var1.bookSpread - var1.bookSpreadPrev) * var8;
      GlStateManager.enableCull();
      this.field_147541_c.render((Entity)null, var10, var13, var14, var15, 0.0F, 0.0625F);
      GlStateManager.popMatrix();
   }

   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int var9) {
      this.renderTileEntityAt((TileEntityEnchantmentTable)var1, var2, var4, var6, var8, var9);
   }
}
