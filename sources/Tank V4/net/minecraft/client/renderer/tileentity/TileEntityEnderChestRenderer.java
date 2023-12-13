package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.ResourceLocation;

public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer {
   private static final ResourceLocation ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");
   private ModelChest field_147521_c = new ModelChest();

   public void renderTileEntityAt(TileEntityEnderChest var1, double var2, double var4, double var6, float var8, int var9) {
      int var10 = 0;
      if (var1.hasWorldObj()) {
         var10 = var1.getBlockMetadata();
      }

      if (var9 >= 0) {
         this.bindTexture(DESTROY_STAGES[var9]);
         GlStateManager.matrixMode(5890);
         GlStateManager.pushMatrix();
         GlStateManager.scale(4.0F, 4.0F, 1.0F);
         GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
         GlStateManager.matrixMode(5888);
      } else {
         this.bindTexture(ENDER_CHEST_TEXTURE);
      }

      GlStateManager.pushMatrix();
      GlStateManager.enableRescaleNormal();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.translate((float)var2, (float)var4 + 1.0F, (float)var6 + 1.0F);
      GlStateManager.scale(1.0F, -1.0F, -1.0F);
      GlStateManager.translate(0.5F, 0.5F, 0.5F);
      short var11 = 0;
      if (var10 == 2) {
         var11 = 180;
      }

      if (var10 == 3) {
         var11 = 0;
      }

      if (var10 == 4) {
         var11 = 90;
      }

      if (var10 == 5) {
         var11 = -90;
      }

      GlStateManager.rotate((float)var11, 0.0F, 1.0F, 0.0F);
      GlStateManager.translate(-0.5F, -0.5F, -0.5F);
      float var12 = var1.prevLidAngle + (var1.lidAngle - var1.prevLidAngle) * var8;
      var12 = 1.0F - var12;
      var12 = 1.0F - var12 * var12 * var12;
      this.field_147521_c.chestLid.rotateAngleX = -(var12 * 3.1415927F / 2.0F);
      this.field_147521_c.renderAll();
      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if (var9 >= 0) {
         GlStateManager.matrixMode(5890);
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5888);
      }

   }

   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int var9) {
      this.renderTileEntityAt((TileEntityEnderChest)var1, var2, var4, var6, var8, var9);
   }
}
