package com.sun.javafx.geom;

import java.util.Arrays;

public final class DirtyRegionContainer {
   public static final int DTR_OK = 1;
   public static final int DTR_CONTAINS_CLIP = 0;
   private RectBounds[] dirtyRegions;
   private int emptyIndex;
   private int[][] heap;
   private int heapSize;
   private long invalidMask;

   public DirtyRegionContainer(int var1) {
      this.initDirtyRegions(var1);
   }

   public boolean equals(Object var1) {
      if (var1 instanceof DirtyRegionContainer) {
         DirtyRegionContainer var2 = (DirtyRegionContainer)var1;
         if (this.size() != var2.size()) {
            return false;
         } else {
            for(int var3 = 0; var3 < this.emptyIndex; ++var3) {
               if (!this.getDirtyRegion(var3).equals(var2.getDirtyRegion(var3))) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = 5;
      var1 = 97 * var1 + Arrays.deepHashCode(this.dirtyRegions);
      var1 = 97 * var1 + this.emptyIndex;
      return var1;
   }

   public DirtyRegionContainer deriveWithNewRegion(RectBounds var1) {
      if (var1 == null) {
         return this;
      } else {
         this.dirtyRegions[0].deriveWithNewBounds((BaseBounds)var1);
         this.emptyIndex = 1;
         return this;
      }
   }

   public DirtyRegionContainer deriveWithNewRegions(RectBounds[] var1) {
      if (var1 != null && var1.length != 0) {
         if (var1.length > this.maxSpace()) {
            this.initDirtyRegions(var1.length);
         }

         this.regioncopy(var1, 0, this.dirtyRegions, 0, var1.length);
         this.emptyIndex = var1.length;
         return this;
      } else {
         return this;
      }
   }

   public DirtyRegionContainer deriveWithNewContainer(DirtyRegionContainer var1) {
      if (var1 != null && var1.maxSpace() != 0) {
         if (var1.maxSpace() > this.maxSpace()) {
            this.initDirtyRegions(var1.maxSpace());
         }

         this.regioncopy(var1.dirtyRegions, 0, this.dirtyRegions, 0, var1.emptyIndex);
         this.emptyIndex = var1.emptyIndex;
         return this;
      } else {
         return this;
      }
   }

   private void initDirtyRegions(int var1) {
      this.dirtyRegions = new RectBounds[var1];

      for(int var2 = 0; var2 < var1; ++var2) {
         this.dirtyRegions[var2] = new RectBounds();
      }

      this.emptyIndex = 0;
   }

   public DirtyRegionContainer copy() {
      DirtyRegionContainer var1 = new DirtyRegionContainer(this.maxSpace());
      this.regioncopy(this.dirtyRegions, 0, var1.dirtyRegions, 0, this.emptyIndex);
      var1.emptyIndex = this.emptyIndex;
      return var1;
   }

   public int maxSpace() {
      return this.dirtyRegions.length;
   }

   public RectBounds getDirtyRegion(int var1) {
      return this.dirtyRegions[var1];
   }

   public void setDirtyRegion(int var1, RectBounds var2) {
      this.dirtyRegions[var1] = var2;
   }

   public void addDirtyRegion(RectBounds var1) {
      if (!var1.isEmpty()) {
         int var4 = 0;
         int var5 = this.emptyIndex;

         RectBounds var2;
         for(int var6 = 0; var6 < var5; ++var6) {
            var2 = this.dirtyRegions[var4];
            if (var1.intersects(var2)) {
               var1.unionWith(var2);
               RectBounds var3 = this.dirtyRegions[var4];
               this.dirtyRegions[var4] = this.dirtyRegions[this.emptyIndex - 1];
               this.dirtyRegions[this.emptyIndex - 1] = var3;
               --this.emptyIndex;
            } else {
               ++var4;
            }
         }

         if (this.hasSpace()) {
            var2 = this.dirtyRegions[this.emptyIndex];
            var2.deriveWithNewBounds((BaseBounds)var1);
            ++this.emptyIndex;
         } else {
            if (this.dirtyRegions.length == 1) {
               this.dirtyRegions[0].deriveWithUnion(var1);
            } else {
               this.compress(var1);
            }

         }
      }
   }

   public void merge(DirtyRegionContainer var1) {
      int var2 = var1.size();

      for(int var3 = 0; var3 < var2; ++var3) {
         this.addDirtyRegion(var1.getDirtyRegion(var3));
      }

   }

   public int size() {
      return this.emptyIndex;
   }

   public void reset() {
      this.emptyIndex = 0;
   }

   private RectBounds compress(RectBounds var1) {
      this.compress_heap();
      this.addDirtyRegion(var1);
      return var1;
   }

   private boolean hasSpace() {
      return this.emptyIndex < this.dirtyRegions.length;
   }

   private void regioncopy(RectBounds[] var1, int var2, RectBounds[] var3, int var4, int var5) {
      for(int var7 = 0; var7 < var5; ++var7) {
         RectBounds var6 = var1[var2++];
         if (var6 == null) {
            var3[var4++].makeEmpty();
         } else {
            var3[var4++].deriveWithNewBounds((BaseBounds)var6);
         }
      }

   }

   public boolean checkAndClearRegion(int var1) {
      boolean var2 = false;
      if (this.dirtyRegions[var1].isEmpty()) {
         System.arraycopy(this.dirtyRegions, var1 + 1, this.dirtyRegions, var1, this.emptyIndex - var1 - 1);
         --this.emptyIndex;
         var2 = true;
      }

      return var2;
   }

   public void grow(int var1, int var2) {
      if (var1 != 0 || var2 != 0) {
         for(int var3 = 0; var3 < this.emptyIndex; ++var3) {
            this.getDirtyRegion(var3).grow((float)var1, (float)var2);
         }
      }

   }

   public void roundOut() {
      for(int var1 = 0; var1 < this.emptyIndex; ++var1) {
         this.dirtyRegions[var1].roundOut();
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < this.emptyIndex; ++var2) {
         var1.append(this.dirtyRegions[var2]);
         var1.append('\n');
      }

      return var1.toString();
   }

   private void heapCompress() {
      this.invalidMask = 0L;
      int[] var1 = new int[this.dirtyRegions.length];

      for(int var2 = 0; var2 < var1.length; var1[var2] = var2++) {
      }

      int var4;
      for(int var3 = 0; var3 < this.dirtyRegions.length / 2; ++var3) {
         int[] var6 = this.takeMinWithMap(var1);
         var4 = this.resolveMap(var1, var6[1]);
         int var5 = this.resolveMap(var1, var6[2]);
         if (var4 != var5) {
            this.dirtyRegions[var4].deriveWithUnion(this.dirtyRegions[var5]);
            var1[var5] = var4;
            this.invalidMask |= (long)(1 << var4);
            this.invalidMask |= (long)(1 << var5);
         }
      }

      for(var4 = 0; var4 < this.emptyIndex; ++var4) {
         if (var1[var4] != var4) {
            while(var1[this.emptyIndex - 1] != this.emptyIndex - 1) {
               --this.emptyIndex;
            }

            if (var4 < this.emptyIndex - 1) {
               RectBounds var7 = this.dirtyRegions[this.emptyIndex - 1];
               this.dirtyRegions[this.emptyIndex - 1] = this.dirtyRegions[var4];
               this.dirtyRegions[var4] = var7;
               var1[var4] = var4;
               --this.emptyIndex;
            }
         }
      }

   }

   private void heapify() {
      for(int var1 = this.heapSize / 2; var1 >= 0; --var1) {
         this.siftDown(var1);
      }

   }

   private void siftDown(int var1) {
      int var4;
      for(int var2 = this.heapSize >> 1; var1 < var2; var1 = var4) {
         var4 = (var1 << 1) + 1;
         int[] var5 = this.heap[var4];
         if (var4 + 1 < this.heapSize && this.heap[var4 + 1][0] < var5[0]) {
            ++var4;
         }

         if (this.heap[var4][0] >= this.heap[var1][0]) {
            break;
         }

         int[] var3 = this.heap[var4];
         this.heap[var4] = this.heap[var1];
         this.heap[var1] = var3;
      }

   }

   private int[] takeMinWithMap(int[] var1) {
      int[] var2;
      for(var2 = this.heap[0]; ((long)(1 << var2[1] | 1 << var2[2]) & this.invalidMask) > 0L; var2 = this.heap[0]) {
         var2[0] = this.unifiedRegionArea(this.resolveMap(var1, var2[1]), this.resolveMap(var1, var2[2]));
         this.siftDown(0);
         if (this.heap[0] == var2) {
            break;
         }
      }

      this.heap[this.heapSize - 1] = var2;
      this.siftDown(0);
      --this.heapSize;
      return var2;
   }

   private int[] takeMin() {
      int[] var1 = this.heap[0];
      this.heap[0] = this.heap[this.heapSize - 1];
      this.heap[this.heapSize - 1] = var1;
      this.siftDown(0);
      --this.heapSize;
      return var1;
   }

   private int resolveMap(int[] var1, int var2) {
      while(var1[var2] != var2) {
         var2 = var1[var2];
      }

      return var2;
   }

   private int unifiedRegionArea(int var1, int var2) {
      RectBounds var3 = this.dirtyRegions[var1];
      RectBounds var4 = this.dirtyRegions[var2];
      float var5 = var3.getMinX() < var4.getMinX() ? var3.getMinX() : var4.getMinX();
      float var6 = var3.getMinY() < var4.getMinY() ? var3.getMinY() : var4.getMinY();
      float var7 = var3.getMaxX() > var4.getMaxX() ? var3.getMaxX() : var4.getMaxX();
      float var8 = var3.getMaxY() > var4.getMaxY() ? var3.getMaxY() : var4.getMaxY();
      return (int)((var7 - var5) * (var8 - var6));
   }

   private void compress_heap() {
      assert this.dirtyRegions.length == this.emptyIndex;

      int var1;
      if (this.heap == null) {
         var1 = this.dirtyRegions.length;
         this.heap = new int[var1 * (var1 - 1) / 2][3];
      }

      this.heapSize = this.heap.length;
      var1 = 0;

      for(int var2 = 0; var2 < this.dirtyRegions.length - 1; ++var2) {
         for(int var3 = var2 + 1; var3 < this.dirtyRegions.length; this.heap[var1++][2] = var3++) {
            this.heap[var1][0] = this.unifiedRegionArea(var2, var3);
            this.heap[var1][1] = var2;
         }
      }

      this.heapify();
      this.heapCompress();
   }
}
