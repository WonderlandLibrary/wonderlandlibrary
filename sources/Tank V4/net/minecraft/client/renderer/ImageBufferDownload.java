package net.minecraft.client.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

public class ImageBufferDownload implements IImageBuffer {
   private static final String __OBFID = "CL_00000956";
   private int imageHeight;
   private int[] imageData;
   private int imageWidth;

   private void setAreaOpaque(int var1, int var2, int var3, int var4) {
      for(int var5 = var1; var5 < var3; ++var5) {
         for(int var6 = var2; var6 < var4; ++var6) {
            int[] var10000 = this.imageData;
            int var10001 = var5 + var6 * this.imageWidth;
            var10000[var10001] |= -16777216;
         }
      }

   }

   public BufferedImage parseUserSkin(BufferedImage var1) {
      if (var1 == null) {
         return null;
      } else {
         this.imageWidth = 64;
         this.imageHeight = 64;
         int var2 = var1.getWidth();
         int var3 = var1.getHeight();

         int var4;
         for(var4 = 1; this.imageWidth < var2 || this.imageHeight < var3; var4 *= 2) {
            this.imageWidth *= 2;
            this.imageHeight *= 2;
         }

         BufferedImage var5 = new BufferedImage(this.imageWidth, this.imageHeight, 2);
         Graphics var6 = var5.getGraphics();
         var6.drawImage(var1, 0, 0, (ImageObserver)null);
         if (var1.getHeight() == 32 * var4) {
            var6.drawImage(var5, 24 * var4, 48 * var4, 20 * var4, 52 * var4, 4 * var4, 16 * var4, 8 * var4, 20 * var4, (ImageObserver)null);
            var6.drawImage(var5, 28 * var4, 48 * var4, 24 * var4, 52 * var4, 8 * var4, 16 * var4, 12 * var4, 20 * var4, (ImageObserver)null);
            var6.drawImage(var5, 20 * var4, 52 * var4, 16 * var4, 64 * var4, 8 * var4, 20 * var4, 12 * var4, 32 * var4, (ImageObserver)null);
            var6.drawImage(var5, 24 * var4, 52 * var4, 20 * var4, 64 * var4, 4 * var4, 20 * var4, 8 * var4, 32 * var4, (ImageObserver)null);
            var6.drawImage(var5, 28 * var4, 52 * var4, 24 * var4, 64 * var4, 0 * var4, 20 * var4, 4 * var4, 32 * var4, (ImageObserver)null);
            var6.drawImage(var5, 32 * var4, 52 * var4, 28 * var4, 64 * var4, 12 * var4, 20 * var4, 16 * var4, 32 * var4, (ImageObserver)null);
            var6.drawImage(var5, 40 * var4, 48 * var4, 36 * var4, 52 * var4, 44 * var4, 16 * var4, 48 * var4, 20 * var4, (ImageObserver)null);
            var6.drawImage(var5, 44 * var4, 48 * var4, 40 * var4, 52 * var4, 48 * var4, 16 * var4, 52 * var4, 20 * var4, (ImageObserver)null);
            var6.drawImage(var5, 36 * var4, 52 * var4, 32 * var4, 64 * var4, 48 * var4, 20 * var4, 52 * var4, 32 * var4, (ImageObserver)null);
            var6.drawImage(var5, 40 * var4, 52 * var4, 36 * var4, 64 * var4, 44 * var4, 20 * var4, 48 * var4, 32 * var4, (ImageObserver)null);
            var6.drawImage(var5, 44 * var4, 52 * var4, 40 * var4, 64 * var4, 40 * var4, 20 * var4, 44 * var4, 32 * var4, (ImageObserver)null);
            var6.drawImage(var5, 48 * var4, 52 * var4, 44 * var4, 64 * var4, 52 * var4, 20 * var4, 56 * var4, 32 * var4, (ImageObserver)null);
         }

         var6.dispose();
         this.imageData = ((DataBufferInt)var5.getRaster().getDataBuffer()).getData();
         this.setAreaOpaque(0, 0, 32 * var4, 16 * var4);
         this.setAreaTransparent(32 * var4, 0, 64 * var4, 32 * var4);
         this.setAreaOpaque(0, 16 * var4, 64 * var4, 32 * var4);
         this.setAreaTransparent(0, 32 * var4, 16 * var4, 48 * var4);
         this.setAreaTransparent(16 * var4, 32 * var4, 40 * var4, 48 * var4);
         this.setAreaTransparent(40 * var4, 32 * var4, 56 * var4, 48 * var4);
         this.setAreaTransparent(0, 48 * var4, 16 * var4, 64 * var4);
         this.setAreaOpaque(16 * var4, 48 * var4, 48 * var4, 64 * var4);
         this.setAreaTransparent(48 * var4, 48 * var4, 64 * var4, 64 * var4);
         return var5;
      }
   }

   private void setAreaTransparent(int var1, int var2, int var3, int var4) {
      if (var3 < var4) {
         for(int var5 = var1; var5 < var3; ++var5) {
            for(int var6 = var2; var6 < var4; ++var6) {
               int[] var10003 = this.imageData;
               int var10004 = var5 + var6 * this.imageWidth;
               var10003[var10004] &= 16777215;
            }
         }
      }

   }

   public void skinAvailable() {
   }
}
