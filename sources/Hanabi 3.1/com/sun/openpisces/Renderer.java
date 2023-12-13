package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import java.util.Arrays;

public final class Renderer implements PathConsumer2D {
   private static final int YMAX = 0;
   private static final int CURX = 1;
   private static final int OR = 2;
   private static final int SLOPE = 3;
   private static final int NEXT = 4;
   private static final int SIZEOF_EDGE = 5;
   private static final int MAX_EDGE_IDX = 16777216;
   private int sampleRowMin;
   private int sampleRowMax;
   private float edgeMinX;
   private float edgeMaxX;
   private float[] edges;
   private int[] edgeBuckets;
   private int numEdges;
   private static final float DEC_BND = 1.0F;
   private static final float INC_BND = 0.4F;
   public static final int WIND_EVEN_ODD = 0;
   public static final int WIND_NON_ZERO = 1;
   private final int SUBPIXEL_LG_POSITIONS_X;
   private final int SUBPIXEL_LG_POSITIONS_Y;
   private final int SUBPIXEL_POSITIONS_X;
   private final int SUBPIXEL_POSITIONS_Y;
   private final int SUBPIXEL_MASK_X;
   private final int SUBPIXEL_MASK_Y;
   final int MAX_AA_ALPHA;
   private int boundsMinX;
   private int boundsMinY;
   private int boundsMaxX;
   private int boundsMaxY;
   private int windingRule;
   private float x0;
   private float y0;
   private float pix_sx0;
   private float pix_sy0;
   private Curve c;
   private int[] savedAlpha;
   private ScanlineIterator savedIterator;

   private void addEdgeToBucket(int var1, int var2) {
      if (this.edgeBuckets[var2 * 2] >= 16777216) {
         throw new ArrayIndexOutOfBoundsException(this.edgeBuckets[var2 * 2]);
      } else {
         this.edges[var1 + 4] = (float)this.edgeBuckets[var2 * 2];
         this.edgeBuckets[var2 * 2] = var1 + 1;
         int[] var10000 = this.edgeBuckets;
         var10000[var2 * 2 + 1] += 2;
      }
   }

   private void quadBreakIntoLinesAndAdd(float var1, float var2, Curve var3, float var4, float var5) {
      int var8 = 16;
      int var9 = var8 * var8;

      for(float var10 = Math.max(var3.dbx / (float)var9, var3.dby / (float)var9); var10 > 32.0F; var8 <<= 1) {
         var10 /= 4.0F;
      }

      var9 = var8 * var8;
      float var11 = var3.dbx / (float)var9;
      float var12 = var3.dby / (float)var9;
      float var13 = var3.bx / (float)var9 + var3.cx / (float)var8;

      float var16;
      for(float var14 = var3.by / (float)var9 + var3.cy / (float)var8; var8-- > 1; var2 = var16) {
         float var15 = var1 + var13;
         var13 += var11;
         var16 = var2 + var14;
         var14 += var12;
         this.addLine(var1, var2, var15, var16);
         var1 = var15;
      }

      this.addLine(var1, var2, var4, var5);
   }

   private void curveBreakIntoLinesAndAdd(float var1, float var2, Curve var3, float var4, float var5) {
      int var7 = 8;
      float var8 = 2.0F * var3.dax / 512.0F;
      float var9 = 2.0F * var3.day / 512.0F;
      float var10 = var8 + var3.dbx / 64.0F;
      float var11 = var9 + var3.dby / 64.0F;
      float var12 = var3.ax / 512.0F + var3.bx / 64.0F + var3.cx / 8.0F;
      float var13 = var3.ay / 512.0F + var3.by / 64.0F + var3.cy / 8.0F;
      float var14 = var1;

      for(float var15 = var2; var7 > 0; var2 = var15) {
         while(Math.abs(var10) > 1.0F || Math.abs(var11) > 1.0F) {
            var8 /= 8.0F;
            var9 /= 8.0F;
            var10 = var10 / 4.0F - var8;
            var11 = var11 / 4.0F - var9;
            var12 = (var12 - var10) / 2.0F;
            var13 = (var13 - var11) / 2.0F;
            var7 <<= 1;
         }

         while(var7 % 2 == 0 && Math.abs(var12) <= 0.4F && Math.abs(var13) <= 0.4F) {
            var12 = 2.0F * var12 + var10;
            var13 = 2.0F * var13 + var11;
            var10 = 4.0F * (var10 + var8);
            var11 = 4.0F * (var11 + var9);
            var8 = 8.0F * var8;
            var9 = 8.0F * var9;
            var7 >>= 1;
         }

         --var7;
         if (var7 > 0) {
            var14 += var12;
            var12 += var10;
            var10 += var8;
            var15 += var13;
            var13 += var11;
            var11 += var9;
         } else {
            var14 = var4;
            var15 = var5;
         }

         this.addLine(var1, var2, var14, var15);
         var1 = var14;
      }

   }

   private void addLine(float var1, float var2, float var3, float var4) {
      float var5 = 1.0F;
      if (var4 < var2) {
         var5 = var4;
         var4 = var2;
         var2 = var5;
         var5 = var3;
         var3 = var1;
         var1 = var5;
         var5 = 0.0F;
      }

      int var6 = Math.max((int)Math.ceil((double)(var2 - 0.5F)), this.boundsMinY);
      int var7 = Math.min((int)Math.ceil((double)(var4 - 0.5F)), this.boundsMaxY);
      if (var6 < var7) {
         if (var6 < this.sampleRowMin) {
            this.sampleRowMin = var6;
         }

         if (var7 > this.sampleRowMax) {
            this.sampleRowMax = var7;
         }

         float var8 = (var3 - var1) / (var4 - var2);
         if (var8 > 0.0F) {
            if (var1 < this.edgeMinX) {
               this.edgeMinX = var1;
            }

            if (var3 > this.edgeMaxX) {
               this.edgeMaxX = var3;
            }
         } else {
            if (var3 < this.edgeMinX) {
               this.edgeMinX = var3;
            }

            if (var1 > this.edgeMaxX) {
               this.edgeMaxX = var1;
            }
         }

         int var9 = var6 - this.boundsMinY;
         int var10 = this.edgeBuckets[var9 * 2];
         if (var10 >= 16777216) {
            throw new ArrayIndexOutOfBoundsException(var10);
         } else {
            int var11 = this.numEdges * 5;
            this.edges = Helpers.widenArray((float[])this.edges, var11, 5);
            ++this.numEdges;
            this.edges[var11 + 2] = var5;
            this.edges[var11 + 1] = var1 + ((float)var6 + 0.5F - var2) * var8;
            this.edges[var11 + 3] = var8;
            this.edges[var11 + 0] = (float)var7;
            this.addEdgeToBucket(var11, var9);
            int[] var10000 = this.edgeBuckets;
            int var10001 = (var7 - this.boundsMinY) * 2 + 1;
            var10000[var10001] |= 1;
         }
      }
   }

   public Renderer(int var1, int var2) {
      this.c = new Curve();
      this.SUBPIXEL_LG_POSITIONS_X = var1;
      this.SUBPIXEL_LG_POSITIONS_Y = var2;
      this.SUBPIXEL_POSITIONS_X = 1 << this.SUBPIXEL_LG_POSITIONS_X;
      this.SUBPIXEL_POSITIONS_Y = 1 << this.SUBPIXEL_LG_POSITIONS_Y;
      this.SUBPIXEL_MASK_X = this.SUBPIXEL_POSITIONS_X - 1;
      this.SUBPIXEL_MASK_Y = this.SUBPIXEL_POSITIONS_Y - 1;
      this.MAX_AA_ALPHA = this.SUBPIXEL_POSITIONS_X * this.SUBPIXEL_POSITIONS_Y;
   }

   public Renderer(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this(var1, var2);
      this.reset(var3, var4, var5, var6, var7);
   }

   public void reset(int var1, int var2, int var3, int var4, int var5) {
      this.windingRule = var5;
      this.boundsMinX = var1 * this.SUBPIXEL_POSITIONS_X;
      this.boundsMinY = var2 * this.SUBPIXEL_POSITIONS_Y;
      this.boundsMaxX = (var1 + var3) * this.SUBPIXEL_POSITIONS_X;
      this.boundsMaxY = (var2 + var4) * this.SUBPIXEL_POSITIONS_Y;
      this.edgeMinX = Float.POSITIVE_INFINITY;
      this.edgeMaxX = Float.NEGATIVE_INFINITY;
      this.sampleRowMax = this.boundsMinY;
      this.sampleRowMin = this.boundsMaxY;
      int var6 = this.boundsMaxY - this.boundsMinY;
      if (this.edgeBuckets != null && this.edgeBuckets.length >= var6 * 2 + 2) {
         Arrays.fill(this.edgeBuckets, 0, var6 * 2, 0);
      } else {
         this.edgeBuckets = new int[var6 * 2 + 2];
      }

      if (this.edges == null) {
         this.edges = new float[160];
      }

      this.numEdges = 0;
      this.pix_sx0 = this.pix_sy0 = this.x0 = this.y0 = 0.0F;
   }

   private float tosubpixx(float var1) {
      return var1 * (float)this.SUBPIXEL_POSITIONS_X;
   }

   private float tosubpixy(float var1) {
      return var1 * (float)this.SUBPIXEL_POSITIONS_Y;
   }

   public void moveTo(float var1, float var2) {
      this.closePath();
      this.pix_sx0 = var1;
      this.pix_sy0 = var2;
      this.y0 = this.tosubpixy(var2);
      this.x0 = this.tosubpixx(var1);
   }

   public void lineTo(float var1, float var2) {
      float var3 = this.tosubpixx(var1);
      float var4 = this.tosubpixy(var2);
      this.addLine(this.x0, this.y0, var3, var4);
      this.x0 = var3;
      this.y0 = var4;
   }

   public void curveTo(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = this.tosubpixx(var5);
      float var8 = this.tosubpixy(var6);
      this.c.set(this.x0, this.y0, this.tosubpixx(var1), this.tosubpixy(var2), this.tosubpixx(var3), this.tosubpixy(var4), var7, var8);
      this.curveBreakIntoLinesAndAdd(this.x0, this.y0, this.c, var7, var8);
      this.x0 = var7;
      this.y0 = var8;
   }

   public void quadTo(float var1, float var2, float var3, float var4) {
      float var5 = this.tosubpixx(var3);
      float var6 = this.tosubpixy(var4);
      this.c.set(this.x0, this.y0, this.tosubpixx(var1), this.tosubpixy(var2), var5, var6);
      this.quadBreakIntoLinesAndAdd(this.x0, this.y0, this.c, var5, var6);
      this.x0 = var5;
      this.y0 = var6;
   }

   public void closePath() {
      this.lineTo(this.pix_sx0, this.pix_sy0);
   }

   public void pathDone() {
      this.closePath();
   }

   public void produceAlphas(AlphaConsumer var1) {
      var1.setMaxAlpha(this.MAX_AA_ALPHA);
      int var2 = this.windingRule == 0 ? 1 : -1;
      int var3 = var1.getWidth();
      int[] var4 = this.savedAlpha;
      if (var4 != null && var4.length >= var3 + 2) {
         Arrays.fill(var4, 0, var3 + 2, 0);
      } else {
         this.savedAlpha = var4 = new int[var3 + 2];
      }

      int var5 = var1.getOriginX() << this.SUBPIXEL_LG_POSITIONS_X;
      int var6 = var5 + (var3 << this.SUBPIXEL_LG_POSITIONS_X);
      int var7 = var6 >> this.SUBPIXEL_LG_POSITIONS_X;
      int var8 = var5 >> this.SUBPIXEL_LG_POSITIONS_Y;
      int var9 = this.boundsMinY;
      ScanlineIterator var10 = this.savedIterator;
      if (var10 == null) {
         this.savedIterator = var10 = new ScanlineIterator();
      } else {
         var10.reset();
      }

      while(var10.hasNext()) {
         int var11 = var10.next();
         int[] var12 = var10.crossings;
         var9 = var10.curY();
         int var13;
         int var14;
         int var15;
         int var16;
         if (var11 > 0) {
            var13 = var12[0] >> 1;
            var14 = var12[var11 - 1] >> 1;
            var15 = Math.max(var13, var5);
            var16 = Math.min(var14, var6);
            var8 = Math.min(var8, var15 >> this.SUBPIXEL_LG_POSITIONS_X);
            var7 = Math.max(var7, var16 >> this.SUBPIXEL_LG_POSITIONS_X);
         }

         var13 = 0;
         var14 = var5;

         for(var15 = 0; var15 < var11; ++var15) {
            var16 = var12[var15];
            int var17 = var16 >> 1;
            int var18 = ((var16 & 1) << 1) - 1;
            if ((var13 & var2) != 0) {
               int var19 = Math.max(var14, var5);
               int var20 = Math.min(var17, var6);
               if (var19 < var20) {
                  var19 -= var5;
                  var20 -= var5;
                  int var21 = var19 >> this.SUBPIXEL_LG_POSITIONS_X;
                  int var22 = var20 - 1 >> this.SUBPIXEL_LG_POSITIONS_X;
                  if (var21 == var22) {
                     var4[var21] += var20 - var19;
                     var4[var21 + 1] -= var20 - var19;
                  } else {
                     int var23 = var20 >> this.SUBPIXEL_LG_POSITIONS_X;
                     var4[var21] += this.SUBPIXEL_POSITIONS_X - (var19 & this.SUBPIXEL_MASK_X);
                     var4[var21 + 1] += var19 & this.SUBPIXEL_MASK_X;
                     var4[var23] -= this.SUBPIXEL_POSITIONS_X - (var20 & this.SUBPIXEL_MASK_X);
                     var4[var23 + 1] -= var20 & this.SUBPIXEL_MASK_X;
                  }
               }
            }

            var13 += var18;
            var14 = var17;
         }

         if ((var9 & this.SUBPIXEL_MASK_Y) == this.SUBPIXEL_MASK_Y) {
            var1.setAndClearRelativeAlphas(var4, var9 >> this.SUBPIXEL_LG_POSITIONS_Y, var8, var7);
            var7 = var6 >> this.SUBPIXEL_LG_POSITIONS_X;
            var8 = var5 >> this.SUBPIXEL_LG_POSITIONS_Y;
         }
      }

      if ((var9 & this.SUBPIXEL_MASK_Y) < this.SUBPIXEL_MASK_Y) {
         var1.setAndClearRelativeAlphas(var4, var9 >> this.SUBPIXEL_LG_POSITIONS_Y, var8, var7);
      }

   }

   public int getSubpixMinX() {
      int var1 = (int)Math.ceil((double)(this.edgeMinX - 0.5F));
      if (var1 < this.boundsMinX) {
         var1 = this.boundsMinX;
      }

      return var1;
   }

   public int getSubpixMaxX() {
      int var1 = (int)Math.ceil((double)(this.edgeMaxX - 0.5F));
      if (var1 > this.boundsMaxX) {
         var1 = this.boundsMaxX;
      }

      return var1;
   }

   public int getSubpixMinY() {
      return this.sampleRowMin;
   }

   public int getSubpixMaxY() {
      return this.sampleRowMax;
   }

   public int getOutpixMinX() {
      return this.getSubpixMinX() >> this.SUBPIXEL_LG_POSITIONS_X;
   }

   public int getOutpixMaxX() {
      return this.getSubpixMaxX() + this.SUBPIXEL_MASK_X >> this.SUBPIXEL_LG_POSITIONS_X;
   }

   public int getOutpixMinY() {
      return this.sampleRowMin >> this.SUBPIXEL_LG_POSITIONS_Y;
   }

   public int getOutpixMaxY() {
      return this.sampleRowMax + this.SUBPIXEL_MASK_Y >> this.SUBPIXEL_LG_POSITIONS_Y;
   }

   private final class ScanlineIterator {
      private int[] crossings;
      private int[] edgePtrs;
      private int edgeCount;
      private int nextY;
      private static final int INIT_CROSSINGS_SIZE = 10;

      private ScanlineIterator() {
         this.crossings = new int[10];
         this.edgePtrs = new int[10];
         this.reset();
      }

      public void reset() {
         this.nextY = Renderer.this.sampleRowMin;
         this.edgeCount = 0;
      }

      private int next() {
         int var1 = this.nextY++;
         int var2 = var1 - Renderer.this.boundsMinY;
         int var3 = this.edgeCount;
         int[] var4 = this.edgePtrs;
         float[] var5 = Renderer.this.edges;
         int var6 = Renderer.this.edgeBuckets[var2 * 2 + 1];
         int var7;
         int var8;
         int var9;
         if ((var6 & 1) != 0) {
            var7 = 0;

            for(var8 = 0; var8 < var3; ++var8) {
               var9 = var4[var8];
               if (var5[var9 + 0] > (float)var1) {
                  var4[var7++] = var9;
               }
            }

            var3 = var7;
         }

         var4 = Helpers.widenArray(var4, var3, var6 >> 1);

         for(var7 = Renderer.this.edgeBuckets[var2 * 2]; var7 != 0; var7 = (int)var5[var7 + 4]) {
            int var10001 = var3++;
            --var7;
            var4[var10001] = var7;
         }

         this.edgePtrs = var4;
         this.edgeCount = var3;
         int[] var14 = this.crossings;
         if (var14.length < var3) {
            this.crossings = var14 = new int[var4.length];
         }

         for(var8 = 0; var8 < var3; ++var8) {
            var9 = var4[var8];
            float var10 = var5[var9 + 1];
            int var11 = (int)Math.ceil((double)(var10 - 0.5F)) << 1;
            var5[var9 + 1] = var10 + var5[var9 + 3];
            if (var5[var9 + 2] > 0.0F) {
               var11 |= 1;
            }

            int var12 = var8;

            while(true) {
               --var12;
               if (var12 < 0) {
                  break;
               }

               int var13 = var14[var12];
               if (var13 <= var11) {
                  break;
               }

               var14[var12 + 1] = var13;
               var4[var12 + 1] = var4[var12];
            }

            var14[var12 + 1] = var11;
            var4[var12 + 1] = var9;
         }

         return var3;
      }

      private boolean hasNext() {
         return this.nextY < Renderer.this.sampleRowMax;
      }

      private int curY() {
         return this.nextY - 1;
      }

      // $FF: synthetic method
      ScanlineIterator(Object var2) {
         this();
      }
   }
}
