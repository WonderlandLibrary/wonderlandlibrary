package com.sun.javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

public class TransformUtils {
   public static Transform immutableTransform(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22) {
      return new ImmutableTransform(var0, var2, var4, var6, var8, var10, var12, var14, var16, var18, var20, var22);
   }

   public static Transform immutableTransform(Transform var0) {
      return new ImmutableTransform(var0.getMxx(), var0.getMxy(), var0.getMxz(), var0.getTx(), var0.getMyx(), var0.getMyy(), var0.getMyz(), var0.getTy(), var0.getMzx(), var0.getMzy(), var0.getMzz(), var0.getTz());
   }

   public static Transform immutableTransform(Transform var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      if (var0 == null) {
         return new ImmutableTransform(var1, var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23);
      } else {
         ((ImmutableTransform)var0).setToTransform(var1, var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23);
         return var0;
      }
   }

   public static Transform immutableTransform(Transform var0, Transform var1) {
      return immutableTransform((ImmutableTransform)var0, var1.getMxx(), var1.getMxy(), var1.getMxz(), var1.getTx(), var1.getMyx(), var1.getMyy(), var1.getMyz(), var1.getTy(), var1.getMzx(), var1.getMzy(), var1.getMzz(), var1.getTz());
   }

   public static Transform immutableTransform(Transform var0, Transform var1, Transform var2) {
      if (var0 == null) {
         var0 = new ImmutableTransform();
      }

      ((ImmutableTransform)var0).setToConcatenation((ImmutableTransform)var1, (ImmutableTransform)var2);
      return (Transform)var0;
   }

   static class ImmutableTransform extends Transform {
      private static final int APPLY_IDENTITY = 0;
      private static final int APPLY_TRANSLATE = 1;
      private static final int APPLY_SCALE = 2;
      private static final int APPLY_SHEAR = 4;
      private static final int APPLY_NON_3D = 0;
      private static final int APPLY_3D_COMPLEX = 4;
      private transient int state2d;
      private transient int state3d;
      private double xx;
      private double xy;
      private double xz;
      private double yx;
      private double yy;
      private double yz;
      private double zx;
      private double zy;
      private double zz;
      private double xt;
      private double yt;
      private double zt;

      public ImmutableTransform() {
         this.xx = this.yy = this.zz = 1.0;
      }

      public ImmutableTransform(Transform var1) {
         this(var1.getMxx(), var1.getMxy(), var1.getMxz(), var1.getTx(), var1.getMyx(), var1.getMyy(), var1.getMyz(), var1.getTy(), var1.getMzx(), var1.getMzy(), var1.getMzz(), var1.getTz());
      }

      public ImmutableTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
         this.xx = var1;
         this.xy = var3;
         this.xz = var5;
         this.xt = var7;
         this.yx = var9;
         this.yy = var11;
         this.yz = var13;
         this.yt = var15;
         this.zx = var17;
         this.zy = var19;
         this.zz = var21;
         this.zt = var23;
         this.updateState();
      }

      private void setToTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
         this.xx = var1;
         this.xy = var3;
         this.xz = var5;
         this.xt = var7;
         this.yx = var9;
         this.yy = var11;
         this.yz = var13;
         this.yt = var15;
         this.zx = var17;
         this.zy = var19;
         this.zz = var21;
         this.zt = var23;
         this.updateState();
      }

      private void setToConcatenation(ImmutableTransform var1, ImmutableTransform var2) {
         if (var1.state3d == 0 && var2.state3d == 0) {
            this.xx = var1.xx * var2.xx + var1.xy * var2.yx;
            this.xy = var1.xx * var2.xy + var1.xy * var2.yy;
            this.xt = var1.xx * var2.xt + var1.xy * var2.yt + var1.xt;
            this.yx = var1.yx * var2.xx + var1.yy * var2.yx;
            this.yy = var1.yx * var2.xy + var1.yy * var2.yy;
            this.yt = var1.yx * var2.xt + var1.yy * var2.yt + var1.yt;
            if (this.state3d != 0) {
               this.xz = this.yz = this.zx = this.zy = this.zt = 0.0;
               this.zz = 1.0;
               this.state3d = 0;
            }

            this.updateState2D();
         } else {
            this.xx = var1.xx * var2.xx + var1.xy * var2.yx + var1.xz * var2.zx;
            this.xy = var1.xx * var2.xy + var1.xy * var2.yy + var1.xz * var2.zy;
            this.xz = var1.xx * var2.xz + var1.xy * var2.yz + var1.xz * var2.zz;
            this.xt = var1.xx * var2.xt + var1.xy * var2.yt + var1.xz * var2.zt + var1.xt;
            this.yx = var1.yx * var2.xx + var1.yy * var2.yx + var1.yz * var2.zx;
            this.yy = var1.yx * var2.xy + var1.yy * var2.yy + var1.yz * var2.zy;
            this.yz = var1.yx * var2.xz + var1.yy * var2.yz + var1.yz * var2.zz;
            this.yt = var1.yx * var2.xt + var1.yy * var2.yt + var1.yz * var2.zt + var1.yt;
            this.zx = var1.zx * var2.xx + var1.zy * var2.yx + var1.zz * var2.zx;
            this.zy = var1.zx * var2.xy + var1.zy * var2.yy + var1.zz * var2.zy;
            this.zz = var1.zx * var2.xz + var1.zy * var2.yz + var1.zz * var2.zz;
            this.zt = var1.zx * var2.xt + var1.zy * var2.yt + var1.zz * var2.zt + var1.zt;
            this.updateState();
         }

      }

      public double getMxx() {
         return this.xx;
      }

      public double getMxy() {
         return this.xy;
      }

      public double getMxz() {
         return this.xz;
      }

      public double getTx() {
         return this.xt;
      }

      public double getMyx() {
         return this.yx;
      }

      public double getMyy() {
         return this.yy;
      }

      public double getMyz() {
         return this.yz;
      }

      public double getTy() {
         return this.yt;
      }

      public double getMzx() {
         return this.zx;
      }

      public double getMzy() {
         return this.zy;
      }

      public double getMzz() {
         return this.zz;
      }

      public double getTz() {
         return this.zt;
      }

      public double determinant() {
         switch (this.state3d) {
            case 1:
               return 1.0;
            case 2:
            case 3:
               return this.xx * this.yy * this.zz;
            case 4:
               return this.xx * (this.yy * this.zz - this.zy * this.yz) + this.xy * (this.yz * this.zx - this.zz * this.yx) + this.xz * (this.yx * this.zy - this.zx * this.yy);
            default:
               stateError();
            case 0:
               switch (this.state2d) {
                  case 0:
                  case 1:
                     return 1.0;
                  case 2:
                  case 3:
                     return this.xx * this.yy;
                  case 4:
                  case 5:
                     return -(this.xy * this.yx);
                  default:
                     stateError();
                  case 6:
                  case 7:
                     return this.xx * this.yy - this.xy * this.yx;
               }
         }
      }

      public Transform createConcatenation(Transform var1) {
         Affine var2 = new Affine(this);
         var2.append(var1);
         return var2;
      }

      public Affine createInverse() throws NonInvertibleTransformException {
         Affine var1 = new Affine(this);
         var1.invert();
         return var1;
      }

      public Transform clone() {
         return new ImmutableTransform(this);
      }

      public Point2D transform(double var1, double var3) {
         this.ensureCanTransform2DPoint();
         switch (this.state2d) {
            case 0:
               return new Point2D(var1, var3);
            case 1:
               return new Point2D(var1 + this.xt, var3 + this.yt);
            case 2:
               return new Point2D(this.xx * var1, this.yy * var3);
            case 3:
               return new Point2D(this.xx * var1 + this.xt, this.yy * var3 + this.yt);
            case 4:
               return new Point2D(this.xy * var3, this.yx * var1);
            case 5:
               return new Point2D(this.xy * var3 + this.xt, this.yx * var1 + this.yt);
            case 6:
               return new Point2D(this.xx * var1 + this.xy * var3, this.yx * var1 + this.yy * var3);
            default:
               stateError();
            case 7:
               return new Point2D(this.xx * var1 + this.xy * var3 + this.xt, this.yx * var1 + this.yy * var3 + this.yt);
         }
      }

      public Point3D transform(double var1, double var3, double var5) {
         switch (this.state3d) {
            case 1:
               return new Point3D(var1 + this.xt, var3 + this.yt, var5 + this.zt);
            case 2:
               return new Point3D(this.xx * var1, this.yy * var3, this.zz * var5);
            case 3:
               return new Point3D(this.xx * var1 + this.xt, this.yy * var3 + this.yt, this.zz * var5 + this.zt);
            case 4:
               return new Point3D(this.xx * var1 + this.xy * var3 + this.xz * var5 + this.xt, this.yx * var1 + this.yy * var3 + this.yz * var5 + this.yt, this.zx * var1 + this.zy * var3 + this.zz * var5 + this.zt);
            default:
               stateError();
            case 0:
               switch (this.state2d) {
                  case 0:
                     return new Point3D(var1, var3, var5);
                  case 1:
                     return new Point3D(var1 + this.xt, var3 + this.yt, var5);
                  case 2:
                     return new Point3D(this.xx * var1, this.yy * var3, var5);
                  case 3:
                     return new Point3D(this.xx * var1 + this.xt, this.yy * var3 + this.yt, var5);
                  case 4:
                     return new Point3D(this.xy * var3, this.yx * var1, var5);
                  case 5:
                     return new Point3D(this.xy * var3 + this.xt, this.yx * var1 + this.yt, var5);
                  case 6:
                     return new Point3D(this.xx * var1 + this.xy * var3, this.yx * var1 + this.yy * var3, var5);
                  default:
                     stateError();
                  case 7:
                     return new Point3D(this.xx * var1 + this.xy * var3 + this.xt, this.yx * var1 + this.yy * var3 + this.yt, var5);
               }
         }
      }

      public Point2D deltaTransform(double var1, double var3) {
         this.ensureCanTransform2DPoint();
         switch (this.state2d) {
            case 0:
            case 1:
               return new Point2D(var1, var3);
            case 2:
            case 3:
               return new Point2D(this.xx * var1, this.yy * var3);
            case 4:
            case 5:
               return new Point2D(this.xy * var3, this.yx * var1);
            default:
               stateError();
            case 6:
            case 7:
               return new Point2D(this.xx * var1 + this.xy * var3, this.yx * var1 + this.yy * var3);
         }
      }

      public Point3D deltaTransform(double var1, double var3, double var5) {
         switch (this.state3d) {
            case 1:
               return new Point3D(var1, var3, var5);
            case 2:
            case 3:
               return new Point3D(this.xx * var1, this.yy * var3, this.zz * var5);
            case 4:
               return new Point3D(this.xx * var1 + this.xy * var3 + this.xz * var5, this.yx * var1 + this.yy * var3 + this.yz * var5, this.zx * var1 + this.zy * var3 + this.zz * var5);
            default:
               stateError();
            case 0:
               switch (this.state2d) {
                  case 0:
                  case 1:
                     return new Point3D(var1, var3, var5);
                  case 2:
                  case 3:
                     return new Point3D(this.xx * var1, this.yy * var3, var5);
                  case 4:
                  case 5:
                     return new Point3D(this.xy * var3, this.yx * var1, var5);
                  default:
                     stateError();
                  case 6:
                  case 7:
                     return new Point3D(this.xx * var1 + this.xy * var3, this.yx * var1 + this.yy * var3, var5);
               }
         }
      }

      public Point2D inverseTransform(double var1, double var3) throws NonInvertibleTransformException {
         this.ensureCanTransform2DPoint();
         switch (this.state2d) {
            case 0:
               return new Point2D(var1, var3);
            case 1:
               return new Point2D(var1 - this.xt, var3 - this.yt);
            case 2:
               if (this.xx != 0.0 && this.yy != 0.0) {
                  return new Point2D(1.0 / this.xx * var1, 1.0 / this.yy * var3);
               }

               throw new NonInvertibleTransformException("Determinant is 0");
            case 3:
               if (this.xx != 0.0 && this.yy != 0.0) {
                  return new Point2D(1.0 / this.xx * var1 - this.xt / this.xx, 1.0 / this.yy * var3 - this.yt / this.yy);
               }

               throw new NonInvertibleTransformException("Determinant is 0");
            case 4:
               if (this.xy != 0.0 && this.yx != 0.0) {
                  return new Point2D(1.0 / this.yx * var3, 1.0 / this.xy * var1);
               }

               throw new NonInvertibleTransformException("Determinant is 0");
            case 5:
               if (this.xy != 0.0 && this.yx != 0.0) {
                  return new Point2D(1.0 / this.yx * var3 - this.yt / this.yx, 1.0 / this.xy * var1 - this.xt / this.xy);
               }

               throw new NonInvertibleTransformException("Determinant is 0");
            default:
               return super.inverseTransform(var1, var3);
         }
      }

      public Point3D inverseTransform(double var1, double var3, double var5) throws NonInvertibleTransformException {
         switch (this.state3d) {
            case 1:
               return new Point3D(var1 - this.xt, var3 - this.yt, var5 - this.zt);
            case 2:
               if (this.xx != 0.0 && this.yy != 0.0 && this.zz != 0.0) {
                  return new Point3D(1.0 / this.xx * var1, 1.0 / this.yy * var3, 1.0 / this.zz * var5);
               }

               throw new NonInvertibleTransformException("Determinant is 0");
            case 3:
               if (this.xx != 0.0 && this.yy != 0.0 && this.zz != 0.0) {
                  return new Point3D(1.0 / this.xx * var1 - this.xt / this.xx, 1.0 / this.yy * var3 - this.yt / this.yy, 1.0 / this.zz * var5 - this.zt / this.zz);
               }

               throw new NonInvertibleTransformException("Determinant is 0");
            case 4:
               return super.inverseTransform(var1, var3, var5);
            default:
               stateError();
            case 0:
               switch (this.state2d) {
                  case 0:
                     return new Point3D(var1, var3, var5);
                  case 1:
                     return new Point3D(var1 - this.xt, var3 - this.yt, var5);
                  case 2:
                     if (this.xx != 0.0 && this.yy != 0.0) {
                        return new Point3D(1.0 / this.xx * var1, 1.0 / this.yy * var3, var5);
                     }

                     throw new NonInvertibleTransformException("Determinant is 0");
                  case 3:
                     if (this.xx != 0.0 && this.yy != 0.0) {
                        return new Point3D(1.0 / this.xx * var1 - this.xt / this.xx, 1.0 / this.yy * var3 - this.yt / this.yy, var5);
                     }

                     throw new NonInvertibleTransformException("Determinant is 0");
                  case 4:
                     if (this.xy != 0.0 && this.yx != 0.0) {
                        return new Point3D(1.0 / this.yx * var3, 1.0 / this.xy * var1, var5);
                     }

                     throw new NonInvertibleTransformException("Determinant is 0");
                  case 5:
                     if (this.xy != 0.0 && this.yx != 0.0) {
                        return new Point3D(1.0 / this.yx * var3 - this.yt / this.yx, 1.0 / this.xy * var1 - this.xt / this.xy, var5);
                     }

                     throw new NonInvertibleTransformException("Determinant is 0");
                  default:
                     return super.inverseTransform(var1, var3, var5);
               }
         }
      }

      public Point2D inverseDeltaTransform(double var1, double var3) throws NonInvertibleTransformException {
         this.ensureCanTransform2DPoint();
         switch (this.state2d) {
            case 0:
            case 1:
               return new Point2D(var1, var3);
            case 2:
            case 3:
               if (this.xx != 0.0 && this.yy != 0.0) {
                  return new Point2D(1.0 / this.xx * var1, 1.0 / this.yy * var3);
               }

               throw new NonInvertibleTransformException("Determinant is 0");
            case 4:
            case 5:
               if (this.xy != 0.0 && this.yx != 0.0) {
                  return new Point2D(1.0 / this.yx * var3, 1.0 / this.xy * var1);
               }

               throw new NonInvertibleTransformException("Determinant is 0");
            default:
               return super.inverseDeltaTransform(var1, var3);
         }
      }

      public Point3D inverseDeltaTransform(double var1, double var3, double var5) throws NonInvertibleTransformException {
         switch (this.state3d) {
            case 1:
               return new Point3D(var1, var3, var5);
            case 2:
            case 3:
               if (this.xx != 0.0 && this.yy != 0.0 && this.zz != 0.0) {
                  return new Point3D(1.0 / this.xx * var1, 1.0 / this.yy * var3, 1.0 / this.zz * var5);
               }

               throw new NonInvertibleTransformException("Determinant is 0");
            case 4:
               return super.inverseDeltaTransform(var1, var3, var5);
            default:
               stateError();
            case 0:
               switch (this.state2d) {
                  case 0:
                  case 1:
                     return new Point3D(var1, var3, var5);
                  case 2:
                  case 3:
                     if (this.xx != 0.0 && this.yy != 0.0) {
                        return new Point3D(1.0 / this.xx * var1, 1.0 / this.yy * var3, var5);
                     }

                     throw new NonInvertibleTransformException("Determinant is 0");
                  case 4:
                  case 5:
                     if (this.xy != 0.0 && this.yx != 0.0) {
                        return new Point3D(1.0 / this.yx * var3, 1.0 / this.xy * var1, var5);
                     }

                     throw new NonInvertibleTransformException("Determinant is 0");
                  default:
                     return super.inverseDeltaTransform(var1, var3, var5);
               }
         }
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("Transform [\n");
         var1.append("\t").append(this.xx);
         var1.append(", ").append(this.xy);
         var1.append(", ").append(this.xz);
         var1.append(", ").append(this.xt);
         var1.append('\n');
         var1.append("\t").append(this.yx);
         var1.append(", ").append(this.yy);
         var1.append(", ").append(this.yz);
         var1.append(", ").append(this.yt);
         var1.append('\n');
         var1.append("\t").append(this.zx);
         var1.append(", ").append(this.zy);
         var1.append(", ").append(this.zz);
         var1.append(", ").append(this.zt);
         return var1.append("\n]").toString();
      }

      private void updateState() {
         this.updateState2D();
         this.state3d = 0;
         if (this.xz == 0.0 && this.yz == 0.0 && this.zx == 0.0 && this.zy == 0.0) {
            if ((this.state2d & 4) == 0) {
               if (this.zt != 0.0) {
                  this.state3d |= 1;
               }

               if (this.zz != 1.0) {
                  this.state3d |= 2;
               }

               if (this.state3d != 0) {
                  this.state3d |= this.state2d & 3;
               }
            } else if (this.zz != 1.0 || this.zt != 0.0) {
               this.state3d = 4;
            }
         } else {
            this.state3d = 4;
         }

      }

      private void updateState2D() {
         if (this.xy == 0.0 && this.yx == 0.0) {
            if (this.xx == 1.0 && this.yy == 1.0) {
               if (this.xt == 0.0 && this.yt == 0.0) {
                  this.state2d = 0;
               } else {
                  this.state2d = 1;
               }
            } else if (this.xt == 0.0 && this.yt == 0.0) {
               this.state2d = 2;
            } else {
               this.state2d = 3;
            }
         } else if (this.xx == 0.0 && this.yy == 0.0) {
            if (this.xt == 0.0 && this.yt == 0.0) {
               this.state2d = 4;
            } else {
               this.state2d = 5;
            }
         } else if (this.xt == 0.0 && this.yt == 0.0) {
            this.state2d = 6;
         } else {
            this.state2d = 7;
         }

      }

      void ensureCanTransform2DPoint() throws IllegalStateException {
         if (this.state3d != 0) {
            throw new IllegalStateException("Cannot transform 2D point with a 3D transform");
         }
      }

      private static void stateError() {
         throw new InternalError("missing case in a switch");
      }

      /** @deprecated */
      @Deprecated
      public void impl_apply(Affine3D var1) {
         var1.concatenate(this.xx, this.xy, this.xz, this.xt, this.yx, this.yy, this.yz, this.yt, this.zx, this.zy, this.zz, this.zt);
      }

      /** @deprecated */
      @Deprecated
      public BaseTransform impl_derive(BaseTransform var1) {
         return var1.deriveWithConcatenation(this.xx, this.xy, this.xz, this.xt, this.yx, this.yy, this.yz, this.yt, this.zx, this.zy, this.zz, this.zt);
      }

      int getState2d() {
         return this.state2d;
      }

      int getState3d() {
         return this.state3d;
      }
   }
}
