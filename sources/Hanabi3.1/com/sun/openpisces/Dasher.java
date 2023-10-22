package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;

public final class Dasher implements PathConsumer2D {
   private final PathConsumer2D out;
   private float[] dash;
   private float startPhase;
   private boolean startDashOn;
   private int startIdx;
   private boolean starting;
   private boolean needsMoveTo;
   private int idx;
   private boolean dashOn;
   private float phase;
   private float sx;
   private float sy;
   private float x0;
   private float y0;
   private float[] curCurvepts;
   static float MAX_CYCLES = 1.6E7F;
   private float[] firstSegmentsBuffer;
   private int firstSegidx;
   private LengthIterator li;

   public Dasher(PathConsumer2D var1, float[] var2, float var3) {
      this(var1);
      this.reset(var2, var3);
   }

   public Dasher(PathConsumer2D var1) {
      this.firstSegmentsBuffer = new float[7];
      this.firstSegidx = 0;
      this.li = null;
      this.out = var1;
      this.curCurvepts = new float[16];
   }

   public void reset(float[] var1, float var2) {
      int var3 = 0;
      this.dashOn = true;
      float var4 = 0.0F;
      float[] var5 = var1;
      int var6 = var1.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         float var8 = var5[var7];
         var4 += var8;
      }

      float var9 = var2 / var4;
      if (var2 < 0.0F) {
         if (-var9 >= MAX_CYCLES) {
            var2 = 0.0F;
         } else {
            var6 = (int)Math.floor((double)(-var9));
            if ((var6 & var1.length & 1) != 0) {
               this.dashOn = !this.dashOn;
            }

            for(var2 += (float)var6 * var4; var2 < 0.0F; this.dashOn = !this.dashOn) {
               --var3;
               if (var3 < 0) {
                  var3 = var1.length - 1;
               }

               var2 += var1[var3];
            }
         }
      } else if (var2 > 0.0F) {
         if (var9 >= MAX_CYCLES) {
            var2 = 0.0F;
         } else {
            var6 = (int)Math.floor((double)var9);
            if ((var6 & var1.length & 1) != 0) {
               this.dashOn = !this.dashOn;
            }

            float var10;
            for(var2 -= (float)var6 * var4; var2 >= (var10 = var1[var3]); this.dashOn = !this.dashOn) {
               var2 -= var10;
               var3 = (var3 + 1) % var1.length;
            }
         }
      }

      this.dash = var1;
      this.startPhase = this.phase = var2;
      this.startDashOn = this.dashOn;
      this.startIdx = var3;
      this.starting = true;
   }

   public void moveTo(float var1, float var2) {
      if (this.firstSegidx > 0) {
         this.out.moveTo(this.sx, this.sy);
         this.emitFirstSegments();
      }

      this.needsMoveTo = true;
      this.idx = this.startIdx;
      this.dashOn = this.startDashOn;
      this.phase = this.startPhase;
      this.sx = this.x0 = var1;
      this.sy = this.y0 = var2;
      this.starting = true;
   }

   private void emitSeg(float[] var1, int var2, int var3) {
      switch (var3) {
         case 4:
            this.out.lineTo(var1[var2], var1[var2 + 1]);
         case 5:
         case 7:
         default:
            break;
         case 6:
            this.out.quadTo(var1[var2 + 0], var1[var2 + 1], var1[var2 + 2], var1[var2 + 3]);
            break;
         case 8:
            this.out.curveTo(var1[var2 + 0], var1[var2 + 1], var1[var2 + 2], var1[var2 + 3], var1[var2 + 4], var1[var2 + 5]);
      }

   }

   private void emitFirstSegments() {
      for(int var1 = 0; var1 < this.firstSegidx; var1 += (int)this.firstSegmentsBuffer[var1] - 1) {
         this.emitSeg(this.firstSegmentsBuffer, var1 + 1, (int)this.firstSegmentsBuffer[var1]);
      }

      this.firstSegidx = 0;
   }

   private void goTo(float[] var1, int var2, int var3) {
      float var4 = var1[var2 + var3 - 4];
      float var5 = var1[var2 + var3 - 3];
      if (this.dashOn) {
         if (this.starting) {
            this.firstSegmentsBuffer = Helpers.widenArray(this.firstSegmentsBuffer, this.firstSegidx, var3 - 1);
            this.firstSegmentsBuffer[this.firstSegidx++] = (float)var3;
            System.arraycopy(var1, var2, this.firstSegmentsBuffer, this.firstSegidx, var3 - 2);
            this.firstSegidx += var3 - 2;
         } else {
            if (this.needsMoveTo) {
               this.out.moveTo(this.x0, this.y0);
               this.needsMoveTo = false;
            }

            this.emitSeg(var1, var2, var3);
         }
      } else {
         this.starting = false;
         this.needsMoveTo = true;
      }

      this.x0 = var4;
      this.y0 = var5;
   }

   public void lineTo(float var1, float var2) {
      float var3 = var1 - this.x0;
      float var4 = var2 - this.y0;
      float var5 = (float)Math.sqrt((double)(var3 * var3 + var4 * var4));
      if (var5 != 0.0F) {
         float var6 = var3 / var5;
         float var7 = var4 / var5;

         while(true) {
            float var8 = this.dash[this.idx] - this.phase;
            if (var5 <= var8) {
               this.curCurvepts[0] = var1;
               this.curCurvepts[1] = var2;
               this.goTo(this.curCurvepts, 0, 4);
               this.phase += var5;
               if (var5 == var8) {
                  this.phase = 0.0F;
                  this.idx = (this.idx + 1) % this.dash.length;
                  this.dashOn = !this.dashOn;
               }

               return;
            }

            float var9 = this.dash[this.idx] * var6;
            float var10 = this.dash[this.idx] * var7;
            if (this.phase == 0.0F) {
               this.curCurvepts[0] = this.x0 + var9;
               this.curCurvepts[1] = this.y0 + var10;
            } else {
               float var11 = var8 / this.dash[this.idx];
               this.curCurvepts[0] = this.x0 + var11 * var9;
               this.curCurvepts[1] = this.y0 + var11 * var10;
            }

            this.goTo(this.curCurvepts, 0, 4);
            var5 -= var8;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
            this.phase = 0.0F;
         }
      }
   }

   private void somethingTo(int var1) {
      if (!pointCurve(this.curCurvepts, var1)) {
         if (this.li == null) {
            this.li = new LengthIterator(4, 0.01F);
         }

         this.li.initializeIterationOnCurve(this.curCurvepts, var1);
         int var2 = 0;
         float var3 = 0.0F;
         float var4 = 0.0F;

         for(float var5 = this.dash[this.idx] - this.phase; (var4 = this.li.next(var5)) < 1.0F; var5 = this.dash[this.idx]) {
            if (var4 != 0.0F) {
               Helpers.subdivideAt((var4 - var3) / (1.0F - var3), this.curCurvepts, var2, this.curCurvepts, 0, this.curCurvepts, var1, var1);
               var3 = var4;
               this.goTo(this.curCurvepts, 2, var1);
               var2 = var1;
            }

            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
            this.phase = 0.0F;
         }

         this.goTo(this.curCurvepts, var2 + 2, var1);
         this.phase += this.li.lastSegLen();
         if (this.phase >= this.dash[this.idx]) {
            this.phase = 0.0F;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
         }

      }
   }

   private static boolean pointCurve(float[] var0, int var1) {
      for(int var2 = 2; var2 < var1; ++var2) {
         if (var0[var2] != var0[var2 - 2]) {
            return false;
         }
      }

      return true;
   }

   public void curveTo(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.curCurvepts[0] = this.x0;
      this.curCurvepts[1] = this.y0;
      this.curCurvepts[2] = var1;
      this.curCurvepts[3] = var2;
      this.curCurvepts[4] = var3;
      this.curCurvepts[5] = var4;
      this.curCurvepts[6] = var5;
      this.curCurvepts[7] = var6;
      this.somethingTo(8);
   }

   public void quadTo(float var1, float var2, float var3, float var4) {
      this.curCurvepts[0] = this.x0;
      this.curCurvepts[1] = this.y0;
      this.curCurvepts[2] = var1;
      this.curCurvepts[3] = var2;
      this.curCurvepts[4] = var3;
      this.curCurvepts[5] = var4;
      this.somethingTo(6);
   }

   public void closePath() {
      this.lineTo(this.sx, this.sy);
      if (this.firstSegidx > 0) {
         if (!this.dashOn || this.needsMoveTo) {
            this.out.moveTo(this.sx, this.sy);
         }

         this.emitFirstSegments();
      }

      this.moveTo(this.sx, this.sy);
   }

   public void pathDone() {
      if (this.firstSegidx > 0) {
         this.out.moveTo(this.sx, this.sy);
         this.emitFirstSegments();
      }

      this.out.pathDone();
   }

   private static class LengthIterator {
      private float[][] recCurveStack;
      private Side[] sides;
      private int curveType;
      private final int limit;
      private final float ERR;
      private final float minTincrement;
      private float nextT;
      private float lenAtNextT;
      private float lastT;
      private float lenAtLastT;
      private float lenAtLastSplit;
      private float lastSegLen;
      private int recLevel;
      private boolean done;
      private float[] curLeafCtrlPolyLengths = new float[3];
      private int cachedHaveLowAcceleration = -1;
      private float[] nextRoots = new float[4];
      private float[] flatLeafCoefCache = new float[]{0.0F, 0.0F, -1.0F, 0.0F};

      public LengthIterator(int var1, float var2) {
         this.limit = var1;
         this.minTincrement = 1.0F / (float)(1 << this.limit);
         this.ERR = var2;
         this.recCurveStack = new float[var1 + 1][8];
         this.sides = new Side[var1];
         this.nextT = Float.MAX_VALUE;
         this.lenAtNextT = Float.MAX_VALUE;
         this.lenAtLastSplit = Float.MIN_VALUE;
         this.recLevel = Integer.MIN_VALUE;
         this.lastSegLen = Float.MAX_VALUE;
         this.done = true;
      }

      public void initializeIterationOnCurve(float[] var1, int var2) {
         System.arraycopy(var1, 0, this.recCurveStack[0], 0, var2);
         this.curveType = var2;
         this.recLevel = 0;
         this.lastT = 0.0F;
         this.lenAtLastT = 0.0F;
         this.nextT = 0.0F;
         this.lenAtNextT = 0.0F;
         this.goLeft();
         this.lenAtLastSplit = 0.0F;
         if (this.recLevel > 0) {
            this.sides[0] = Dasher.LengthIterator.Side.LEFT;
            this.done = false;
         } else {
            this.sides[0] = Dasher.LengthIterator.Side.RIGHT;
            this.done = true;
         }

         this.lastSegLen = 0.0F;
      }

      private boolean haveLowAcceleration(float var1) {
         if (this.cachedHaveLowAcceleration == -1) {
            float var2 = this.curLeafCtrlPolyLengths[0];
            float var3 = this.curLeafCtrlPolyLengths[1];
            if (!Helpers.within(var2, var3, var1 * var3)) {
               this.cachedHaveLowAcceleration = 0;
               return false;
            } else {
               if (this.curveType == 8) {
                  float var4 = this.curLeafCtrlPolyLengths[2];
                  if (!Helpers.within(var3, var4, var1 * var4) || !Helpers.within(var2, var4, var1 * var4)) {
                     this.cachedHaveLowAcceleration = 0;
                     return false;
                  }
               }

               this.cachedHaveLowAcceleration = 1;
               return true;
            }
         } else {
            return this.cachedHaveLowAcceleration == 1;
         }
      }

      public float next(float var1) {
         float var2 = this.lenAtLastSplit + var1;

         while(this.lenAtNextT < var2) {
            if (this.done) {
               this.lastSegLen = this.lenAtNextT - this.lenAtLastSplit;
               return 1.0F;
            }

            this.goToNextLeaf();
         }

         this.lenAtLastSplit = var2;
         float var3 = this.lenAtNextT - this.lenAtLastT;
         float var4 = (var2 - this.lenAtLastT) / var3;
         if (!this.haveLowAcceleration(0.05F)) {
            float var5;
            float var6;
            float var7;
            if (this.flatLeafCoefCache[2] < 0.0F) {
               var5 = 0.0F + this.curLeafCtrlPolyLengths[0];
               var6 = var5 + this.curLeafCtrlPolyLengths[1];
               if (this.curveType == 8) {
                  var7 = var6 + this.curLeafCtrlPolyLengths[2];
                  this.flatLeafCoefCache[0] = 3.0F * (var5 - var6) + var7;
                  this.flatLeafCoefCache[1] = 3.0F * (var6 - 2.0F * var5);
                  this.flatLeafCoefCache[2] = 3.0F * var5;
                  this.flatLeafCoefCache[3] = -var7;
               } else if (this.curveType == 6) {
                  this.flatLeafCoefCache[0] = 0.0F;
                  this.flatLeafCoefCache[1] = var6 - 2.0F * var5;
                  this.flatLeafCoefCache[2] = 2.0F * var5;
                  this.flatLeafCoefCache[3] = -var6;
               }
            }

            var5 = this.flatLeafCoefCache[0];
            var6 = this.flatLeafCoefCache[1];
            var7 = this.flatLeafCoefCache[2];
            float var8 = var4 * this.flatLeafCoefCache[3];
            int var9 = Helpers.cubicRootsInAB(var5, var6, var7, var8, this.nextRoots, 0, 0.0F, 1.0F);
            if (var9 == 1 && !Float.isNaN(this.nextRoots[0])) {
               var4 = this.nextRoots[0];
            }
         }

         var4 = var4 * (this.nextT - this.lastT) + this.lastT;
         if (var4 >= 1.0F) {
            var4 = 1.0F;
            this.done = true;
         }

         this.lastSegLen = var1;
         return var4;
      }

      public float lastSegLen() {
         return this.lastSegLen;
      }

      private void goToNextLeaf() {
         --this.recLevel;

         while(this.sides[this.recLevel] == Dasher.LengthIterator.Side.RIGHT) {
            if (this.recLevel == 0) {
               this.done = true;
               return;
            }

            --this.recLevel;
         }

         this.sides[this.recLevel] = Dasher.LengthIterator.Side.RIGHT;
         System.arraycopy(this.recCurveStack[this.recLevel], 0, this.recCurveStack[this.recLevel + 1], 0, this.curveType);
         ++this.recLevel;
         this.goLeft();
      }

      private void goLeft() {
         float var1 = this.onLeaf();
         if (var1 >= 0.0F) {
            this.lastT = this.nextT;
            this.lenAtLastT = this.lenAtNextT;
            this.nextT += (float)(1 << this.limit - this.recLevel) * this.minTincrement;
            this.lenAtNextT += var1;
            this.flatLeafCoefCache[2] = -1.0F;
            this.cachedHaveLowAcceleration = -1;
         } else {
            Helpers.subdivide(this.recCurveStack[this.recLevel], 0, this.recCurveStack[this.recLevel + 1], 0, this.recCurveStack[this.recLevel], 0, this.curveType);
            this.sides[this.recLevel] = Dasher.LengthIterator.Side.LEFT;
            ++this.recLevel;
            this.goLeft();
         }

      }

      private float onLeaf() {
         float[] var1 = this.recCurveStack[this.recLevel];
         float var2 = 0.0F;
         float var3 = var1[0];
         float var4 = var1[1];

         for(int var5 = 2; var5 < this.curveType; var5 += 2) {
            float var6 = var1[var5];
            float var7 = var1[var5 + 1];
            float var8 = Helpers.linelen(var3, var4, var6, var7);
            var2 += var8;
            this.curLeafCtrlPolyLengths[var5 / 2 - 1] = var8;
            var3 = var6;
            var4 = var7;
         }

         float var9 = Helpers.linelen(var1[0], var1[1], var1[this.curveType - 2], var1[this.curveType - 1]);
         return !(var2 - var9 < this.ERR) && this.recLevel != this.limit ? -1.0F : (var2 + var9) / 2.0F;
      }

      private static enum Side {
         LEFT,
         RIGHT;
      }
   }
}
