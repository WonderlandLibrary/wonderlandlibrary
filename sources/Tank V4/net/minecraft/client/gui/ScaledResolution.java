package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class ScaledResolution {
   private int scaleFactor;
   private final double scaledHeightD;
   private final double scaledWidthD;
   private int scaledWidth;
   private static int scaledHeight;

   public double getScaledWidth_double() {
      return this.scaledWidthD;
   }

   public int getScaledWidth() {
      return this.scaledWidth;
   }

   public static int getScaledHeight() {
      return scaledHeight;
   }

   public int getScaleFactor() {
      return this.scaleFactor;
   }

   public double getScaledHeight_double() {
      return this.scaledHeightD;
   }

   public ScaledResolution(Minecraft var1) {
      this.scaledWidth = var1.displayWidth;
      scaledHeight = var1.displayHeight;
      this.scaleFactor = 1;
      boolean var2 = var1.isUnicode();
      int var3 = var1.gameSettings.guiScale;
      if (var3 == 0) {
         var3 = 1000;
      }

      while(this.scaleFactor < var3 && this.scaledWidth / (this.scaleFactor + 1) >= 320 && scaledHeight / (this.scaleFactor + 1) >= 240) {
         ++this.scaleFactor;
      }

      if (var2 && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
         --this.scaleFactor;
      }

      this.scaledWidthD = (double)this.scaledWidth / (double)this.scaleFactor;
      this.scaledHeightD = (double)scaledHeight / (double)this.scaleFactor;
      this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
      scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
   }
}
