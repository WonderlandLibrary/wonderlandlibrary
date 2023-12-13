package com.sun.prism.impl.packrect;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Texture;
import java.util.ArrayList;
import java.util.List;

public class RectanglePacker {
   private Texture backingStore;
   private List levels;
   private static final int MIN_SIZE = 8;
   private static final int ROUND_UP = 4;
   private int recentUsedLevelIndex;
   private int length;
   private int size;
   private int sizeOffset;
   private int x;
   private int y;
   private boolean vertical;

   public RectanglePacker(Texture var1, int var2, int var3, int var4, int var5, boolean var6) {
      this.levels = new ArrayList(150);
      this.recentUsedLevelIndex = 0;
      this.backingStore = var1;
      if (var6) {
         this.length = var5;
         this.size = var4;
      } else {
         this.length = var4;
         this.size = var5;
      }

      this.x = var2;
      this.y = var3;
      this.vertical = var6;
   }

   public RectanglePacker(Texture var1, int var2, int var3) {
      this(var1, 0, 0, var2, var3, false);
   }

   public final Texture getBackingStore() {
      return this.backingStore;
   }

   public final boolean add(Rectangle var1) {
      int var2 = this.vertical ? var1.height : var1.width;
      int var3 = this.vertical ? var1.width : var1.height;
      if (var2 > this.length) {
         return false;
      } else if (var3 > this.size) {
         return false;
      } else {
         int var4 = 8 > var3 ? 8 : var3;
         var4 = var4 + 4 - 1 - (var4 - 1) % 4;
         int var5;
         if (this.recentUsedLevelIndex < this.levels.size() && ((Level)this.levels.get(this.recentUsedLevelIndex)).size != var4) {
            var5 = binarySearch(this.levels, var4);
         } else {
            var5 = this.recentUsedLevelIndex;
         }

         boolean var6 = this.sizeOffset + var4 <= this.size;
         int var7 = var5;

         for(int var8 = this.levels.size(); var7 < var8; ++var7) {
            Level var9 = (Level)this.levels.get(var7);
            if (var9.size > var4 + 8 && var6) {
               break;
            }

            if (var9.add(var1, this.x, this.y, var2, var3, this.vertical)) {
               this.recentUsedLevelIndex = var7;
               return true;
            }
         }

         if (!var6) {
            return false;
         } else {
            Level var10 = new Level(this.length, var4, this.sizeOffset);
            this.sizeOffset += var4;
            if (var5 < this.levels.size() && ((Level)this.levels.get(var5)).size <= var4) {
               this.levels.add(var5 + 1, var10);
               this.recentUsedLevelIndex = var5 + 1;
            } else {
               this.levels.add(var5, var10);
               this.recentUsedLevelIndex = var5;
            }

            return var10.add(var1, this.x, this.y, var2, var3, this.vertical);
         }
      }
   }

   public void clear() {
      this.levels.clear();
      this.sizeOffset = 0;
      this.recentUsedLevelIndex = 0;
   }

   public void dispose() {
      if (this.backingStore != null) {
         this.backingStore.dispose();
      }

      this.backingStore = null;
      this.levels = null;
   }

   private static int binarySearch(List var0, int var1) {
      int var2 = var1 + 1;
      int var3 = 0;
      int var4 = var0.size() - 1;
      int var5 = 0;
      int var6 = 0;
      if (var4 < 0) {
         return 0;
      } else {
         while(var3 <= var4) {
            var5 = (var3 + var4) / 2;
            var6 = ((Level)var0.get(var5)).size;
            if (var2 < var6) {
               var4 = var5 - 1;
            } else {
               var3 = var5 + 1;
            }
         }

         if (var6 < var1) {
            return var5 + 1;
         } else if (var6 > var1) {
            return var5 > 0 ? var5 - 1 : 0;
         } else {
            return var5;
         }
      }
   }
}
