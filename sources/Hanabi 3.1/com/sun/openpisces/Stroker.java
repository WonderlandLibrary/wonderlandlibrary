package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import java.util.Arrays;

public final class Stroker implements PathConsumer2D {
   private static final int MOVE_TO = 0;
   private static final int DRAWING_OP_TO = 1;
   private static final int CLOSE = 2;
   public static final int JOIN_MITER = 0;
   public static final int JOIN_ROUND = 1;
   public static final int JOIN_BEVEL = 2;
   public static final int CAP_BUTT = 0;
   public static final int CAP_ROUND = 1;
   public static final int CAP_SQUARE = 2;
   private PathConsumer2D out;
   private int capStyle;
   private int joinStyle;
   private float lineWidth2;
   private final float[][] offset;
   private final float[] miter;
   private float miterLimitSq;
   private int prev;
   private float sx0;
   private float sy0;
   private float sdx;
   private float sdy;
   private float cx0;
   private float cy0;
   private float cdx;
   private float cdy;
   private float smx;
   private float smy;
   private float cmx;
   private float cmy;
   private final PolyStack reverse;
   private static final float ROUND_JOIN_THRESHOLD = 0.015258789F;
   private float[] middle;
   private float[] lp;
   private float[] rp;
   private static final int MAX_N_CURVES = 11;
   private float[] subdivTs;
   private static Curve c = new Curve();

   public Stroker(PathConsumer2D var1, float var2, int var3, int var4, float var5) {
      this(var1);
      this.reset(var2, var3, var4, var5);
   }

   public Stroker(PathConsumer2D var1) {
      this.offset = new float[3][2];
      this.miter = new float[2];
      this.reverse = new PolyStack();
      this.middle = new float[88];
      this.lp = new float[8];
      this.rp = new float[8];
      this.subdivTs = new float[10];
      this.setConsumer(var1);
   }

   public void setConsumer(PathConsumer2D var1) {
      this.out = var1;
   }

   public void reset(float var1, int var2, int var3, float var4) {
      this.lineWidth2 = var1 / 2.0F;
      this.capStyle = var2;
      this.joinStyle = var3;
      float var5 = var4 * this.lineWidth2;
      this.miterLimitSq = var5 * var5;
      this.prev = 2;
   }

   private static void computeOffset(float var0, float var1, float var2, float[] var3) {
      float var4 = (float)Math.sqrt((double)(var0 * var0 + var1 * var1));
      if (var4 == 0.0F) {
         var3[0] = var3[1] = 0.0F;
      } else {
         var3[0] = var1 * var2 / var4;
         var3[1] = -(var0 * var2) / var4;
      }

   }

   private static boolean isCW(float var0, float var1, float var2, float var3) {
      return var0 * var3 <= var1 * var2;
   }

   private void drawRoundJoin(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, float var8) {
      if ((var3 != 0.0F || var4 != 0.0F) && (var5 != 0.0F || var6 != 0.0F)) {
         float var9 = var3 - var5;
         float var10 = var4 - var6;
         float var11 = var9 * var9 + var10 * var10;
         if (!(var11 < var8)) {
            if (var7) {
               var3 = -var3;
               var4 = -var4;
               var5 = -var5;
               var6 = -var6;
            }

            this.drawRoundJoin(var1, var2, var3, var4, var5, var6, var7);
         }
      }
   }

   private void drawRoundJoin(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      double var8 = (double)(var3 * var5 + var4 * var6);
      int var10 = var8 >= 0.0 ? 1 : 2;
      switch (var10) {
         case 1:
            this.drawBezApproxForArc(var1, var2, var3, var4, var5, var6, var7);
            break;
         case 2:
            float var11 = var6 - var4;
            float var12 = var3 - var5;
            float var13 = (float)Math.sqrt((double)(var11 * var11 + var12 * var12));
            float var14 = this.lineWidth2 / var13;
            float var15 = var11 * var14;
            float var16 = var12 * var14;
            if (var7) {
               var15 = -var15;
               var16 = -var16;
            }

            this.drawBezApproxForArc(var1, var2, var3, var4, var15, var16, var7);
            this.drawBezApproxForArc(var1, var2, var15, var16, var5, var6, var7);
      }

   }

   private void drawBezApproxForArc(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      float var8 = (var3 * var5 + var4 * var6) / (2.0F * this.lineWidth2 * this.lineWidth2);
      float var9 = (float)(1.3333333333333333 * Math.sqrt(0.5 - (double)var8) / (1.0 + Math.sqrt((double)var8 + 0.5)));
      if (var7) {
         var9 = -var9;
      }

      float var10 = var1 + var3;
      float var11 = var2 + var4;
      float var12 = var10 - var9 * var4;
      float var13 = var11 + var9 * var3;
      float var14 = var1 + var5;
      float var15 = var2 + var6;
      float var16 = var14 + var9 * var6;
      float var17 = var15 - var9 * var5;
      this.emitCurveTo(var10, var11, var12, var13, var16, var17, var14, var15, var7);
   }

   private void drawRoundCap(float var1, float var2, float var3, float var4) {
      this.emitCurveTo(var1 + var3, var2 + var4, var1 + var3 - 0.5522848F * var4, var2 + var4 + 0.5522848F * var3, var1 - var4 + 0.5522848F * var3, var2 + var3 + 0.5522848F * var4, var1 - var4, var2 + var3, false);
      this.emitCurveTo(var1 - var4, var2 + var3, var1 - var4 - 0.5522848F * var3, var2 + var3 - 0.5522848F * var4, var1 - var3 - 0.5522848F * var4, var2 - var4 + 0.5522848F * var3, var1 - var3, var2 - var4, false);
   }

   private void computeMiter(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float[] var9, int var10) {
      float var11 = var3 - var1;
      float var12 = var4 - var2;
      float var13 = var7 - var5;
      float var14 = var8 - var6;
      float var15 = var11 * var14 - var13 * var12;
      float var16 = var13 * (var2 - var6) - var14 * (var1 - var5);
      var16 /= var15;
      var9[var10++] = var1 + var16 * var11;
      var9[var10] = var2 + var16 * var12;
   }

   private void safecomputeMiter(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float[] var9, int var10) {
      float var11 = var3 - var1;
      float var12 = var4 - var2;
      float var13 = var7 - var5;
      float var14 = var8 - var6;
      float var15 = var11 * var14 - var13 * var12;
      if (var15 == 0.0F) {
         var9[var10++] = (var1 + var5) / 2.0F;
         var9[var10] = (var2 + var6) / 2.0F;
      } else {
         float var16 = var13 * (var2 - var6) - var14 * (var1 - var5);
         var16 /= var15;
         var9[var10++] = var1 + var16 * var11;
         var9[var10] = var2 + var16 * var12;
      }
   }

   private void drawMiter(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, boolean var11) {
      if ((var9 != var7 || var10 != var8) && (var1 != 0.0F || var2 != 0.0F) && (var5 != 0.0F || var6 != 0.0F)) {
         if (var11) {
            var7 = -var7;
            var8 = -var8;
            var9 = -var9;
            var10 = -var10;
         }

         this.computeMiter(var3 - var1 + var7, var4 - var2 + var8, var3 + var7, var4 + var8, var5 + var3 + var9, var6 + var4 + var10, var3 + var9, var4 + var10, this.miter, 0);
         float var12 = (this.miter[0] - var3) * (this.miter[0] - var3) + (this.miter[1] - var4) * (this.miter[1] - var4);
         if (var12 < this.miterLimitSq) {
            this.emitLineTo(this.miter[0], this.miter[1], var11);
         }

      }
   }

   public void moveTo(float var1, float var2) {
      if (this.prev == 1) {
         this.finish();
      }

      this.sx0 = this.cx0 = var1;
      this.sy0 = this.cy0 = var2;
      this.cdx = this.sdx = 1.0F;
      this.cdy = this.sdy = 0.0F;
      this.prev = 0;
   }

   public void lineTo(float var1, float var2) {
      float var3 = var1 - this.cx0;
      float var4 = var2 - this.cy0;
      if (var3 == 0.0F && var4 == 0.0F) {
         var3 = 1.0F;
      }

      computeOffset(var3, var4, this.lineWidth2, this.offset[0]);
      float var5 = this.offset[0][0];
      float var6 = this.offset[0][1];
      this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, var3, var4, this.cmx, this.cmy, var5, var6);
      this.emitLineTo(this.cx0 + var5, this.cy0 + var6);
      this.emitLineTo(var1 + var5, var2 + var6);
      this.emitLineTo(this.cx0 - var5, this.cy0 - var6, true);
      this.emitLineTo(var1 - var5, var2 - var6, true);
      this.cmx = var5;
      this.cmy = var6;
      this.cdx = var3;
      this.cdy = var4;
      this.cx0 = var1;
      this.cy0 = var2;
      this.prev = 1;
   }

   public void closePath() {
      if (this.prev != 1) {
         if (this.prev != 2) {
            this.emitMoveTo(this.cx0, this.cy0 - this.lineWidth2);
            this.cmx = this.smx = 0.0F;
            this.cmy = this.smy = -this.lineWidth2;
            this.cdx = this.sdx = 1.0F;
            this.cdy = this.sdy = 0.0F;
            this.finish();
         }
      } else {
         if (this.cx0 != this.sx0 || this.cy0 != this.sy0) {
            this.lineTo(this.sx0, this.sy0);
         }

         this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, this.sdx, this.sdy, this.cmx, this.cmy, this.smx, this.smy);
         this.emitLineTo(this.sx0 + this.smx, this.sy0 + this.smy);
         this.emitMoveTo(this.sx0 - this.smx, this.sy0 - this.smy);
         this.emitReverse();
         this.prev = 2;
         this.emitClose();
      }
   }

   private void emitReverse() {
      while(!this.reverse.isEmpty()) {
         this.reverse.pop(this.out);
      }

   }

   public void pathDone() {
      if (this.prev == 1) {
         this.finish();
      }

      this.out.pathDone();
      this.prev = 2;
   }

   private void finish() {
      if (this.capStyle == 1) {
         this.drawRoundCap(this.cx0, this.cy0, this.cmx, this.cmy);
      } else if (this.capStyle == 2) {
         this.emitLineTo(this.cx0 - this.cmy + this.cmx, this.cy0 + this.cmx + this.cmy);
         this.emitLineTo(this.cx0 - this.cmy - this.cmx, this.cy0 + this.cmx - this.cmy);
      }

      this.emitReverse();
      if (this.capStyle == 1) {
         this.drawRoundCap(this.sx0, this.sy0, -this.smx, -this.smy);
      } else if (this.capStyle == 2) {
         this.emitLineTo(this.sx0 + this.smy - this.smx, this.sy0 - this.smx - this.smy);
         this.emitLineTo(this.sx0 + this.smy + this.smx, this.sy0 - this.smx + this.smy);
      }

      this.emitClose();
   }

   private void emitMoveTo(float var1, float var2) {
      this.out.moveTo(var1, var2);
   }

   private void emitLineTo(float var1, float var2) {
      this.out.lineTo(var1, var2);
   }

   private void emitLineTo(float var1, float var2, boolean var3) {
      if (var3) {
         this.reverse.pushLine(var1, var2);
      } else {
         this.emitLineTo(var1, var2);
      }

   }

   private void emitQuadTo(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      if (var7) {
         this.reverse.pushQuad(var1, var2, var3, var4);
      } else {
         this.out.quadTo(var3, var4, var5, var6);
      }

   }

   private void emitCurveTo(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, boolean var9) {
      if (var9) {
         this.reverse.pushCubic(var1, var2, var3, var4, var5, var6);
      } else {
         this.out.curveTo(var3, var4, var5, var6, var7, var8);
      }

   }

   private void emitClose() {
      this.out.closePath();
   }

   private void drawJoin(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      if (this.prev != 1) {
         this.emitMoveTo(var3 + var9, var4 + var10);
         this.sdx = var5;
         this.sdy = var6;
         this.smx = var9;
         this.smy = var10;
      } else {
         boolean var11 = isCW(var1, var2, var5, var6);
         if (this.joinStyle == 0) {
            this.drawMiter(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11);
         } else if (this.joinStyle == 1) {
            this.drawRoundJoin(var3, var4, var7, var8, var9, var10, var11, 0.015258789F);
         }

         this.emitLineTo(var3, var4, !var11);
      }

      this.prev = 1;
   }

   private static boolean within(float var0, float var1, float var2, float var3, float var4) {
      assert var4 > 0.0F : "";

      return Helpers.within(var0, var2, var4) && Helpers.within(var1, var3, var4);
   }

   private void getLineOffsets(float var1, float var2, float var3, float var4, float[] var5, float[] var6) {
      computeOffset(var3 - var1, var4 - var2, this.lineWidth2, this.offset[0]);
      var5[0] = var1 + this.offset[0][0];
      var5[1] = var2 + this.offset[0][1];
      var5[2] = var3 + this.offset[0][0];
      var5[3] = var4 + this.offset[0][1];
      var6[0] = var1 - this.offset[0][0];
      var6[1] = var2 - this.offset[0][1];
      var6[2] = var3 - this.offset[0][0];
      var6[3] = var4 - this.offset[0][1];
   }

   private int computeOffsetCubic(float[] var1, int var2, float[] var3, float[] var4) {
      float var5 = var1[var2 + 0];
      float var6 = var1[var2 + 1];
      float var7 = var1[var2 + 2];
      float var8 = var1[var2 + 3];
      float var9 = var1[var2 + 4];
      float var10 = var1[var2 + 5];
      float var11 = var1[var2 + 6];
      float var12 = var1[var2 + 7];
      float var13 = var11 - var9;
      float var14 = var12 - var10;
      float var15 = var7 - var5;
      float var16 = var8 - var6;
      boolean var17 = within(var5, var6, var7, var8, 6.0F * Math.ulp(var8));
      boolean var18 = within(var9, var10, var11, var12, 6.0F * Math.ulp(var12));
      if (var17 && var18) {
         this.getLineOffsets(var5, var6, var11, var12, var3, var4);
         return 4;
      } else {
         if (var17) {
            var15 = var9 - var5;
            var16 = var10 - var6;
         } else if (var18) {
            var13 = var11 - var7;
            var14 = var12 - var8;
         }

         float var19 = var15 * var13 + var16 * var14;
         var19 *= var19;
         float var20 = var15 * var15 + var16 * var16;
         float var21 = var13 * var13 + var14 * var14;
         if (Helpers.within(var19, var20 * var21, 4.0F * Math.ulp(var19))) {
            this.getLineOffsets(var5, var6, var11, var12, var3, var4);
            return 4;
         } else {
            float var22 = 0.125F * (var5 + 3.0F * (var7 + var9) + var11);
            float var23 = 0.125F * (var6 + 3.0F * (var8 + var10) + var12);
            float var24 = var9 + var11 - var5 - var7;
            float var25 = var10 + var12 - var6 - var8;
            computeOffset(var15, var16, this.lineWidth2, this.offset[0]);
            computeOffset(var24, var25, this.lineWidth2, this.offset[1]);
            computeOffset(var13, var14, this.lineWidth2, this.offset[2]);
            float var26 = var5 + this.offset[0][0];
            float var27 = var6 + this.offset[0][1];
            float var28 = var22 + this.offset[1][0];
            float var29 = var23 + this.offset[1][1];
            float var30 = var11 + this.offset[2][0];
            float var31 = var12 + this.offset[2][1];
            float var32 = 4.0F / (3.0F * (var15 * var14 - var16 * var13));
            float var33 = 2.0F * var28 - var26 - var30;
            float var34 = 2.0F * var29 - var27 - var31;
            float var35 = var32 * (var14 * var33 - var13 * var34);
            float var36 = var32 * (var15 * var34 - var16 * var33);
            float var37 = var26 + var35 * var15;
            float var38 = var27 + var35 * var16;
            float var39 = var30 + var36 * var13;
            float var40 = var31 + var36 * var14;
            var3[0] = var26;
            var3[1] = var27;
            var3[2] = var37;
            var3[3] = var38;
            var3[4] = var39;
            var3[5] = var40;
            var3[6] = var30;
            var3[7] = var31;
            var26 = var5 - this.offset[0][0];
            var27 = var6 - this.offset[0][1];
            var28 -= 2.0F * this.offset[1][0];
            var29 -= 2.0F * this.offset[1][1];
            var30 = var11 - this.offset[2][0];
            var31 = var12 - this.offset[2][1];
            var33 = 2.0F * var28 - var26 - var30;
            var34 = 2.0F * var29 - var27 - var31;
            var35 = var32 * (var14 * var33 - var13 * var34);
            var36 = var32 * (var15 * var34 - var16 * var33);
            var37 = var26 + var35 * var15;
            var38 = var27 + var35 * var16;
            var39 = var30 + var36 * var13;
            var40 = var31 + var36 * var14;
            var4[0] = var26;
            var4[1] = var27;
            var4[2] = var37;
            var4[3] = var38;
            var4[4] = var39;
            var4[5] = var40;
            var4[6] = var30;
            var4[7] = var31;
            return 8;
         }
      }
   }

   private int computeOffsetQuad(float[] var1, int var2, float[] var3, float[] var4) {
      float var5 = var1[var2 + 0];
      float var6 = var1[var2 + 1];
      float var7 = var1[var2 + 2];
      float var8 = var1[var2 + 3];
      float var9 = var1[var2 + 4];
      float var10 = var1[var2 + 5];
      float var11 = var9 - var7;
      float var12 = var10 - var8;
      float var13 = var7 - var5;
      float var14 = var8 - var6;
      boolean var15 = within(var5, var6, var7, var8, 6.0F * Math.ulp(var8));
      boolean var16 = within(var7, var8, var9, var10, 6.0F * Math.ulp(var10));
      if (!var15 && !var16) {
         float var17 = var13 * var11 + var14 * var12;
         var17 *= var17;
         float var18 = var13 * var13 + var14 * var14;
         float var19 = var11 * var11 + var12 * var12;
         if (Helpers.within(var17, var18 * var19, 4.0F * Math.ulp(var17))) {
            this.getLineOffsets(var5, var6, var9, var10, var3, var4);
            return 4;
         } else {
            computeOffset(var13, var14, this.lineWidth2, this.offset[0]);
            computeOffset(var11, var12, this.lineWidth2, this.offset[1]);
            float var20 = var5 + this.offset[0][0];
            float var21 = var6 + this.offset[0][1];
            float var22 = var9 + this.offset[1][0];
            float var23 = var10 + this.offset[1][1];
            this.safecomputeMiter(var20, var21, var20 + var13, var21 + var14, var22, var23, var22 - var11, var23 - var12, var3, 2);
            var3[0] = var20;
            var3[1] = var21;
            var3[4] = var22;
            var3[5] = var23;
            var20 = var5 - this.offset[0][0];
            var21 = var6 - this.offset[0][1];
            var22 = var9 - this.offset[1][0];
            var23 = var10 - this.offset[1][1];
            this.safecomputeMiter(var20, var21, var20 + var13, var21 + var14, var22, var23, var22 - var11, var23 - var12, var4, 2);
            var4[0] = var20;
            var4[1] = var21;
            var4[4] = var22;
            var4[5] = var23;
            return 6;
         }
      } else {
         this.getLineOffsets(var5, var6, var9, var10, var3, var4);
         return 4;
      }
   }

   private static int findSubdivPoints(float[] var0, float[] var1, int var2, float var3) {
      float var4 = var0[2] - var0[0];
      float var5 = var0[3] - var0[1];
      if (var5 != 0.0F && var4 != 0.0F) {
         float var6 = (float)Math.sqrt((double)(var4 * var4 + var5 * var5));
         float var7 = var4 / var6;
         float var8 = var5 / var6;
         float var9 = var7 * var0[0] + var8 * var0[1];
         float var10 = var7 * var0[1] - var8 * var0[0];
         float var11 = var7 * var0[2] + var8 * var0[3];
         float var12 = var7 * var0[3] - var8 * var0[2];
         float var13 = var7 * var0[4] + var8 * var0[5];
         float var14 = var7 * var0[5] - var8 * var0[4];
         switch (var2) {
            case 6:
               c.set(var9, var10, var11, var12, var13, var14);
               break;
            case 8:
               float var15 = var7 * var0[6] + var8 * var0[7];
               float var16 = var7 * var0[7] - var8 * var0[6];
               c.set(var9, var10, var11, var12, var13, var14, var15, var16);
         }
      } else {
         c.set(var0, var2);
      }

      int var17 = 0;
      var17 += c.dxRoots(var1, var17);
      var17 += c.dyRoots(var1, var17);
      if (var2 == 8) {
         var17 += c.infPoints(var1, var17);
      }

      var17 += c.rootsOfROCMinusW(var1, var17, var3, 1.0E-4F);
      var17 = Helpers.filterOutNotInAB(var1, 0, var17, 1.0E-4F, 0.9999F);
      Helpers.isort(var1, 0, var17);
      return var17;
   }

   public void curveTo(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.middle[0] = this.cx0;
      this.middle[1] = this.cy0;
      this.middle[2] = var1;
      this.middle[3] = var2;
      this.middle[4] = var3;
      this.middle[5] = var4;
      this.middle[6] = var5;
      this.middle[7] = var6;
      float var7 = this.middle[6];
      float var8 = this.middle[7];
      float var9 = this.middle[2] - this.middle[0];
      float var10 = this.middle[3] - this.middle[1];
      float var11 = this.middle[6] - this.middle[4];
      float var12 = this.middle[7] - this.middle[5];
      boolean var13 = var9 == 0.0F && var10 == 0.0F;
      boolean var14 = var11 == 0.0F && var12 == 0.0F;
      if (var13) {
         var9 = this.middle[4] - this.middle[0];
         var10 = this.middle[5] - this.middle[1];
         if (var9 == 0.0F && var10 == 0.0F) {
            var9 = this.middle[6] - this.middle[0];
            var10 = this.middle[7] - this.middle[1];
         }
      }

      if (var14) {
         var11 = this.middle[6] - this.middle[2];
         var12 = this.middle[7] - this.middle[3];
         if (var11 == 0.0F && var12 == 0.0F) {
            var11 = this.middle[6] - this.middle[0];
            var12 = this.middle[7] - this.middle[1];
         }
      }

      if (var9 == 0.0F && var10 == 0.0F) {
         this.lineTo(this.middle[0], this.middle[1]);
      } else {
         float var15;
         if (Math.abs(var9) < 0.1F && Math.abs(var10) < 0.1F) {
            var15 = (float)Math.sqrt((double)(var9 * var9 + var10 * var10));
            var9 /= var15;
            var10 /= var15;
         }

         if (Math.abs(var11) < 0.1F && Math.abs(var12) < 0.1F) {
            var15 = (float)Math.sqrt((double)(var11 * var11 + var12 * var12));
            var11 /= var15;
            var12 /= var15;
         }

         computeOffset(var9, var10, this.lineWidth2, this.offset[0]);
         var15 = this.offset[0][0];
         float var16 = this.offset[0][1];
         this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, var9, var10, this.cmx, this.cmy, var15, var16);
         int var17 = findSubdivPoints(this.middle, this.subdivTs, 8, this.lineWidth2);
         float var18 = 0.0F;

         int var19;
         for(var19 = 0; var19 < var17; ++var19) {
            float var20 = this.subdivTs[var19];
            Helpers.subdivideCubicAt((var20 - var18) / (1.0F - var18), this.middle, var19 * 6, this.middle, var19 * 6, this.middle, var19 * 6 + 6);
            var18 = var20;
         }

         var19 = 0;

         for(int var21 = 0; var21 <= var17; ++var21) {
            var19 = this.computeOffsetCubic(this.middle, var21 * 6, this.lp, this.rp);
            if (var19 != 0) {
               this.emitLineTo(this.lp[0], this.lp[1]);
               switch (var19) {
                  case 4:
                     this.emitLineTo(this.lp[2], this.lp[3]);
                     this.emitLineTo(this.rp[0], this.rp[1], true);
                     break;
                  case 8:
                     this.emitCurveTo(this.lp[0], this.lp[1], this.lp[2], this.lp[3], this.lp[4], this.lp[5], this.lp[6], this.lp[7], false);
                     this.emitCurveTo(this.rp[0], this.rp[1], this.rp[2], this.rp[3], this.rp[4], this.rp[5], this.rp[6], this.rp[7], true);
               }

               this.emitLineTo(this.rp[var19 - 2], this.rp[var19 - 1], true);
            }
         }

         this.cmx = (this.lp[var19 - 2] - this.rp[var19 - 2]) / 2.0F;
         this.cmy = (this.lp[var19 - 1] - this.rp[var19 - 1]) / 2.0F;
         this.cdx = var11;
         this.cdy = var12;
         this.cx0 = var7;
         this.cy0 = var8;
         this.prev = 1;
      }
   }

   public void quadTo(float var1, float var2, float var3, float var4) {
      this.middle[0] = this.cx0;
      this.middle[1] = this.cy0;
      this.middle[2] = var1;
      this.middle[3] = var2;
      this.middle[4] = var3;
      this.middle[5] = var4;
      float var5 = this.middle[4];
      float var6 = this.middle[5];
      float var7 = this.middle[2] - this.middle[0];
      float var8 = this.middle[3] - this.middle[1];
      float var9 = this.middle[4] - this.middle[2];
      float var10 = this.middle[5] - this.middle[3];
      if (var7 == 0.0F && var8 == 0.0F || var9 == 0.0F && var10 == 0.0F) {
         var7 = var9 = this.middle[4] - this.middle[0];
         var8 = var10 = this.middle[5] - this.middle[1];
      }

      if (var7 == 0.0F && var8 == 0.0F) {
         this.lineTo(this.middle[0], this.middle[1]);
      } else {
         float var11;
         if (Math.abs(var7) < 0.1F && Math.abs(var8) < 0.1F) {
            var11 = (float)Math.sqrt((double)(var7 * var7 + var8 * var8));
            var7 /= var11;
            var8 /= var11;
         }

         if (Math.abs(var9) < 0.1F && Math.abs(var10) < 0.1F) {
            var11 = (float)Math.sqrt((double)(var9 * var9 + var10 * var10));
            var9 /= var11;
            var10 /= var11;
         }

         computeOffset(var7, var8, this.lineWidth2, this.offset[0]);
         var11 = this.offset[0][0];
         float var12 = this.offset[0][1];
         this.drawJoin(this.cdx, this.cdy, this.cx0, this.cy0, var7, var8, this.cmx, this.cmy, var11, var12);
         int var13 = findSubdivPoints(this.middle, this.subdivTs, 6, this.lineWidth2);
         float var14 = 0.0F;

         int var15;
         for(var15 = 0; var15 < var13; ++var15) {
            float var16 = this.subdivTs[var15];
            Helpers.subdivideQuadAt((var16 - var14) / (1.0F - var14), this.middle, var15 * 4, this.middle, var15 * 4, this.middle, var15 * 4 + 4);
            var14 = var16;
         }

         var15 = 0;

         for(int var17 = 0; var17 <= var13; ++var17) {
            var15 = this.computeOffsetQuad(this.middle, var17 * 4, this.lp, this.rp);
            if (var15 != 0) {
               this.emitLineTo(this.lp[0], this.lp[1]);
               switch (var15) {
                  case 4:
                     this.emitLineTo(this.lp[2], this.lp[3]);
                     this.emitLineTo(this.rp[0], this.rp[1], true);
                     break;
                  case 6:
                     this.emitQuadTo(this.lp[0], this.lp[1], this.lp[2], this.lp[3], this.lp[4], this.lp[5], false);
                     this.emitQuadTo(this.rp[0], this.rp[1], this.rp[2], this.rp[3], this.rp[4], this.rp[5], true);
               }

               this.emitLineTo(this.rp[var15 - 2], this.rp[var15 - 1], true);
            }
         }

         this.cmx = (this.lp[var15 - 2] - this.rp[var15 - 2]) / 2.0F;
         this.cmy = (this.lp[var15 - 1] - this.rp[var15 - 1]) / 2.0F;
         this.cdx = var9;
         this.cdy = var10;
         this.cx0 = var5;
         this.cy0 = var6;
         this.prev = 1;
      }
   }

   private static final class PolyStack {
      float[] curves = new float[400];
      int end = 0;
      int[] curveTypes = new int[50];
      int numCurves = 0;
      private static final int INIT_SIZE = 50;

      PolyStack() {
      }

      public boolean isEmpty() {
         return this.numCurves == 0;
      }

      private void ensureSpace(int var1) {
         int var2;
         if (this.end + var1 >= this.curves.length) {
            var2 = (this.end + var1) * 2;
            this.curves = Arrays.copyOf(this.curves, var2);
         }

         if (this.numCurves >= this.curveTypes.length) {
            var2 = this.numCurves * 2;
            this.curveTypes = Arrays.copyOf(this.curveTypes, var2);
         }

      }

      public void pushCubic(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.ensureSpace(6);
         this.curveTypes[this.numCurves++] = 8;
         this.curves[this.end++] = var5;
         this.curves[this.end++] = var6;
         this.curves[this.end++] = var3;
         this.curves[this.end++] = var4;
         this.curves[this.end++] = var1;
         this.curves[this.end++] = var2;
      }

      public void pushQuad(float var1, float var2, float var3, float var4) {
         this.ensureSpace(4);
         this.curveTypes[this.numCurves++] = 6;
         this.curves[this.end++] = var3;
         this.curves[this.end++] = var4;
         this.curves[this.end++] = var1;
         this.curves[this.end++] = var2;
      }

      public void pushLine(float var1, float var2) {
         this.ensureSpace(2);
         this.curveTypes[this.numCurves++] = 4;
         this.curves[this.end++] = var1;
         this.curves[this.end++] = var2;
      }

      public int pop(float[] var1) {
         int var2 = this.curveTypes[this.numCurves - 1];
         --this.numCurves;
         this.end -= var2 - 2;
         System.arraycopy(this.curves, this.end, var1, 0, var2 - 2);
         return var2;
      }

      public void pop(PathConsumer2D var1) {
         --this.numCurves;
         int var2 = this.curveTypes[this.numCurves];
         this.end -= var2 - 2;
         switch (var2) {
            case 4:
               var1.lineTo(this.curves[this.end], this.curves[this.end + 1]);
            case 5:
            case 7:
            default:
               break;
            case 6:
               var1.quadTo(this.curves[this.end + 0], this.curves[this.end + 1], this.curves[this.end + 2], this.curves[this.end + 3]);
               break;
            case 8:
               var1.curveTo(this.curves[this.end + 0], this.curves[this.end + 1], this.curves[this.end + 2], this.curves[this.end + 3], this.curves[this.end + 4], this.curves[this.end + 5]);
         }

      }

      public String toString() {
         String var1 = "";
         int var2 = this.numCurves;

         int var4;
         for(int var3 = this.end; var2 > 0; var1 = var1 + Arrays.toString(Arrays.copyOfRange(this.curves, var3, var3 + var4 - 2)) + "\n") {
            --var2;
            var4 = this.curveTypes[this.numCurves];
            var3 -= var4 - 2;
            switch (var4) {
               case 4:
                  var1 = var1 + "line: ";
               case 5:
               case 7:
               default:
                  break;
               case 6:
                  var1 = var1 + "quad: ";
                  break;
               case 8:
                  var1 = var1 + "cubic: ";
            }
         }

         return var1;
      }
   }
}
