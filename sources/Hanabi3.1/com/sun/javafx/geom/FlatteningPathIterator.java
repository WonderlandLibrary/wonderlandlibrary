package com.sun.javafx.geom;

import java.util.NoSuchElementException;

public class FlatteningPathIterator implements PathIterator {
   static final int GROW_SIZE = 24;
   PathIterator src;
   float squareflat;
   int limit;
   volatile float[] hold;
   float curx;
   float cury;
   float movx;
   float movy;
   int holdType;
   int holdEnd;
   int holdIndex;
   int[] levels;
   int levelIndex;
   boolean done;

   public FlatteningPathIterator(PathIterator var1, float var2) {
      this(var1, var2, 10);
   }

   public FlatteningPathIterator(PathIterator var1, float var2, int var3) {
      this.hold = new float[14];
      if (var2 < 0.0F) {
         throw new IllegalArgumentException("flatness must be >= 0");
      } else if (var3 < 0) {
         throw new IllegalArgumentException("limit must be >= 0");
      } else {
         this.src = var1;
         this.squareflat = var2 * var2;
         this.limit = var3;
         this.levels = new int[var3 + 1];
         this.next(false);
      }
   }

   public float getFlatness() {
      return (float)Math.sqrt((double)this.squareflat);
   }

   public int getRecursionLimit() {
      return this.limit;
   }

   public int getWindingRule() {
      return this.src.getWindingRule();
   }

   public boolean isDone() {
      return this.done;
   }

   void ensureHoldCapacity(int var1) {
      if (this.holdIndex - var1 < 0) {
         int var2 = this.hold.length - this.holdIndex;
         int var3 = this.hold.length + 24;
         float[] var4 = new float[var3];
         System.arraycopy(this.hold, this.holdIndex, var4, this.holdIndex + 24, var2);
         this.hold = var4;
         this.holdIndex += 24;
         this.holdEnd += 24;
      }

   }

   public void next() {
      this.next(true);
   }

   private void next(boolean var1) {
      if (this.holdIndex >= this.holdEnd) {
         if (var1) {
            this.src.next();
         }

         if (this.src.isDone()) {
            this.done = true;
            return;
         }

         this.holdType = this.src.currentSegment(this.hold);
         this.levelIndex = 0;
         this.levels[0] = 0;
      }

      int var2;
      switch (this.holdType) {
         case 0:
         case 1:
            this.curx = this.hold[0];
            this.cury = this.hold[1];
            if (this.holdType == 0) {
               this.movx = this.curx;
               this.movy = this.cury;
            }

            this.holdIndex = 0;
            this.holdEnd = 0;
            break;
         case 2:
            if (this.holdIndex >= this.holdEnd) {
               this.holdIndex = this.hold.length - 6;
               this.holdEnd = this.hold.length - 2;
               this.hold[this.holdIndex + 0] = this.curx;
               this.hold[this.holdIndex + 1] = this.cury;
               this.hold[this.holdIndex + 2] = this.hold[0];
               this.hold[this.holdIndex + 3] = this.hold[1];
               this.hold[this.holdIndex + 4] = this.curx = this.hold[2];
               this.hold[this.holdIndex + 5] = this.cury = this.hold[3];
            }

            for(var2 = this.levels[this.levelIndex]; var2 < this.limit && !(QuadCurve2D.getFlatnessSq(this.hold, this.holdIndex) < this.squareflat); this.levels[this.levelIndex] = var2) {
               this.ensureHoldCapacity(4);
               QuadCurve2D.subdivide(this.hold, this.holdIndex, this.hold, this.holdIndex - 4, this.hold, this.holdIndex);
               this.holdIndex -= 4;
               ++var2;
               this.levels[this.levelIndex] = var2;
               ++this.levelIndex;
            }

            this.holdIndex += 4;
            --this.levelIndex;
            break;
         case 3:
            if (this.holdIndex >= this.holdEnd) {
               this.holdIndex = this.hold.length - 8;
               this.holdEnd = this.hold.length - 2;
               this.hold[this.holdIndex + 0] = this.curx;
               this.hold[this.holdIndex + 1] = this.cury;
               this.hold[this.holdIndex + 2] = this.hold[0];
               this.hold[this.holdIndex + 3] = this.hold[1];
               this.hold[this.holdIndex + 4] = this.hold[2];
               this.hold[this.holdIndex + 5] = this.hold[3];
               this.hold[this.holdIndex + 6] = this.curx = this.hold[4];
               this.hold[this.holdIndex + 7] = this.cury = this.hold[5];
            }

            for(var2 = this.levels[this.levelIndex]; var2 < this.limit && !(CubicCurve2D.getFlatnessSq(this.hold, this.holdIndex) < this.squareflat); this.levels[this.levelIndex] = var2) {
               this.ensureHoldCapacity(6);
               CubicCurve2D.subdivide(this.hold, this.holdIndex, this.hold, this.holdIndex - 6, this.hold, this.holdIndex);
               this.holdIndex -= 6;
               ++var2;
               this.levels[this.levelIndex] = var2;
               ++this.levelIndex;
            }

            this.holdIndex += 6;
            --this.levelIndex;
            break;
         case 4:
            this.curx = this.movx;
            this.cury = this.movy;
            this.holdIndex = 0;
            this.holdEnd = 0;
      }

   }

   public int currentSegment(float[] var1) {
      if (this.isDone()) {
         throw new NoSuchElementException("flattening iterator out of bounds");
      } else {
         int var2 = this.holdType;
         if (var2 != 4) {
            var1[0] = this.hold[this.holdIndex + 0];
            var1[1] = this.hold[this.holdIndex + 1];
            if (var2 != 0) {
               var2 = 1;
            }
         }

         return var2;
      }
   }
}
