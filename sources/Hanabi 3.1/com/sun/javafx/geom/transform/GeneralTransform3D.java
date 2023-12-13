package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.Vec3f;

public final class GeneralTransform3D implements CanTransformVec3d {
   protected double[] mat = new double[16];
   private boolean identity;
   private Vec3d tempV3d;
   private static final double EPSILON_ABSOLUTE = 1.0E-5;

   public GeneralTransform3D() {
      this.setIdentity();
   }

   public boolean isAffine() {
      return !this.isInfOrNaN() && almostZero(this.mat[12]) && almostZero(this.mat[13]) && almostZero(this.mat[14]) && almostOne(this.mat[15]);
   }

   public GeneralTransform3D set(GeneralTransform3D var1) {
      System.arraycopy(var1.mat, 0, this.mat, 0, this.mat.length);
      this.updateState();
      return this;
   }

   public GeneralTransform3D set(double[] var1) {
      System.arraycopy(var1, 0, this.mat, 0, this.mat.length);
      this.updateState();
      return this;
   }

   public double[] get(double[] var1) {
      if (var1 == null) {
         var1 = new double[this.mat.length];
      }

      System.arraycopy(this.mat, 0, var1, 0, this.mat.length);
      return var1;
   }

   public double get(int var1) {
      assert var1 >= 0 && var1 < this.mat.length;

      return this.mat[var1];
   }

   public BaseBounds transform(BaseBounds var1, BaseBounds var2) {
      if (this.tempV3d == null) {
         this.tempV3d = new Vec3d();
      }

      return TransformHelper.general3dBoundsTransform(this, var1, var2, this.tempV3d);
   }

   public Point2D transform(Point2D var1, Point2D var2) {
      if (var2 == null) {
         var2 = new Point2D();
      }

      double var3 = (double)((float)(this.mat[12] * (double)var1.x + this.mat[13] * (double)var1.y + this.mat[15]));
      var2.x = (float)(this.mat[0] * (double)var1.x + this.mat[1] * (double)var1.y + this.mat[3]);
      var2.y = (float)(this.mat[4] * (double)var1.x + this.mat[5] * (double)var1.y + this.mat[7]);
      var2.x = (float)((double)var2.x / var3);
      var2.y = (float)((double)var2.y / var3);
      return var2;
   }

   public Vec3d transform(Vec3d var1, Vec3d var2) {
      if (var2 == null) {
         var2 = new Vec3d();
      }

      double var3 = (double)((float)(this.mat[12] * var1.x + this.mat[13] * var1.y + this.mat[14] * var1.z + this.mat[15]));
      var2.x = (double)((float)(this.mat[0] * var1.x + this.mat[1] * var1.y + this.mat[2] * var1.z + this.mat[3]));
      var2.y = (double)((float)(this.mat[4] * var1.x + this.mat[5] * var1.y + this.mat[6] * var1.z + this.mat[7]));
      var2.z = (double)((float)(this.mat[8] * var1.x + this.mat[9] * var1.y + this.mat[10] * var1.z + this.mat[11]));
      if (var3 != 0.0) {
         var2.x /= var3;
         var2.y /= var3;
         var2.z /= var3;
      }

      return var2;
   }

   public Vec3d transform(Vec3d var1) {
      return this.transform(var1, var1);
   }

   public Vec3f transformNormal(Vec3f var1, Vec3f var2) {
      var1.x = (float)(this.mat[0] * (double)var1.x + this.mat[1] * (double)var1.y + this.mat[2] * (double)var1.z);
      var1.y = (float)(this.mat[4] * (double)var1.x + this.mat[5] * (double)var1.y + this.mat[6] * (double)var1.z);
      var1.z = (float)(this.mat[8] * (double)var1.x + this.mat[9] * (double)var1.y + this.mat[10] * (double)var1.z);
      return var2;
   }

   public Vec3f transformNormal(Vec3f var1) {
      return this.transformNormal(var1, var1);
   }

   public GeneralTransform3D perspective(boolean var1, double var2, double var4, double var6, double var8) {
      double var16 = var2 * 0.5;
      double var14 = var8 - var6;
      double var10 = Math.sin(var16);
      double var12 = Math.cos(var16) / var10;
      this.mat[0] = var1 ? var12 / var4 : var12;
      this.mat[5] = var1 ? var12 : var12 * var4;
      this.mat[10] = -(var8 + var6) / var14;
      this.mat[11] = -2.0 * var6 * var8 / var14;
      this.mat[14] = -1.0;
      this.mat[1] = this.mat[2] = this.mat[3] = this.mat[4] = this.mat[6] = this.mat[7] = this.mat[8] = this.mat[9] = this.mat[12] = this.mat[13] = this.mat[15] = 0.0;
      this.updateState();
      return this;
   }

   public GeneralTransform3D ortho(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = 1.0 / (var3 - var1);
      double var15 = 1.0 / (var7 - var5);
      double var17 = 1.0 / (var11 - var9);
      this.mat[0] = 2.0 * var13;
      this.mat[3] = -(var3 + var1) * var13;
      this.mat[5] = 2.0 * var15;
      this.mat[7] = -(var7 + var5) * var15;
      this.mat[10] = 2.0 * var17;
      this.mat[11] = (var11 + var9) * var17;
      this.mat[1] = this.mat[2] = this.mat[4] = this.mat[6] = this.mat[8] = this.mat[9] = this.mat[12] = this.mat[13] = this.mat[14] = 0.0;
      this.mat[15] = 1.0;
      this.updateState();
      return this;
   }

   public double computeClipZCoord() {
      double var1 = (1.0 - this.mat[15]) / this.mat[14];
      double var3 = this.mat[10] * var1 + this.mat[11];
      return var3;
   }

   public double determinant() {
      return this.mat[0] * (this.mat[5] * (this.mat[10] * this.mat[15] - this.mat[11] * this.mat[14]) - this.mat[6] * (this.mat[9] * this.mat[15] - this.mat[11] * this.mat[13]) + this.mat[7] * (this.mat[9] * this.mat[14] - this.mat[10] * this.mat[13])) - this.mat[1] * (this.mat[4] * (this.mat[10] * this.mat[15] - this.mat[11] * this.mat[14]) - this.mat[6] * (this.mat[8] * this.mat[15] - this.mat[11] * this.mat[12]) + this.mat[7] * (this.mat[8] * this.mat[14] - this.mat[10] * this.mat[12])) + this.mat[2] * (this.mat[4] * (this.mat[9] * this.mat[15] - this.mat[11] * this.mat[13]) - this.mat[5] * (this.mat[8] * this.mat[15] - this.mat[11] * this.mat[12]) + this.mat[7] * (this.mat[8] * this.mat[13] - this.mat[9] * this.mat[12])) - this.mat[3] * (this.mat[4] * (this.mat[9] * this.mat[14] - this.mat[10] * this.mat[13]) - this.mat[5] * (this.mat[8] * this.mat[14] - this.mat[10] * this.mat[12]) + this.mat[6] * (this.mat[8] * this.mat[13] - this.mat[9] * this.mat[12]));
   }

   public GeneralTransform3D invert() {
      return this.invert(this);
   }

   private GeneralTransform3D invert(GeneralTransform3D var1) {
      double[] var2 = new double[16];
      int[] var3 = new int[4];
      System.arraycopy(var1.mat, 0, var2, 0, var2.length);
      if (!luDecomposition(var2, var3)) {
         throw new SingularMatrixException();
      } else {
         this.mat[0] = 1.0;
         this.mat[1] = 0.0;
         this.mat[2] = 0.0;
         this.mat[3] = 0.0;
         this.mat[4] = 0.0;
         this.mat[5] = 1.0;
         this.mat[6] = 0.0;
         this.mat[7] = 0.0;
         this.mat[8] = 0.0;
         this.mat[9] = 0.0;
         this.mat[10] = 1.0;
         this.mat[11] = 0.0;
         this.mat[12] = 0.0;
         this.mat[13] = 0.0;
         this.mat[14] = 0.0;
         this.mat[15] = 1.0;
         luBacksubstitution(var2, var3, this.mat);
         this.updateState();
         return this;
      }
   }

   private static boolean luDecomposition(double[] var0, int[] var1) {
      double[] var2 = new double[4];
      int var5 = 0;
      int var6 = 0;

      int var3;
      double var7;
      for(var3 = 4; var3-- != 0; var2[var6++] = 1.0 / var7) {
         var7 = 0.0;
         int var4 = 4;

         while(var4-- != 0) {
            double var9 = var0[var5++];
            var9 = Math.abs(var9);
            if (var9 > var7) {
               var7 = var9;
            }
         }

         if (var7 == 0.0) {
            return false;
         }
      }

      byte var17 = 0;

      for(var3 = 0; var3 < 4; ++var3) {
         int var8;
         int var10;
         double var11;
         int var18;
         int var19;
         for(var5 = 0; var5 < var3; ++var5) {
            var8 = var17 + 4 * var5 + var3;
            var11 = var0[var8];
            var18 = var5;
            var19 = var17 + 4 * var5;

            for(var10 = var17 + var3; var18-- != 0; var10 += 4) {
               var11 -= var0[var19] * var0[var10];
               ++var19;
            }

            var0[var8] = var11;
         }

         double var13 = 0.0;
         var6 = -1;

         double var15;
         for(var5 = var3; var5 < 4; ++var5) {
            var8 = var17 + 4 * var5 + var3;
            var11 = var0[var8];
            var18 = var3;
            var19 = var17 + 4 * var5;

            for(var10 = var17 + var3; var18-- != 0; var10 += 4) {
               var11 -= var0[var19] * var0[var10];
               ++var19;
            }

            var0[var8] = var11;
            if ((var15 = var2[var5] * Math.abs(var11)) >= var13) {
               var13 = var15;
               var6 = var5;
            }
         }

         if (var6 < 0) {
            return false;
         }

         if (var3 != var6) {
            var18 = 4;
            var19 = var17 + 4 * var6;

            for(var10 = var17 + 4 * var3; var18-- != 0; var0[var10++] = var15) {
               var15 = var0[var19];
               var0[var19++] = var0[var10];
            }

            var2[var6] = var2[var3];
         }

         var1[var3] = var6;
         if (var0[var17 + 4 * var3 + var3] == 0.0) {
            return false;
         }

         if (var3 != 3) {
            var15 = 1.0 / var0[var17 + 4 * var3 + var3];
            var8 = var17 + 4 * (var3 + 1) + var3;

            for(var5 = 3 - var3; var5-- != 0; var8 += 4) {
               var0[var8] *= var15;
            }
         }
      }

      return true;
   }

   private static void luBacksubstitution(double[] var0, int[] var1, double[] var2) {
      byte var8 = 0;

      for(int var7 = 0; var7 < 4; ++var7) {
         int var9 = var7;
         int var4 = -1;

         int var10;
         for(int var3 = 0; var3 < 4; ++var3) {
            int var5 = var1[var8 + var3];
            double var11 = var2[var9 + 4 * var5];
            var2[var9 + 4 * var5] = var2[var9 + 4 * var3];
            if (var4 >= 0) {
               var10 = var3 * 4;

               for(int var6 = var4; var6 <= var3 - 1; ++var6) {
                  var11 -= var0[var10 + var6] * var2[var9 + 4 * var6];
               }
            } else if (var11 != 0.0) {
               var4 = var3;
            }

            var2[var9 + 4 * var3] = var11;
         }

         byte var13 = 12;
         var2[var9 + 12] /= var0[var13 + 3];
         var10 = var13 - 4;
         var2[var9 + 8] = (var2[var9 + 8] - var0[var10 + 3] * var2[var9 + 12]) / var0[var10 + 2];
         var10 -= 4;
         var2[var9 + 4] = (var2[var9 + 4] - var0[var10 + 2] * var2[var9 + 8] - var0[var10 + 3] * var2[var9 + 12]) / var0[var10 + 1];
         var10 -= 4;
         var2[var9 + 0] = (var2[var9 + 0] - var0[var10 + 1] * var2[var9 + 4] - var0[var10 + 2] * var2[var9 + 8] - var0[var10 + 3] * var2[var9 + 12]) / var0[var10 + 0];
      }

   }

   public GeneralTransform3D mul(BaseTransform var1) {
      if (var1.isIdentity()) {
         return this;
      } else {
         double var34 = var1.getMxx();
         double var36 = var1.getMxy();
         double var38 = var1.getMxz();
         double var40 = var1.getMxt();
         double var42 = var1.getMyx();
         double var44 = var1.getMyy();
         double var46 = var1.getMyz();
         double var48 = var1.getMyt();
         double var50 = var1.getMzx();
         double var52 = var1.getMzy();
         double var54 = var1.getMzz();
         double var56 = var1.getMzt();
         double var2 = this.mat[0] * var34 + this.mat[1] * var42 + this.mat[2] * var50;
         double var4 = this.mat[0] * var36 + this.mat[1] * var44 + this.mat[2] * var52;
         double var6 = this.mat[0] * var38 + this.mat[1] * var46 + this.mat[2] * var54;
         double var8 = this.mat[0] * var40 + this.mat[1] * var48 + this.mat[2] * var56 + this.mat[3];
         double var10 = this.mat[4] * var34 + this.mat[5] * var42 + this.mat[6] * var50;
         double var12 = this.mat[4] * var36 + this.mat[5] * var44 + this.mat[6] * var52;
         double var14 = this.mat[4] * var38 + this.mat[5] * var46 + this.mat[6] * var54;
         double var16 = this.mat[4] * var40 + this.mat[5] * var48 + this.mat[6] * var56 + this.mat[7];
         double var18 = this.mat[8] * var34 + this.mat[9] * var42 + this.mat[10] * var50;
         double var20 = this.mat[8] * var36 + this.mat[9] * var44 + this.mat[10] * var52;
         double var22 = this.mat[8] * var38 + this.mat[9] * var46 + this.mat[10] * var54;
         double var24 = this.mat[8] * var40 + this.mat[9] * var48 + this.mat[10] * var56 + this.mat[11];
         double var26;
         double var28;
         double var30;
         double var32;
         if (this.isAffine()) {
            var30 = 0.0;
            var28 = 0.0;
            var26 = 0.0;
            var32 = 1.0;
         } else {
            var26 = this.mat[12] * var34 + this.mat[13] * var42 + this.mat[14] * var50;
            var28 = this.mat[12] * var36 + this.mat[13] * var44 + this.mat[14] * var52;
            var30 = this.mat[12] * var38 + this.mat[13] * var46 + this.mat[14] * var54;
            var32 = this.mat[12] * var40 + this.mat[13] * var48 + this.mat[14] * var56 + this.mat[15];
         }

         this.mat[0] = var2;
         this.mat[1] = var4;
         this.mat[2] = var6;
         this.mat[3] = var8;
         this.mat[4] = var10;
         this.mat[5] = var12;
         this.mat[6] = var14;
         this.mat[7] = var16;
         this.mat[8] = var18;
         this.mat[9] = var20;
         this.mat[10] = var22;
         this.mat[11] = var24;
         this.mat[12] = var26;
         this.mat[13] = var28;
         this.mat[14] = var30;
         this.mat[15] = var32;
         this.updateState();
         return this;
      }
   }

   public GeneralTransform3D scale(double var1, double var3, double var5) {
      boolean var7 = false;
      double[] var10000;
      if (var1 != 1.0) {
         var10000 = this.mat;
         var10000[0] *= var1;
         var10000 = this.mat;
         var10000[4] *= var1;
         var10000 = this.mat;
         var10000[8] *= var1;
         var10000 = this.mat;
         var10000[12] *= var1;
         var7 = true;
      }

      if (var3 != 1.0) {
         var10000 = this.mat;
         var10000[1] *= var3;
         var10000 = this.mat;
         var10000[5] *= var3;
         var10000 = this.mat;
         var10000[9] *= var3;
         var10000 = this.mat;
         var10000[13] *= var3;
         var7 = true;
      }

      if (var5 != 1.0) {
         var10000 = this.mat;
         var10000[2] *= var5;
         var10000 = this.mat;
         var10000[6] *= var5;
         var10000 = this.mat;
         var10000[10] *= var5;
         var10000 = this.mat;
         var10000[14] *= var5;
         var7 = true;
      }

      if (var7) {
         this.updateState();
      }

      return this;
   }

   public GeneralTransform3D mul(GeneralTransform3D var1) {
      if (var1.isIdentity()) {
         return this;
      } else {
         double var32;
         double var2;
         double var4;
         double var6;
         double var8;
         double var10;
         double var12;
         double var14;
         double var16;
         double var18;
         double var20;
         double var22;
         double var24;
         double var26;
         double var28;
         double var30;
         if (var1.isAffine()) {
            var2 = this.mat[0] * var1.mat[0] + this.mat[1] * var1.mat[4] + this.mat[2] * var1.mat[8];
            var4 = this.mat[0] * var1.mat[1] + this.mat[1] * var1.mat[5] + this.mat[2] * var1.mat[9];
            var6 = this.mat[0] * var1.mat[2] + this.mat[1] * var1.mat[6] + this.mat[2] * var1.mat[10];
            var8 = this.mat[0] * var1.mat[3] + this.mat[1] * var1.mat[7] + this.mat[2] * var1.mat[11] + this.mat[3];
            var10 = this.mat[4] * var1.mat[0] + this.mat[5] * var1.mat[4] + this.mat[6] * var1.mat[8];
            var12 = this.mat[4] * var1.mat[1] + this.mat[5] * var1.mat[5] + this.mat[6] * var1.mat[9];
            var14 = this.mat[4] * var1.mat[2] + this.mat[5] * var1.mat[6] + this.mat[6] * var1.mat[10];
            var16 = this.mat[4] * var1.mat[3] + this.mat[5] * var1.mat[7] + this.mat[6] * var1.mat[11] + this.mat[7];
            var18 = this.mat[8] * var1.mat[0] + this.mat[9] * var1.mat[4] + this.mat[10] * var1.mat[8];
            var20 = this.mat[8] * var1.mat[1] + this.mat[9] * var1.mat[5] + this.mat[10] * var1.mat[9];
            var22 = this.mat[8] * var1.mat[2] + this.mat[9] * var1.mat[6] + this.mat[10] * var1.mat[10];
            var24 = this.mat[8] * var1.mat[3] + this.mat[9] * var1.mat[7] + this.mat[10] * var1.mat[11] + this.mat[11];
            if (this.isAffine()) {
               var30 = 0.0;
               var28 = 0.0;
               var26 = 0.0;
               var32 = 1.0;
            } else {
               var26 = this.mat[12] * var1.mat[0] + this.mat[13] * var1.mat[4] + this.mat[14] * var1.mat[8];
               var28 = this.mat[12] * var1.mat[1] + this.mat[13] * var1.mat[5] + this.mat[14] * var1.mat[9];
               var30 = this.mat[12] * var1.mat[2] + this.mat[13] * var1.mat[6] + this.mat[14] * var1.mat[10];
               var32 = this.mat[12] * var1.mat[3] + this.mat[13] * var1.mat[7] + this.mat[14] * var1.mat[11] + this.mat[15];
            }
         } else {
            var2 = this.mat[0] * var1.mat[0] + this.mat[1] * var1.mat[4] + this.mat[2] * var1.mat[8] + this.mat[3] * var1.mat[12];
            var4 = this.mat[0] * var1.mat[1] + this.mat[1] * var1.mat[5] + this.mat[2] * var1.mat[9] + this.mat[3] * var1.mat[13];
            var6 = this.mat[0] * var1.mat[2] + this.mat[1] * var1.mat[6] + this.mat[2] * var1.mat[10] + this.mat[3] * var1.mat[14];
            var8 = this.mat[0] * var1.mat[3] + this.mat[1] * var1.mat[7] + this.mat[2] * var1.mat[11] + this.mat[3] * var1.mat[15];
            var10 = this.mat[4] * var1.mat[0] + this.mat[5] * var1.mat[4] + this.mat[6] * var1.mat[8] + this.mat[7] * var1.mat[12];
            var12 = this.mat[4] * var1.mat[1] + this.mat[5] * var1.mat[5] + this.mat[6] * var1.mat[9] + this.mat[7] * var1.mat[13];
            var14 = this.mat[4] * var1.mat[2] + this.mat[5] * var1.mat[6] + this.mat[6] * var1.mat[10] + this.mat[7] * var1.mat[14];
            var16 = this.mat[4] * var1.mat[3] + this.mat[5] * var1.mat[7] + this.mat[6] * var1.mat[11] + this.mat[7] * var1.mat[15];
            var18 = this.mat[8] * var1.mat[0] + this.mat[9] * var1.mat[4] + this.mat[10] * var1.mat[8] + this.mat[11] * var1.mat[12];
            var20 = this.mat[8] * var1.mat[1] + this.mat[9] * var1.mat[5] + this.mat[10] * var1.mat[9] + this.mat[11] * var1.mat[13];
            var22 = this.mat[8] * var1.mat[2] + this.mat[9] * var1.mat[6] + this.mat[10] * var1.mat[10] + this.mat[11] * var1.mat[14];
            var24 = this.mat[8] * var1.mat[3] + this.mat[9] * var1.mat[7] + this.mat[10] * var1.mat[11] + this.mat[11] * var1.mat[15];
            if (this.isAffine()) {
               var26 = var1.mat[12];
               var28 = var1.mat[13];
               var30 = var1.mat[14];
               var32 = var1.mat[15];
            } else {
               var26 = this.mat[12] * var1.mat[0] + this.mat[13] * var1.mat[4] + this.mat[14] * var1.mat[8] + this.mat[15] * var1.mat[12];
               var28 = this.mat[12] * var1.mat[1] + this.mat[13] * var1.mat[5] + this.mat[14] * var1.mat[9] + this.mat[15] * var1.mat[13];
               var30 = this.mat[12] * var1.mat[2] + this.mat[13] * var1.mat[6] + this.mat[14] * var1.mat[10] + this.mat[15] * var1.mat[14];
               var32 = this.mat[12] * var1.mat[3] + this.mat[13] * var1.mat[7] + this.mat[14] * var1.mat[11] + this.mat[15] * var1.mat[15];
            }
         }

         this.mat[0] = var2;
         this.mat[1] = var4;
         this.mat[2] = var6;
         this.mat[3] = var8;
         this.mat[4] = var10;
         this.mat[5] = var12;
         this.mat[6] = var14;
         this.mat[7] = var16;
         this.mat[8] = var18;
         this.mat[9] = var20;
         this.mat[10] = var22;
         this.mat[11] = var24;
         this.mat[12] = var26;
         this.mat[13] = var28;
         this.mat[14] = var30;
         this.mat[15] = var32;
         this.updateState();
         return this;
      }
   }

   public GeneralTransform3D setIdentity() {
      this.mat[0] = 1.0;
      this.mat[1] = 0.0;
      this.mat[2] = 0.0;
      this.mat[3] = 0.0;
      this.mat[4] = 0.0;
      this.mat[5] = 1.0;
      this.mat[6] = 0.0;
      this.mat[7] = 0.0;
      this.mat[8] = 0.0;
      this.mat[9] = 0.0;
      this.mat[10] = 1.0;
      this.mat[11] = 0.0;
      this.mat[12] = 0.0;
      this.mat[13] = 0.0;
      this.mat[14] = 0.0;
      this.mat[15] = 1.0;
      this.identity = true;
      return this;
   }

   public boolean isIdentity() {
      return this.identity;
   }

   private void updateState() {
      this.identity = this.mat[0] == 1.0 && this.mat[5] == 1.0 && this.mat[10] == 1.0 && this.mat[15] == 1.0 && this.mat[1] == 0.0 && this.mat[2] == 0.0 && this.mat[3] == 0.0 && this.mat[4] == 0.0 && this.mat[6] == 0.0 && this.mat[7] == 0.0 && this.mat[8] == 0.0 && this.mat[9] == 0.0 && this.mat[11] == 0.0 && this.mat[12] == 0.0 && this.mat[13] == 0.0 && this.mat[14] == 0.0;
   }

   boolean isInfOrNaN() {
      double var1 = 0.0;

      for(int var3 = 0; var3 < this.mat.length; ++var3) {
         var1 *= this.mat[var3];
      }

      return var1 != 0.0;
   }

   static boolean almostZero(double var0) {
      return var0 < 1.0E-5 && var0 > -1.0E-5;
   }

   static boolean almostOne(double var0) {
      return var0 < 1.00001 && var0 > 0.99999;
   }

   public GeneralTransform3D copy() {
      GeneralTransform3D var1 = new GeneralTransform3D();
      var1.set(this);
      return var1;
   }

   public String toString() {
      return this.mat[0] + ", " + this.mat[1] + ", " + this.mat[2] + ", " + this.mat[3] + "\n" + this.mat[4] + ", " + this.mat[5] + ", " + this.mat[6] + ", " + this.mat[7] + "\n" + this.mat[8] + ", " + this.mat[9] + ", " + this.mat[10] + ", " + this.mat[11] + "\n" + this.mat[12] + ", " + this.mat[13] + ", " + this.mat[14] + ", " + this.mat[15] + "\n";
   }
}
