package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer {
   public static void renderMob(MobSpawnerBaseLogic var0, double var1, double var3, double var5, float var7) {
      Entity var8 = var0.func_180612_a(var0.getSpawnerWorld());
      if (var8 != null) {
         float var9 = 0.4375F;
         GlStateManager.translate(0.0F, 0.4F, 0.0F);
         GlStateManager.rotate((float)(var0.getPrevMobRotation() + (var0.getMobRotation() - var0.getPrevMobRotation()) * (double)var7) * 10.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(-30.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.translate(0.0F, -0.4F, 0.0F);
         GlStateManager.scale(var9, var9, var9);
         var8.setLocationAndAngles(var1, var3, var5, 0.0F, 0.0F);
         Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(var8, 0.0D, 0.0D, 0.0D, 0.0F, var7);
      }

   }

   public void renderTileEntityAt(TileEntityMobSpawner var1, double var2, double var4, double var6, float var8, int var9) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)var2 + 0.5F, (float)var4, (float)var6 + 0.5F);
      renderMob(var1.getSpawnerBaseLogic(), var2, var4, var6, var8);
      GlStateManager.popMatrix();
   }

   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int var9) {
      this.renderTileEntityAt((TileEntityMobSpawner)var1, var2, var4, var6, var8, var9);
   }
}
