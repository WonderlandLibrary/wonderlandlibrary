package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.logging.PulseLogger;
import com.sun.prism.Graphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.packrect.RectanglePacker;
import java.util.HashMap;
import javafx.scene.layout.Background;

class RegionImageCache {
   private static final int MAX_SIZE = 90000;
   private static final int WIDTH = 1024;
   private static final int HEIGHT = 1024;
   private HashMap imageMap = new HashMap();
   private RTTexture backingStore;
   private RectanglePacker hPacker;
   private RectanglePacker vPacker;

   RegionImageCache(ResourceFactory var1) {
      Texture.WrapMode var2;
      byte var3;
      if (var1.isWrapModeSupported(Texture.WrapMode.CLAMP_TO_ZERO)) {
         var2 = Texture.WrapMode.CLAMP_TO_ZERO;
         var3 = 0;
      } else {
         var2 = Texture.WrapMode.CLAMP_NOT_NEEDED;
         var3 = 1;
      }

      this.backingStore = var1.createRTTexture(2048, 1024, var2);
      this.backingStore.contentsUseful();
      this.backingStore.makePermanent();
      var1.setRegionTexture(this.backingStore);
      this.hPacker = new RectanglePacker(this.backingStore, var3, var3, 1024 - var3, 1024 - var3, false);
      this.vPacker = new RectanglePacker(this.backingStore, 1024, var3, 1024, 1024 - var3, true);
   }

   boolean isImageCachable(int var1, int var2) {
      return 0 < var1 && var1 < 1024 && 0 < var2 && var2 < 1024 && var1 * var2 < 90000;
   }

   RTTexture getBackingStore() {
      return this.backingStore;
   }

   boolean getImageLocation(Integer var1, Rectangle var2, Background var3, Shape var4, Graphics var5) {
      CachedImage var6 = (CachedImage)this.imageMap.get(var1);
      if (var6 != null) {
         if (var6.equals(var2.width, var2.height, var3, var4)) {
            var2.x = var6.x;
            var2.y = var6.y;
            return false;
         } else {
            var2.width = var2.height = -1;
            return false;
         }
      } else {
         boolean var7 = var2.height > 64;
         RectanglePacker var8 = var7 ? this.vPacker : this.hPacker;
         if (!var8.add(var2)) {
            var5.sync();
            this.vPacker.clear();
            this.hPacker.clear();
            this.imageMap.clear();
            var8.add(var2);
            this.backingStore.createGraphics().clear();
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
               PulseLogger.incrementCounter("Region image cache flushed");
            }
         }

         this.imageMap.put(var1, new CachedImage(var2, var3, var4));
         return true;
      }
   }

   static class CachedImage {
      Background background;
      Shape shape;
      int x;
      int y;
      int width;
      int height;

      CachedImage(Rectangle var1, Background var2, Shape var3) {
         this.x = var1.x;
         this.y = var1.y;
         this.width = var1.width;
         this.height = var1.height;
         this.background = var2;
         this.shape = var3;
      }

      public boolean equals(int var1, int var2, Background var3, Shape var4) {
         boolean var10000;
         label42: {
            if (this.width == var1 && this.height == var2) {
               label36: {
                  if (this.background == null) {
                     if (var3 != null) {
                        break label36;
                     }
                  } else if (!this.background.equals(var3)) {
                     break label36;
                  }

                  if (this.shape == null) {
                     if (var4 == null) {
                        break label42;
                     }
                  } else if (this.shape.equals(var4)) {
                     break label42;
                  }
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }
   }
}
