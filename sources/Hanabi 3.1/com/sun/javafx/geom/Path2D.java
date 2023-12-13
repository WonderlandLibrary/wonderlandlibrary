package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

public class Path2D extends Shape implements PathConsumer2D {
   static final int[] curvecoords = new int[]{2, 2, 4, 6, 0};
   public static final int WIND_EVEN_ODD = 0;
   public static final int WIND_NON_ZERO = 1;
   private static final byte SEG_MOVETO = 0;
   private static final byte SEG_LINETO = 1;
   private static final byte SEG_QUADTO = 2;
   private static final byte SEG_CUBICTO = 3;
   private static final byte SEG_CLOSE = 4;
   byte[] pointTypes;
   int numTypes;
   int numCoords;
   int windingRule;
   static final int INIT_SIZE = 20;
   static final int EXPAND_MAX = 500;
   float[] floatCoords;
   float moveX;
   float moveY;
   float prevX;
   float prevY;
   float currX;
   float currY;

   public Path2D() {
      this(1, 20);
   }

   public Path2D(int var1) {
      this(var1, 20);
   }

   public Path2D(int var1, int var2) {
      this.setWindingRule(var1);
      this.pointTypes = new byte[var2];
      this.floatCoords = new float[var2 * 2];
   }

   public Path2D(Shape var1) {
      this(var1, (BaseTransform)null);
   }

   public Path2D(Shape var1, BaseTransform var2) {
      if (var1 instanceof Path2D) {
         Path2D var3 = (Path2D)var1;
         this.setWindingRule(var3.windingRule);
         this.numTypes = var3.numTypes;
         this.pointTypes = copyOf(var3.pointTypes, var3.pointTypes.length);
         this.numCoords = var3.numCoords;
         if (var2 != null && !var2.isIdentity()) {
            this.floatCoords = new float[this.numCoords + 6];
            var2.transform((float[])var3.floatCoords, 0, (float[])this.floatCoords, 0, this.numCoords / 2);
            this.floatCoords[this.numCoords + 0] = this.moveX;
            this.floatCoords[this.numCoords + 1] = this.moveY;
            this.floatCoords[this.numCoords + 2] = this.prevX;
            this.floatCoords[this.numCoords + 3] = this.prevY;
            this.floatCoords[this.numCoords + 4] = this.currX;
            this.floatCoords[this.numCoords + 5] = this.currY;
            var2.transform((float[])this.floatCoords, this.numCoords, (float[])this.floatCoords, this.numCoords, 3);
            this.moveX = this.floatCoords[this.numCoords + 0];
            this.moveY = this.floatCoords[this.numCoords + 1];
            this.prevX = this.floatCoords[this.numCoords + 2];
            this.prevY = this.floatCoords[this.numCoords + 3];
            this.currX = this.floatCoords[this.numCoords + 4];
            this.currY = this.floatCoords[this.numCoords + 5];
         } else {
            this.floatCoords = copyOf(var3.floatCoords, this.numCoords);
            this.moveX = var3.moveX;
            this.moveY = var3.moveY;
            this.prevX = var3.prevX;
            this.prevY = var3.prevY;
            this.currX = var3.currX;
            this.currY = var3.currY;
         }
      } else {
         PathIterator var4 = var1.getPathIterator(var2);
         this.setWindingRule(var4.getWindingRule());
         this.pointTypes = new byte[20];
         this.floatCoords = new float[40];
         this.append(var4, false);
      }

   }

   public Path2D(int var1, byte[] var2, int var3, float[] var4, int var5) {
      this.windingRule = var1;
      this.pointTypes = var2;
      this.numTypes = var3;
      this.floatCoords = var4;
      this.numCoords = var5;
   }

   Point2D getPoint(int var1) {
      return new Point2D(this.floatCoords[var1], this.floatCoords[var1 + 1]);
   }

   private boolean close(int var1, float var2, float var3) {
      return Math.abs((float)var1 - var2) <= var3;
   }

   public boolean checkAndGetIntRect(Rectangle var1, float var2) {
      if (this.numTypes == 5) {
         if (this.pointTypes[4] != 1 && this.pointTypes[4] != 4) {
            return false;
         }
      } else if (this.numTypes == 6) {
         if (this.pointTypes[4] != 1) {
            return false;
         }

         if (this.pointTypes[5] != 4) {
            return false;
         }
      } else if (this.numTypes != 4) {
         return false;
      }

      if (this.pointTypes[0] != 0) {
         return false;
      } else if (this.pointTypes[1] != 1) {
         return false;
      } else if (this.pointTypes[2] != 1) {
         return false;
      } else if (this.pointTypes[3] != 1) {
         return false;
      } else {
         int var3 = (int)(this.floatCoords[0] + 0.5F);
         int var4 = (int)(this.floatCoords[1] + 0.5F);
         if (!this.close(var3, this.floatCoords[0], var2)) {
            return false;
         } else if (!this.close(var4, this.floatCoords[1], var2)) {
            return false;
         } else {
            int var5 = (int)(this.floatCoords[2] + 0.5F);
            int var6 = (int)(this.floatCoords[3] + 0.5F);
            if (!this.close(var5, this.floatCoords[2], var2)) {
               return false;
            } else if (!this.close(var6, this.floatCoords[3], var2)) {
               return false;
            } else {
               int var7 = (int)(this.floatCoords[4] + 0.5F);
               int var8 = (int)(this.floatCoords[5] + 0.5F);
               if (!this.close(var7, this.floatCoords[4], var2)) {
                  return false;
               } else if (!this.close(var8, this.floatCoords[5], var2)) {
                  return false;
               } else {
                  int var9 = (int)(this.floatCoords[6] + 0.5F);
                  int var10 = (int)(this.floatCoords[7] + 0.5F);
                  if (!this.close(var9, this.floatCoords[6], var2)) {
                     return false;
                  } else if (!this.close(var10, this.floatCoords[7], var2)) {
                     return false;
                  } else {
                     if (this.numTypes > 4 && this.pointTypes[4] == 1) {
                        if (!this.close(var3, this.floatCoords[8], var2)) {
                           return false;
                        }

                        if (!this.close(var4, this.floatCoords[9], var2)) {
                           return false;
                        }
                     }

                     if ((var3 != var5 || var7 != var9 || var4 != var10 || var6 != var8) && (var4 != var6 || var8 != var10 || var3 != var9 || var5 != var7)) {
                        return false;
                     } else {
                        int var11;
                        int var13;
                        if (var7 < var3) {
                           var11 = var7;
                           var13 = var3 - var7;
                        } else {
                           var11 = var3;
                           var13 = var7 - var3;
                        }

                        int var12;
                        int var14;
                        if (var8 < var4) {
                           var12 = var8;
                           var14 = var4 - var8;
                        } else {
                           var12 = var4;
                           var14 = var8 - var4;
                        }

                        if (var13 < 0) {
                           return false;
                        } else if (var14 < 0) {
                           return false;
                        } else {
                           if (var1 != null) {
                              var1.setBounds(var11, var12, var13, var14);
                           }

                           return true;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   void needRoom(boolean var1, int var2) {
      if (var1 && this.numTypes == 0) {
         throw new IllegalPathStateException("missing initial moveto in path definition");
      } else {
         int var3 = this.pointTypes.length;
         int var4;
         if (var3 == 0) {
            this.pointTypes = new byte[2];
         } else if (this.numTypes >= var3) {
            var4 = var3;
            if (var3 > 500) {
               var4 = 500;
            }

            this.pointTypes = copyOf(this.pointTypes, var3 + var4);
         }

         var3 = this.floatCoords.length;
         if (this.numCoords + var2 > var3) {
            var4 = var3;
            if (var3 > 1000) {
               var4 = 1000;
            }

            if (var4 < var2) {
               var4 = var2;
            }

            this.floatCoords = copyOf(this.floatCoords, var3 + var4);
         }

      }
   }

   public final void moveTo(float var1, float var2) {
      if (this.numTypes > 0 && this.pointTypes[this.numTypes - 1] == 0) {
         this.floatCoords[this.numCoords - 2] = this.moveX = this.prevX = this.currX = var1;
         this.floatCoords[this.numCoords - 1] = this.moveY = this.prevY = this.currY = var2;
      } else {
         this.needRoom(false, 2);
         this.pointTypes[this.numTypes++] = 0;
         this.floatCoords[this.numCoords++] = this.moveX = this.prevX = this.currX = var1;
         this.floatCoords[this.numCoords++] = this.moveY = this.prevY = this.currY = var2;
      }

   }

   public final void moveToRel(float var1, float var2) {
      if (this.numTypes > 0 && this.pointTypes[this.numTypes - 1] == 0) {
         this.floatCoords[this.numCoords - 2] = this.moveX = this.prevX = this.currX += var1;
         this.floatCoords[this.numCoords - 1] = this.moveY = this.prevY = this.currY += var2;
      } else {
         this.needRoom(true, 2);
         this.pointTypes[this.numTypes++] = 0;
         this.floatCoords[this.numCoords++] = this.moveX = this.prevX = this.currX += var1;
         this.floatCoords[this.numCoords++] = this.moveY = this.prevY = this.currY += var2;
      }

   }

   public final void lineTo(float var1, float var2) {
      this.needRoom(true, 2);
      this.pointTypes[this.numTypes++] = 1;
      this.floatCoords[this.numCoords++] = this.prevX = this.currX = var1;
      this.floatCoords[this.numCoords++] = this.prevY = this.currY = var2;
   }

   public final void lineToRel(float var1, float var2) {
      this.needRoom(true, 2);
      this.pointTypes[this.numTypes++] = 1;
      this.floatCoords[this.numCoords++] = this.prevX = this.currX += var1;
      this.floatCoords[this.numCoords++] = this.prevY = this.currY += var2;
   }

   public final void quadTo(float var1, float var2, float var3, float var4) {
      this.needRoom(true, 4);
      this.pointTypes[this.numTypes++] = 2;
      this.floatCoords[this.numCoords++] = this.prevX = var1;
      this.floatCoords[this.numCoords++] = this.prevY = var2;
      this.floatCoords[this.numCoords++] = this.currX = var3;
      this.floatCoords[this.numCoords++] = this.currY = var4;
   }

   public final void quadToRel(float var1, float var2, float var3, float var4) {
      this.needRoom(true, 4);
      this.pointTypes[this.numTypes++] = 2;
      this.floatCoords[this.numCoords++] = this.prevX = this.currX + var1;
      this.floatCoords[this.numCoords++] = this.prevY = this.currY + var2;
      this.floatCoords[this.numCoords++] = this.currX += var3;
      this.floatCoords[this.numCoords++] = this.currY += var4;
   }

   public final void quadToSmooth(float var1, float var2) {
      this.needRoom(true, 4);
      this.pointTypes[this.numTypes++] = 2;
      this.floatCoords[this.numCoords++] = this.prevX = this.currX * 2.0F - this.prevX;
      this.floatCoords[this.numCoords++] = this.prevY = this.currY * 2.0F - this.prevY;
      this.floatCoords[this.numCoords++] = this.currX = var1;
      this.floatCoords[this.numCoords++] = this.currY = var2;
   }

   public final void quadToSmoothRel(float var1, float var2) {
      this.needRoom(true, 4);
      this.pointTypes[this.numTypes++] = 2;
      this.floatCoords[this.numCoords++] = this.prevX = this.currX * 2.0F - this.prevX;
      this.floatCoords[this.numCoords++] = this.prevY = this.currY * 2.0F - this.prevY;
      this.floatCoords[this.numCoords++] = this.currX += var1;
      this.floatCoords[this.numCoords++] = this.currY += var2;
   }

   public final void curveTo(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.needRoom(true, 6);
      this.pointTypes[this.numTypes++] = 3;
      this.floatCoords[this.numCoords++] = var1;
      this.floatCoords[this.numCoords++] = var2;
      this.floatCoords[this.numCoords++] = this.prevX = var3;
      this.floatCoords[this.numCoords++] = this.prevY = var4;
      this.floatCoords[this.numCoords++] = this.currX = var5;
      this.floatCoords[this.numCoords++] = this.currY = var6;
   }

   public final void curveToRel(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.needRoom(true, 6);
      this.pointTypes[this.numTypes++] = 3;
      this.floatCoords[this.numCoords++] = this.currX + var1;
      this.floatCoords[this.numCoords++] = this.currY + var2;
      this.floatCoords[this.numCoords++] = this.prevX = this.currX + var3;
      this.floatCoords[this.numCoords++] = this.prevY = this.currY + var4;
      this.floatCoords[this.numCoords++] = this.currX += var5;
      this.floatCoords[this.numCoords++] = this.currY += var6;
   }

   public final void curveToSmooth(float var1, float var2, float var3, float var4) {
      this.needRoom(true, 6);
      this.pointTypes[this.numTypes++] = 3;
      this.floatCoords[this.numCoords++] = this.currX * 2.0F - this.prevX;
      this.floatCoords[this.numCoords++] = this.currY * 2.0F - this.prevY;
      this.floatCoords[this.numCoords++] = this.prevX = var1;
      this.floatCoords[this.numCoords++] = this.prevY = var2;
      this.floatCoords[this.numCoords++] = this.currX = var3;
      this.floatCoords[this.numCoords++] = this.currY = var4;
   }

   public final void curveToSmoothRel(float var1, float var2, float var3, float var4) {
      this.needRoom(true, 6);
      this.pointTypes[this.numTypes++] = 3;
      this.floatCoords[this.numCoords++] = this.currX * 2.0F - this.prevX;
      this.floatCoords[this.numCoords++] = this.currY * 2.0F - this.prevY;
      this.floatCoords[this.numCoords++] = this.prevX = this.currX + var1;
      this.floatCoords[this.numCoords++] = this.prevY = this.currY + var2;
      this.floatCoords[this.numCoords++] = this.currX += var3;
      this.floatCoords[this.numCoords++] = this.currY += var4;
   }

   public final void ovalQuadrantTo(float var1, float var2, float var3, float var4, float var5, float var6) {
      if (this.numTypes < 1) {
         throw new IllegalPathStateException("missing initial moveto in path definition");
      } else {
         this.appendOvalQuadrant(this.currX, this.currY, var1, var2, var3, var4, var5, var6, Path2D.CornerPrefix.CORNER_ONLY);
      }
   }

   public final void appendOvalQuadrant(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, CornerPrefix var9) {
      if (var7 >= 0.0F && var7 <= var8 && var8 <= 1.0F) {
         float var10 = (float)((double)var1 + (double)(var3 - var1) * 0.5522847498307933);
         float var11 = (float)((double)var2 + (double)(var4 - var2) * 0.5522847498307933);
         float var12 = (float)((double)var5 + (double)(var3 - var5) * 0.5522847498307933);
         float var13 = (float)((double)var6 + (double)(var4 - var6) * 0.5522847498307933);
         if (var8 < 1.0F) {
            float var14 = 1.0F - var8;
            var5 += (var12 - var5) * var14;
            var6 += (var13 - var6) * var14;
            var12 += (var10 - var12) * var14;
            var13 += (var11 - var13) * var14;
            var10 += (var1 - var10) * var14;
            var11 += (var2 - var11) * var14;
            var5 += (var12 - var5) * var14;
            var6 += (var13 - var6) * var14;
            var12 += (var10 - var12) * var14;
            var13 += (var11 - var13) * var14;
            var5 += (var12 - var5) * var14;
            var6 += (var13 - var6) * var14;
         }

         if (var7 > 0.0F) {
            if (var8 < 1.0F) {
               var7 /= var8;
            }

            var1 += (var10 - var1) * var7;
            var2 += (var11 - var2) * var7;
            var10 += (var12 - var10) * var7;
            var11 += (var13 - var11) * var7;
            var12 += (var5 - var12) * var7;
            var13 += (var6 - var13) * var7;
            var1 += (var10 - var1) * var7;
            var2 += (var11 - var2) * var7;
            var10 += (var12 - var10) * var7;
            var11 += (var13 - var11) * var7;
            var1 += (var10 - var1) * var7;
            var2 += (var11 - var2) * var7;
         }

         if (var9 == Path2D.CornerPrefix.MOVE_THEN_CORNER) {
            this.moveTo(var1, var2);
         } else if (var9 == Path2D.CornerPrefix.LINE_THEN_CORNER && (this.numTypes == 1 || var1 != this.currX || var2 != this.currY)) {
            this.lineTo(var1, var2);
         }

         if (var7 != var8 && (var1 != var10 || var10 != var12 || var12 != var5 || var2 != var11 || var11 != var13 || var13 != var6)) {
            this.curveTo(var10, var11, var12, var13, var5, var6);
         } else if (var9 != Path2D.CornerPrefix.LINE_THEN_CORNER) {
            this.lineTo(var5, var6);
         }

      } else {
         throw new IllegalArgumentException("0 <= tfrom <= tto <= 1 required");
      }
   }

   public void arcTo(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) {
      if (this.numTypes < 1) {
         throw new IllegalPathStateException("missing initial moveto in path definition");
      } else {
         double var8 = (double)Math.abs(var1);
         double var10 = (double)Math.abs(var2);
         if (var8 != 0.0 && var10 != 0.0) {
            double var12 = (double)this.currX;
            double var14 = (double)this.currY;
            double var16 = (double)var6;
            double var18 = (double)var7;
            if (var12 != var16 || var14 != var18) {
               double var20;
               double var22;
               if ((double)var3 == 0.0) {
                  var20 = 1.0;
                  var22 = 0.0;
               } else {
                  var20 = Math.cos((double)var3);
                  var22 = Math.sin((double)var3);
               }

               double var24 = (var12 + var16) / 2.0;
               double var26 = (var14 + var18) / 2.0;
               double var28 = var12 - var24;
               double var30 = var14 - var26;
               double var32 = (var20 * var28 + var22 * var30) / var8;
               double var34 = (var20 * var30 - var22 * var28) / var10;
               double var36 = var32 * var32 + var34 * var34;
               double var38;
               double var40;
               double var42;
               double var44;
               double var46;
               double var48;
               double var50;
               if (var36 >= 1.0) {
                  var38 = var34 * var8;
                  var40 = var32 * var10;
                  if (var5) {
                     var38 = -var38;
                  } else {
                     var40 = -var40;
                  }

                  var42 = var20 * var38 - var22 * var40;
                  var44 = var20 * var40 + var22 * var38;
                  var46 = var24 + var42;
                  var48 = var26 + var44;
                  var50 = var12 + var42;
                  double var71 = var14 + var44;
                  this.appendOvalQuadrant((float)var12, (float)var14, (float)var50, (float)var71, (float)var46, (float)var48, 0.0F, 1.0F, Path2D.CornerPrefix.CORNER_ONLY);
                  var50 = var16 + var42;
                  var71 = var18 + var44;
                  this.appendOvalQuadrant((float)var46, (float)var48, (float)var50, (float)var71, (float)var16, (float)var18, 0.0F, 1.0F, Path2D.CornerPrefix.CORNER_ONLY);
               } else {
                  var38 = Math.sqrt((1.0 - var36) / var36);
                  var40 = var38 * var34;
                  var42 = var38 * var32;
                  if (var4 == var5) {
                     var40 = -var40;
                  } else {
                     var42 = -var42;
                  }

                  var24 += var20 * var40 * var8 - var22 * var42 * var10;
                  var26 += var20 * var42 * var10 + var22 * var40 * var8;
                  var44 = var32 - var40;
                  var46 = var34 - var42;
                  var48 = -(var32 + var40);
                  var50 = -(var34 + var42);
                  boolean var52 = false;
                  float var53 = 1.0F;
                  boolean var54 = false;

                  do {
                     double var55 = var46;
                     double var57 = var44;
                     if (var5) {
                        var55 = -var46;
                     } else {
                        var57 = -var44;
                     }

                     double var59;
                     if (var55 * var48 + var57 * var50 > 0.0) {
                        var59 = var44 * var48 + var46 * var50;
                        if (var59 >= 0.0) {
                           var53 = (float)(Math.acos(var59) / 1.5707963267948966);
                           var52 = true;
                        }

                        var54 = true;
                     } else if (var54) {
                        break;
                     }

                     var59 = var20 * var55 * var8 - var22 * var57 * var10;
                     double var61 = var20 * var57 * var10 + var22 * var55 * var8;
                     double var63 = var24 + var59;
                     double var65 = var26 + var61;
                     double var67 = var12 + var59;
                     double var69 = var14 + var61;
                     this.appendOvalQuadrant((float)var12, (float)var14, (float)var67, (float)var69, (float)var63, (float)var65, 0.0F, var53, Path2D.CornerPrefix.CORNER_ONLY);
                     var12 = var63;
                     var14 = var65;
                     var44 = var55;
                     var46 = var57;
                  } while(!var52);

               }
            }
         } else {
            this.lineTo(var6, var7);
         }
      }
   }

   public void arcToRel(float var1, float var2, float var3, boolean var4, boolean var5, float var6, float var7) {
      this.arcTo(var1, var2, var3, var4, var5, this.currX + var6, this.currY + var7);
   }

   int pointCrossings(float var1, float var2) {
      float[] var9 = this.floatCoords;
      float var3;
      float var5 = var3 = var9[0];
      float var4;
      float var6 = var4 = var9[1];
      int var10 = 0;
      int var11 = 2;

      for(int var12 = 1; var12 < this.numTypes; ++var12) {
         float var7;
         float var8;
         switch (this.pointTypes[var12]) {
            case 0:
               if (var6 != var4) {
                  var10 += Shape.pointCrossingsForLine(var1, var2, var5, var6, var3, var4);
               }

               var3 = var5 = var9[var11++];
               var4 = var6 = var9[var11++];
               break;
            case 1:
               var10 += Shape.pointCrossingsForLine(var1, var2, var5, var6, var7 = var9[var11++], var8 = var9[var11++]);
               var5 = var7;
               var6 = var8;
               break;
            case 2:
               var10 += Shape.pointCrossingsForQuad(var1, var2, var5, var6, var9[var11++], var9[var11++], var7 = var9[var11++], var8 = var9[var11++], 0);
               var5 = var7;
               var6 = var8;
               break;
            case 3:
               var10 += Shape.pointCrossingsForCubic(var1, var2, var5, var6, var9[var11++], var9[var11++], var9[var11++], var9[var11++], var7 = var9[var11++], var8 = var9[var11++], 0);
               var5 = var7;
               var6 = var8;
               break;
            case 4:
               if (var6 != var4) {
                  var10 += Shape.pointCrossingsForLine(var1, var2, var5, var6, var3, var4);
               }

               var5 = var3;
               var6 = var4;
         }
      }

      if (var6 != var4) {
         var10 += Shape.pointCrossingsForLine(var1, var2, var5, var6, var3, var4);
      }

      return var10;
   }

   int rectCrossings(float var1, float var2, float var3, float var4) {
      float[] var5 = this.floatCoords;
      float var8;
      float var6 = var8 = var5[0];
      float var9;
      float var7 = var9 = var5[1];
      int var12 = 0;
      int var13 = 2;

      for(int var14 = 1; var12 != Integer.MIN_VALUE && var14 < this.numTypes; ++var14) {
         float var10;
         float var11;
         switch (this.pointTypes[var14]) {
            case 0:
               if (var6 != var8 || var7 != var9) {
                  var12 = Shape.rectCrossingsForLine(var12, var1, var2, var3, var4, var6, var7, var8, var9);
               }

               var8 = var6 = var5[var13++];
               var9 = var7 = var5[var13++];
               break;
            case 1:
               var12 = Shape.rectCrossingsForLine(var12, var1, var2, var3, var4, var6, var7, var10 = var5[var13++], var11 = var5[var13++]);
               var6 = var10;
               var7 = var11;
               break;
            case 2:
               var12 = Shape.rectCrossingsForQuad(var12, var1, var2, var3, var4, var6, var7, var5[var13++], var5[var13++], var10 = var5[var13++], var11 = var5[var13++], 0);
               var6 = var10;
               var7 = var11;
               break;
            case 3:
               var12 = Shape.rectCrossingsForCubic(var12, var1, var2, var3, var4, var6, var7, var5[var13++], var5[var13++], var5[var13++], var5[var13++], var10 = var5[var13++], var11 = var5[var13++], 0);
               var6 = var10;
               var7 = var11;
               break;
            case 4:
               if (var6 != var8 || var7 != var9) {
                  var12 = Shape.rectCrossingsForLine(var12, var1, var2, var3, var4, var6, var7, var8, var9);
               }

               var6 = var8;
               var7 = var9;
         }
      }

      if (var12 != Integer.MIN_VALUE && (var6 != var8 || var7 != var9)) {
         var12 = Shape.rectCrossingsForLine(var12, var1, var2, var3, var4, var6, var7, var8, var9);
      }

      return var12;
   }

   public final void append(PathIterator var1, boolean var2) {
      for(float[] var3 = new float[6]; !var1.isDone(); var2 = false) {
         switch (var1.currentSegment(var3)) {
            case 0:
               if (!var2 || this.numTypes < 1 || this.numCoords < 1) {
                  this.moveTo(var3[0], var3[1]);
                  break;
               } else if (this.pointTypes[this.numTypes - 1] != 4 && this.floatCoords[this.numCoords - 2] == var3[0] && this.floatCoords[this.numCoords - 1] == var3[1]) {
                  break;
               }
            case 1:
               this.lineTo(var3[0], var3[1]);
               break;
            case 2:
               this.quadTo(var3[0], var3[1], var3[2], var3[3]);
               break;
            case 3:
               this.curveTo(var3[0], var3[1], var3[2], var3[3], var3[4], var3[5]);
               break;
            case 4:
               this.closePath();
         }

         var1.next();
      }

   }

   public final void transform(BaseTransform var1) {
      if (this.numCoords != 0) {
         this.needRoom(false, 6);
         this.floatCoords[this.numCoords + 0] = this.moveX;
         this.floatCoords[this.numCoords + 1] = this.moveY;
         this.floatCoords[this.numCoords + 2] = this.prevX;
         this.floatCoords[this.numCoords + 3] = this.prevY;
         this.floatCoords[this.numCoords + 4] = this.currX;
         this.floatCoords[this.numCoords + 5] = this.currY;
         var1.transform((float[])this.floatCoords, 0, (float[])this.floatCoords, 0, this.numCoords / 2 + 3);
         this.moveX = this.floatCoords[this.numCoords + 0];
         this.moveY = this.floatCoords[this.numCoords + 1];
         this.prevX = this.floatCoords[this.numCoords + 2];
         this.prevY = this.floatCoords[this.numCoords + 3];
         this.currX = this.floatCoords[this.numCoords + 4];
         this.currY = this.floatCoords[this.numCoords + 5];
      }
   }

   public final RectBounds getBounds() {
      int var5 = this.numCoords;
      float var1;
      float var2;
      float var3;
      float var4;
      if (var5 > 0) {
         --var5;
         var2 = var4 = this.floatCoords[var5];
         --var5;
         var1 = var3 = this.floatCoords[var5];

         while(var5 > 0) {
            --var5;
            float var6 = this.floatCoords[var5];
            --var5;
            float var7 = this.floatCoords[var5];
            if (var7 < var1) {
               var1 = var7;
            }

            if (var6 < var2) {
               var2 = var6;
            }

            if (var7 > var3) {
               var3 = var7;
            }

            if (var6 > var4) {
               var4 = var6;
            }
         }
      } else {
         var4 = 0.0F;
         var3 = 0.0F;
         var2 = 0.0F;
         var1 = 0.0F;
      }

      return new RectBounds(var1, var2, var3, var4);
   }

   public final int getNumCommands() {
      return this.numTypes;
   }

   public final byte[] getCommandsNoClone() {
      return this.pointTypes;
   }

   public final float[] getFloatCoordsNoClone() {
      return this.floatCoords;
   }

   public PathIterator getPathIterator(BaseTransform var1) {
      return (PathIterator)(var1 == null ? new CopyIterator(this) : new TxIterator(this, var1));
   }

   public final void closePath() {
      if (this.numTypes == 0 || this.pointTypes[this.numTypes - 1] != 4) {
         this.needRoom(true, 0);
         this.pointTypes[this.numTypes++] = 4;
         this.prevX = this.currX = this.moveX;
         this.prevY = this.currY = this.moveY;
      }

   }

   public void pathDone() {
   }

   public final void append(Shape var1, boolean var2) {
      this.append(var1.getPathIterator((BaseTransform)null), var2);
   }

   public final void appendSVGPath(String var1) {
      SVGParser var2 = new SVGParser(var1);

      label117:
      for(var2.allowcomma = false; !var2.isDone(); var2.allowcomma = false) {
         var2.allowcomma = false;
         char var3 = var2.getChar();
         switch (var3) {
            case 'A':
               while(true) {
                  this.arcTo(var2.f(), var2.f(), var2.a(), var2.b(), var2.b(), var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'B':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'I':
            case 'J':
            case 'K':
            case 'N':
            case 'O':
            case 'P':
            case 'R':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '_':
            case '`':
            case 'b':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'i':
            case 'j':
            case 'k':
            case 'n':
            case 'o':
            case 'p':
            case 'r':
            case 'u':
            case 'w':
            case 'x':
            case 'y':
            default:
               throw new IllegalArgumentException("invalid command (" + var3 + ") in SVG path at pos=" + var2.pos);
            case 'C':
               while(true) {
                  this.curveTo(var2.f(), var2.f(), var2.f(), var2.f(), var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'H':
               while(true) {
                  this.lineTo(var2.f(), this.currY);
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'L':
               while(true) {
                  this.lineTo(var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'M':
               this.moveTo(var2.f(), var2.f());

               while(true) {
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }

                  this.lineTo(var2.f(), var2.f());
               }
            case 'Q':
               while(true) {
                  this.quadTo(var2.f(), var2.f(), var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'S':
               while(true) {
                  this.curveToSmooth(var2.f(), var2.f(), var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'T':
               while(true) {
                  this.quadToSmooth(var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'V':
               while(true) {
                  this.lineTo(this.currX, var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'Z':
            case 'z':
               this.closePath();
               break;
            case 'a':
               while(true) {
                  this.arcToRel(var2.f(), var2.f(), var2.a(), var2.b(), var2.b(), var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'c':
               while(true) {
                  this.curveToRel(var2.f(), var2.f(), var2.f(), var2.f(), var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'h':
               while(true) {
                  this.lineToRel(var2.f(), 0.0F);
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'l':
               while(true) {
                  this.lineToRel(var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'm':
               if (this.numTypes > 0) {
                  this.moveToRel(var2.f(), var2.f());
               } else {
                  this.moveTo(var2.f(), var2.f());
               }

               while(true) {
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }

                  this.lineToRel(var2.f(), var2.f());
               }
            case 'q':
               while(true) {
                  this.quadToRel(var2.f(), var2.f(), var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 's':
               while(true) {
                  this.curveToSmoothRel(var2.f(), var2.f(), var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 't':
               while(true) {
                  this.quadToSmoothRel(var2.f(), var2.f());
                  if (!var2.nextIsNumber()) {
                     continue label117;
                  }
               }
            case 'v':
               do {
                  this.lineToRel(0.0F, var2.f());
               } while(var2.nextIsNumber());
         }
      }

   }

   public final int getWindingRule() {
      return this.windingRule;
   }

   public final void setWindingRule(int var1) {
      if (var1 != 0 && var1 != 1) {
         throw new IllegalArgumentException("winding rule must be WIND_EVEN_ODD or WIND_NON_ZERO");
      } else {
         this.windingRule = var1;
      }
   }

   public final Point2D getCurrentPoint() {
      return this.numTypes < 1 ? null : new Point2D(this.currX, this.currY);
   }

   public final float getCurrentX() {
      if (this.numTypes < 1) {
         throw new IllegalPathStateException("no current point in empty path");
      } else {
         return this.currX;
      }
   }

   public final float getCurrentY() {
      if (this.numTypes < 1) {
         throw new IllegalPathStateException("no current point in empty path");
      } else {
         return this.currY;
      }
   }

   public final void reset() {
      this.numTypes = this.numCoords = 0;
      this.moveX = this.moveY = this.prevX = this.prevY = this.currX = this.currY = 0.0F;
   }

   public final Shape createTransformedShape(BaseTransform var1) {
      return new Path2D(this, var1);
   }

   public Path2D copy() {
      return new Path2D(this);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         if (var1 instanceof Path2D) {
            Path2D var2 = (Path2D)var1;
            if (var2.numTypes == this.numTypes && var2.numCoords == this.numCoords && var2.windingRule == this.windingRule) {
               int var3;
               for(var3 = 0; var3 < this.numTypes; ++var3) {
                  if (var2.pointTypes[var3] != this.pointTypes[var3]) {
                     return false;
                  }
               }

               for(var3 = 0; var3 < this.numCoords; ++var3) {
                  if (var2.floatCoords[var3] != this.floatCoords[var3]) {
                     return false;
                  }
               }

               return true;
            }
         }

         return false;
      }
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 11 * var1 + this.numTypes;
      var1 = 11 * var1 + this.numCoords;
      var1 = 11 * var1 + this.windingRule;

      int var2;
      for(var2 = 0; var2 < this.numTypes; ++var2) {
         var1 = 11 * var1 + this.pointTypes[var2];
      }

      for(var2 = 0; var2 < this.numCoords; ++var2) {
         var1 = 11 * var1 + Float.floatToIntBits(this.floatCoords[var2]);
      }

      return var1;
   }

   public static boolean contains(PathIterator var0, float var1, float var2) {
      if (var1 * 0.0F + var2 * 0.0F == 0.0F) {
         int var3 = var0.getWindingRule() == 1 ? -1 : 1;
         int var4 = Shape.pointCrossingsForPath(var0, var1, var2);
         return (var4 & var3) != 0;
      } else {
         return false;
      }
   }

   public static boolean contains(PathIterator var0, Point2D var1) {
      return contains(var0, var1.x, var1.y);
   }

   public final boolean contains(float var1, float var2) {
      if (var1 * 0.0F + var2 * 0.0F == 0.0F) {
         if (this.numTypes < 2) {
            return false;
         } else {
            int var3 = this.windingRule == 1 ? -1 : 1;
            return (this.pointCrossings(var1, var2) & var3) != 0;
         }
      } else {
         return false;
      }
   }

   public final boolean contains(Point2D var1) {
      return this.contains(var1.x, var1.y);
   }

   public static boolean contains(PathIterator var0, float var1, float var2, float var3, float var4) {
      if (!Float.isNaN(var1 + var3) && !Float.isNaN(var2 + var4)) {
         if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
            int var5 = var0.getWindingRule() == 1 ? -1 : 2;
            int var6 = Shape.rectCrossingsForPath(var0, var1, var2, var1 + var3, var2 + var4);
            return var6 != Integer.MIN_VALUE && (var6 & var5) != 0;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public final boolean contains(float var1, float var2, float var3, float var4) {
      if (!Float.isNaN(var1 + var3) && !Float.isNaN(var2 + var4)) {
         if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
            int var5 = this.windingRule == 1 ? -1 : 2;
            int var6 = this.rectCrossings(var1, var2, var1 + var3, var2 + var4);
            return var6 != Integer.MIN_VALUE && (var6 & var5) != 0;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean intersects(PathIterator var0, float var1, float var2, float var3, float var4) {
      if (!Float.isNaN(var1 + var3) && !Float.isNaN(var2 + var4)) {
         if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
            int var5 = var0.getWindingRule() == 1 ? -1 : 2;
            int var6 = Shape.rectCrossingsForPath(var0, var1, var2, var1 + var3, var2 + var4);
            return var6 == Integer.MIN_VALUE || (var6 & var5) != 0;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public final boolean intersects(float var1, float var2, float var3, float var4) {
      if (!Float.isNaN(var1 + var3) && !Float.isNaN(var2 + var4)) {
         if (!(var3 <= 0.0F) && !(var4 <= 0.0F)) {
            int var5 = this.windingRule == 1 ? -1 : 2;
            int var6 = this.rectCrossings(var1, var2, var1 + var3, var2 + var4);
            return var6 == Integer.MIN_VALUE || (var6 & var5) != 0;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public PathIterator getPathIterator(BaseTransform var1, float var2) {
      return new FlatteningPathIterator(this.getPathIterator(var1), var2);
   }

   static byte[] copyOf(byte[] var0, int var1) {
      byte[] var2 = new byte[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   static float[] copyOf(float[] var0, int var1) {
      float[] var2 = new float[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public void setTo(Path2D var1) {
      this.numTypes = var1.numTypes;
      this.numCoords = var1.numCoords;
      if (this.numTypes > this.pointTypes.length) {
         this.pointTypes = new byte[this.numTypes];
      }

      System.arraycopy(var1.pointTypes, 0, this.pointTypes, 0, this.numTypes);
      if (this.numCoords > this.floatCoords.length) {
         this.floatCoords = new float[this.numCoords];
      }

      System.arraycopy(var1.floatCoords, 0, this.floatCoords, 0, this.numCoords);
      this.windingRule = var1.windingRule;
      this.moveX = var1.moveX;
      this.moveY = var1.moveY;
      this.prevX = var1.prevX;
      this.prevY = var1.prevY;
      this.currX = var1.currX;
      this.currY = var1.currY;
   }

   abstract static class Iterator implements PathIterator {
      int typeIdx;
      int pointIdx;
      Path2D path;

      Iterator(Path2D var1) {
         this.path = var1;
      }

      public int getWindingRule() {
         return this.path.getWindingRule();
      }

      public boolean isDone() {
         return this.typeIdx >= this.path.numTypes;
      }

      public void next() {
         byte var1 = this.path.pointTypes[this.typeIdx++];
         this.pointIdx += Path2D.curvecoords[var1];
      }
   }

   static class SVGParser {
      final String svgpath;
      final int len;
      int pos;
      boolean allowcomma;

      public SVGParser(String var1) {
         this.svgpath = var1;
         this.len = var1.length();
      }

      public boolean isDone() {
         return this.toNextNonWsp() >= this.len;
      }

      public char getChar() {
         return this.svgpath.charAt(this.pos++);
      }

      public boolean nextIsNumber() {
         if (this.toNextNonWsp() < this.len) {
            switch (this.svgpath.charAt(this.pos)) {
               case '+':
               case '-':
               case '.':
               case '0':
               case '1':
               case '2':
               case '3':
               case '4':
               case '5':
               case '6':
               case '7':
               case '8':
               case '9':
                  return true;
               case ',':
               case '/':
            }
         }

         return false;
      }

      public float f() {
         return this.getFloat();
      }

      public float a() {
         return (float)Math.toRadians((double)this.getFloat());
      }

      public float getFloat() {
         int var1 = this.toNextNonWsp();
         this.allowcomma = true;
         int var2 = this.toNumberEnd();
         if (var1 < var2) {
            String var3 = this.svgpath.substring(var1, var2);

            try {
               return Float.parseFloat(var3);
            } catch (NumberFormatException var5) {
               throw new IllegalArgumentException("invalid float (" + var3 + ") in path at pos=" + var1);
            }
         } else {
            throw new IllegalArgumentException("end of path looking for float");
         }
      }

      public boolean b() {
         this.toNextNonWsp();
         this.allowcomma = true;
         if (this.pos < this.len) {
            char var1 = this.svgpath.charAt(this.pos);
            switch (var1) {
               case '0':
                  ++this.pos;
                  return false;
               case '1':
                  ++this.pos;
                  return true;
               default:
                  throw new IllegalArgumentException("invalid boolean flag (" + var1 + ") in path at pos=" + this.pos);
            }
         } else {
            throw new IllegalArgumentException("end of path looking for boolean");
         }
      }

      private int toNextNonWsp() {
         boolean var1 = this.allowcomma;

         while(this.pos < this.len) {
            switch (this.svgpath.charAt(this.pos)) {
               case ',':
                  if (!var1) {
                     return this.pos;
                  }

                  var1 = false;
               case '\t':
               case '\n':
               case '\r':
               case ' ':
                  ++this.pos;
                  break;
               default:
                  return this.pos;
            }
         }

         return this.pos;
      }

      private int toNumberEnd() {
         boolean var1 = true;
         boolean var2 = false;

         for(boolean var3 = false; this.pos < this.len; ++this.pos) {
            switch (this.svgpath.charAt(this.pos)) {
               case '+':
               case '-':
                  if (!var1) {
                     return this.pos;
                  }

                  var1 = false;
                  break;
               case ',':
               case '/':
               case ':':
               case ';':
               case '<':
               case '=':
               case '>':
               case '?':
               case '@':
               case 'A':
               case 'B':
               case 'C':
               case 'D':
               case 'F':
               case 'G':
               case 'H':
               case 'I':
               case 'J':
               case 'K':
               case 'L':
               case 'M':
               case 'N':
               case 'O':
               case 'P':
               case 'Q':
               case 'R':
               case 'S':
               case 'T':
               case 'U':
               case 'V':
               case 'W':
               case 'X':
               case 'Y':
               case 'Z':
               case '[':
               case '\\':
               case ']':
               case '^':
               case '_':
               case '`':
               case 'a':
               case 'b':
               case 'c':
               case 'd':
               default:
                  return this.pos;
               case '.':
                  if (!var2 && !var3) {
                     var3 = true;
                     var1 = false;
                     break;
                  }

                  return this.pos;
               case '0':
               case '1':
               case '2':
               case '3':
               case '4':
               case '5':
               case '6':
               case '7':
               case '8':
               case '9':
                  var1 = false;
                  break;
               case 'E':
               case 'e':
                  if (var2) {
                     return this.pos;
                  }

                  var1 = true;
                  var2 = true;
            }
         }

         return this.pos;
      }
   }

   static class TxIterator extends Iterator {
      float[] floatCoords;
      BaseTransform transform;

      TxIterator(Path2D var1, BaseTransform var2) {
         super(var1);
         this.floatCoords = var1.floatCoords;
         this.transform = var2;
      }

      public int currentSegment(float[] var1) {
         byte var2 = this.path.pointTypes[this.typeIdx];
         int var3 = Path2D.curvecoords[var2];
         if (var3 > 0) {
            this.transform.transform((float[])this.floatCoords, this.pointIdx, (float[])var1, 0, var3 / 2);
         }

         return var2;
      }

      public int currentSegment(double[] var1) {
         byte var2 = this.path.pointTypes[this.typeIdx];
         int var3 = Path2D.curvecoords[var2];
         if (var3 > 0) {
            this.transform.transform((float[])this.floatCoords, this.pointIdx, (double[])var1, 0, var3 / 2);
         }

         return var2;
      }
   }

   static class CopyIterator extends Iterator {
      float[] floatCoords;

      CopyIterator(Path2D var1) {
         super(var1);
         this.floatCoords = var1.floatCoords;
      }

      public int currentSegment(float[] var1) {
         byte var2 = this.path.pointTypes[this.typeIdx];
         int var3 = Path2D.curvecoords[var2];
         if (var3 > 0) {
            System.arraycopy(this.floatCoords, this.pointIdx, var1, 0, var3);
         }

         return var2;
      }

      public int currentSegment(double[] var1) {
         byte var2 = this.path.pointTypes[this.typeIdx];
         int var3 = Path2D.curvecoords[var2];
         if (var3 > 0) {
            for(int var4 = 0; var4 < var3; ++var4) {
               var1[var4] = (double)this.floatCoords[this.pointIdx + var4];
            }
         }

         return var2;
      }
   }

   public static enum CornerPrefix {
      CORNER_ONLY,
      MOVE_THEN_CORNER,
      LINE_THEN_CORNER;
   }
}
