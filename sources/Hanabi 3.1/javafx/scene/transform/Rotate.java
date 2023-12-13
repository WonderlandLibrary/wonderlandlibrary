package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class Rotate extends Transform {
   public static final Point3D X_AXIS = new Point3D(1.0, 0.0, 0.0);
   public static final Point3D Y_AXIS = new Point3D(0.0, 1.0, 0.0);
   public static final Point3D Z_AXIS = new Point3D(0.0, 0.0, 1.0);
   private MatrixCache cache;
   private MatrixCache inverseCache;
   private DoubleProperty angle;
   private DoubleProperty pivotX;
   private DoubleProperty pivotY;
   private DoubleProperty pivotZ;
   private ObjectProperty axis;

   public Rotate() {
   }

   public Rotate(double var1) {
      this.setAngle(var1);
   }

   public Rotate(double var1, Point3D var3) {
      this.setAngle(var1);
      this.setAxis(var3);
   }

   public Rotate(double var1, double var3, double var5) {
      this.setAngle(var1);
      this.setPivotX(var3);
      this.setPivotY(var5);
   }

   public Rotate(double var1, double var3, double var5, double var7) {
      this(var1, var3, var5);
      this.setPivotZ(var7);
   }

   public Rotate(double var1, double var3, double var5, double var7, Point3D var9) {
      this(var1, var3, var5);
      this.setPivotZ(var7);
      this.setAxis(var9);
   }

   public final void setAngle(double var1) {
      this.angleProperty().set(var1);
   }

   public final double getAngle() {
      return this.angle == null ? 0.0 : this.angle.get();
   }

   public final DoubleProperty angleProperty() {
      if (this.angle == null) {
         this.angle = new DoublePropertyBase() {
            public void invalidated() {
               Rotate.this.transformChanged();
            }

            public Object getBean() {
               return Rotate.this;
            }

            public String getName() {
               return "angle";
            }
         };
      }

      return this.angle;
   }

   public final void setPivotX(double var1) {
      this.pivotXProperty().set(var1);
   }

   public final double getPivotX() {
      return this.pivotX == null ? 0.0 : this.pivotX.get();
   }

   public final DoubleProperty pivotXProperty() {
      if (this.pivotX == null) {
         this.pivotX = new DoublePropertyBase() {
            public void invalidated() {
               Rotate.this.transformChanged();
            }

            public Object getBean() {
               return Rotate.this;
            }

            public String getName() {
               return "pivotX";
            }
         };
      }

      return this.pivotX;
   }

   public final void setPivotY(double var1) {
      this.pivotYProperty().set(var1);
   }

   public final double getPivotY() {
      return this.pivotY == null ? 0.0 : this.pivotY.get();
   }

   public final DoubleProperty pivotYProperty() {
      if (this.pivotY == null) {
         this.pivotY = new DoublePropertyBase() {
            public void invalidated() {
               Rotate.this.transformChanged();
            }

            public Object getBean() {
               return Rotate.this;
            }

            public String getName() {
               return "pivotY";
            }
         };
      }

      return this.pivotY;
   }

   public final void setPivotZ(double var1) {
      this.pivotZProperty().set(var1);
   }

   public final double getPivotZ() {
      return this.pivotZ == null ? 0.0 : this.pivotZ.get();
   }

   public final DoubleProperty pivotZProperty() {
      if (this.pivotZ == null) {
         this.pivotZ = new DoublePropertyBase() {
            public void invalidated() {
               Rotate.this.transformChanged();
            }

            public Object getBean() {
               return Rotate.this;
            }

            public String getName() {
               return "pivotZ";
            }
         };
      }

      return this.pivotZ;
   }

   public final void setAxis(Point3D var1) {
      this.axisProperty().set(var1);
   }

   public final Point3D getAxis() {
      return this.axis == null ? Z_AXIS : (Point3D)this.axis.get();
   }

   public final ObjectProperty axisProperty() {
      if (this.axis == null) {
         this.axis = new ObjectPropertyBase(Z_AXIS) {
            public void invalidated() {
               Rotate.this.transformChanged();
            }

            public Object getBean() {
               return Rotate.this;
            }

            public String getName() {
               return "axis";
            }
         };
      }

      return this.axis;
   }

   public double getMxx() {
      this.updateCache();
      return this.cache.mxx;
   }

   public double getMxy() {
      this.updateCache();
      return this.cache.mxy;
   }

   public double getMxz() {
      this.updateCache();
      return this.cache.mxz;
   }

   public double getTx() {
      this.updateCache();
      return this.cache.tx;
   }

   public double getMyx() {
      this.updateCache();
      return this.cache.myx;
   }

   public double getMyy() {
      this.updateCache();
      return this.cache.myy;
   }

   public double getMyz() {
      this.updateCache();
      return this.cache.myz;
   }

   public double getTy() {
      this.updateCache();
      return this.cache.ty;
   }

   public double getMzx() {
      this.updateCache();
      return this.cache.mzx;
   }

   public double getMzy() {
      this.updateCache();
      return this.cache.mzy;
   }

   public double getMzz() {
      this.updateCache();
      return this.cache.mzz;
   }

   public double getTz() {
      this.updateCache();
      return this.cache.tz;
   }

   boolean computeIs2D() {
      Point3D var1 = this.getAxis();
      return var1.getX() == 0.0 && var1.getY() == 0.0 || this.getAngle() == 0.0;
   }

   boolean computeIsIdentity() {
      if (this.getAngle() == 0.0) {
         return true;
      } else {
         Point3D var1 = this.getAxis();
         return var1.getX() == 0.0 && var1.getY() == 0.0 && var1.getZ() == 0.0;
      }
   }

   void fill2DArray(double[] var1) {
      this.updateCache();
      var1[0] = this.cache.mxx;
      var1[1] = this.cache.mxy;
      var1[2] = this.cache.tx;
      var1[3] = this.cache.myx;
      var1[4] = this.cache.myy;
      var1[5] = this.cache.ty;
   }

   void fill3DArray(double[] var1) {
      this.updateCache();
      var1[0] = this.cache.mxx;
      var1[1] = this.cache.mxy;
      var1[2] = this.cache.mxz;
      var1[3] = this.cache.tx;
      var1[4] = this.cache.myx;
      var1[5] = this.cache.myy;
      var1[6] = this.cache.myz;
      var1[7] = this.cache.ty;
      var1[8] = this.cache.mzx;
      var1[9] = this.cache.mzy;
      var1[10] = this.cache.mzz;
      var1[11] = this.cache.tz;
   }

   public Transform createConcatenation(Transform var1) {
      if (var1 instanceof Rotate) {
         Rotate var2 = (Rotate)var1;
         double var3 = this.getPivotX();
         double var5 = this.getPivotY();
         double var7 = this.getPivotZ();
         if ((var2.getAxis() == this.getAxis() || var2.getAxis().normalize().equals(this.getAxis().normalize())) && var3 == var2.getPivotX() && var5 == var2.getPivotY() && var7 == var2.getPivotZ()) {
            return new Rotate(this.getAngle() + var2.getAngle(), var3, var5, var7, this.getAxis());
         }
      }

      if (var1 instanceof Affine) {
         Affine var9 = (Affine)var1.clone();
         var9.prepend(this);
         return var9;
      } else {
         return super.createConcatenation(var1);
      }
   }

   public Transform createInverse() throws NonInvertibleTransformException {
      return new Rotate(-this.getAngle(), this.getPivotX(), this.getPivotY(), this.getPivotZ(), this.getAxis());
   }

   public Rotate clone() {
      return new Rotate(this.getAngle(), this.getPivotX(), this.getPivotY(), this.getPivotZ(), this.getAxis());
   }

   public Point2D transform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      this.updateCache();
      return new Point2D(this.cache.mxx * var1 + this.cache.mxy * var3 + this.cache.tx, this.cache.myx * var1 + this.cache.myy * var3 + this.cache.ty);
   }

   public Point3D transform(double var1, double var3, double var5) {
      this.updateCache();
      return new Point3D(this.cache.mxx * var1 + this.cache.mxy * var3 + this.cache.mxz * var5 + this.cache.tx, this.cache.myx * var1 + this.cache.myy * var3 + this.cache.myz * var5 + this.cache.ty, this.cache.mzx * var1 + this.cache.mzy * var3 + this.cache.mzz * var5 + this.cache.tz);
   }

   void transform2DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      this.updateCache();

      while(true) {
         --var5;
         if (var5 < 0) {
            return;
         }

         double var6 = var1[var2++];
         double var8 = var1[var2++];
         var3[var4++] = this.cache.mxx * var6 + this.cache.mxy * var8 + this.cache.tx;
         var3[var4++] = this.cache.myx * var6 + this.cache.myy * var8 + this.cache.ty;
      }
   }

   void transform3DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      this.updateCache();

      while(true) {
         --var5;
         if (var5 < 0) {
            return;
         }

         double var6 = var1[var2++];
         double var8 = var1[var2++];
         double var10 = var1[var2++];
         var3[var4++] = this.cache.mxx * var6 + this.cache.mxy * var8 + this.cache.mxz * var10 + this.cache.tx;
         var3[var4++] = this.cache.myx * var6 + this.cache.myy * var8 + this.cache.myz * var10 + this.cache.ty;
         var3[var4++] = this.cache.mzx * var6 + this.cache.mzy * var8 + this.cache.mzz * var10 + this.cache.tz;
      }
   }

   public Point2D deltaTransform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      this.updateCache();
      return new Point2D(this.cache.mxx * var1 + this.cache.mxy * var3, this.cache.myx * var1 + this.cache.myy * var3);
   }

   public Point3D deltaTransform(double var1, double var3, double var5) {
      this.updateCache();
      return new Point3D(this.cache.mxx * var1 + this.cache.mxy * var3 + this.cache.mxz * var5, this.cache.myx * var1 + this.cache.myy * var3 + this.cache.myz * var5, this.cache.mzx * var1 + this.cache.mzy * var3 + this.cache.mzz * var5);
   }

   public Point2D inverseTransform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      this.updateInverseCache();
      return new Point2D(this.inverseCache.mxx * var1 + this.inverseCache.mxy * var3 + this.inverseCache.tx, this.inverseCache.myx * var1 + this.inverseCache.myy * var3 + this.inverseCache.ty);
   }

   public Point3D inverseTransform(double var1, double var3, double var5) {
      this.updateInverseCache();
      return new Point3D(this.inverseCache.mxx * var1 + this.inverseCache.mxy * var3 + this.inverseCache.mxz * var5 + this.inverseCache.tx, this.inverseCache.myx * var1 + this.inverseCache.myy * var3 + this.inverseCache.myz * var5 + this.inverseCache.ty, this.inverseCache.mzx * var1 + this.inverseCache.mzy * var3 + this.inverseCache.mzz * var5 + this.inverseCache.tz);
   }

   void inverseTransform2DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      this.updateInverseCache();

      while(true) {
         --var5;
         if (var5 < 0) {
            return;
         }

         double var6 = var1[var2++];
         double var8 = var1[var2++];
         var3[var4++] = this.inverseCache.mxx * var6 + this.inverseCache.mxy * var8 + this.inverseCache.tx;
         var3[var4++] = this.inverseCache.myx * var6 + this.inverseCache.myy * var8 + this.inverseCache.ty;
      }
   }

   void inverseTransform3DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      this.updateInverseCache();

      while(true) {
         --var5;
         if (var5 < 0) {
            return;
         }

         double var6 = var1[var2++];
         double var8 = var1[var2++];
         double var10 = var1[var2++];
         var3[var4++] = this.inverseCache.mxx * var6 + this.inverseCache.mxy * var8 + this.inverseCache.mxz * var10 + this.inverseCache.tx;
         var3[var4++] = this.inverseCache.myx * var6 + this.inverseCache.myy * var8 + this.inverseCache.myz * var10 + this.inverseCache.ty;
         var3[var4++] = this.inverseCache.mzx * var6 + this.inverseCache.mzy * var8 + this.inverseCache.mzz * var10 + this.inverseCache.tz;
      }
   }

   public Point2D inverseDeltaTransform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      this.updateInverseCache();
      return new Point2D(this.inverseCache.mxx * var1 + this.inverseCache.mxy * var3, this.inverseCache.myx * var1 + this.inverseCache.myy * var3);
   }

   public Point3D inverseDeltaTransform(double var1, double var3, double var5) {
      this.updateInverseCache();
      return new Point3D(this.inverseCache.mxx * var1 + this.inverseCache.mxy * var3 + this.inverseCache.mxz * var5, this.inverseCache.myx * var1 + this.inverseCache.myy * var3 + this.inverseCache.myz * var5, this.inverseCache.mzx * var1 + this.inverseCache.mzy * var3 + this.inverseCache.mzz * var5);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Rotate [");
      var1.append("angle=").append(this.getAngle());
      var1.append(", pivotX=").append(this.getPivotX());
      var1.append(", pivotY=").append(this.getPivotY());
      var1.append(", pivotZ=").append(this.getPivotZ());
      var1.append(", axis=").append(this.getAxis());
      return var1.append("]").toString();
   }

   /** @deprecated */
   @Deprecated
   public void impl_apply(Affine3D var1) {
      double var2 = this.getPivotX();
      double var4 = this.getPivotY();
      double var6 = this.getPivotZ();
      double var8 = this.getAngle();
      if (var2 == 0.0 && var4 == 0.0 && var6 == 0.0) {
         var1.rotate(Math.toRadians(var8), this.getAxis().getX(), this.getAxis().getY(), this.getAxis().getZ());
      } else {
         var1.translate(var2, var4, var6);
         var1.rotate(Math.toRadians(var8), this.getAxis().getX(), this.getAxis().getY(), this.getAxis().getZ());
         var1.translate(-var2, -var4, -var6);
      }

   }

   /** @deprecated */
   @Deprecated
   public BaseTransform impl_derive(BaseTransform var1) {
      if (this.isIdentity()) {
         return var1;
      } else {
         double var2 = this.getPivotX();
         double var4 = this.getPivotY();
         double var6 = this.getPivotZ();
         double var8 = this.getAngle();
         if (var2 == 0.0 && var4 == 0.0 && var6 == 0.0) {
            return var1.deriveWithRotation(Math.toRadians(var8), this.getAxis().getX(), this.getAxis().getY(), this.getAxis().getZ());
         } else {
            var1 = var1.deriveWithTranslation(var2, var4, var6);
            var1 = var1.deriveWithRotation(Math.toRadians(var8), this.getAxis().getX(), this.getAxis().getY(), this.getAxis().getZ());
            return var1.deriveWithTranslation(-var2, -var4, -var6);
         }
      }
   }

   void validate() {
      this.getAxis();
      this.getAngle();
      this.getPivotX();
      this.getPivotY();
      this.getPivotZ();
   }

   protected void transformChanged() {
      if (this.cache != null) {
         this.cache.invalidate();
      }

      super.transformChanged();
   }

   void appendTo(Affine var1) {
      var1.appendRotation(this.getAngle(), this.getPivotX(), this.getPivotY(), this.getPivotZ(), this.getAxis());
   }

   void prependTo(Affine var1) {
      var1.prependRotation(this.getAngle(), this.getPivotX(), this.getPivotY(), this.getPivotZ(), this.getAxis());
   }

   private void updateCache() {
      if (this.cache == null) {
         this.cache = new MatrixCache();
      }

      if (!this.cache.valid) {
         this.cache.update(this.getAngle(), this.getAxis(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
      }

   }

   private void updateInverseCache() {
      if (this.inverseCache == null) {
         this.inverseCache = new MatrixCache();
      }

      if (!this.inverseCache.valid) {
         this.inverseCache.update(-this.getAngle(), this.getAxis(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
      }

   }

   private static class MatrixCache {
      boolean valid = false;
      boolean is3D = false;
      double mxx;
      double mxy;
      double mxz;
      double tx;
      double myx;
      double myy;
      double myz;
      double ty;
      double mzx;
      double mzy;
      double mzz = 1.0;
      double tz;

      public MatrixCache() {
      }

      public void update(double var1, Point3D var3, double var4, double var6, double var8) {
         double var10 = Math.toRadians(var1);
         double var12 = Math.sin(var10);
         double var14 = Math.cos(var10);
         if (var3 != Rotate.Z_AXIS && (var3.getX() != 0.0 || var3.getY() != 0.0 || !(var3.getZ() > 0.0))) {
            this.is3D = true;
            double var16;
            double var18;
            double var20;
            if (var3 != Rotate.X_AXIS && var3 != Rotate.Y_AXIS && var3 != Rotate.Z_AXIS) {
               double var22 = Math.sqrt(var3.getX() * var3.getX() + var3.getY() * var3.getY() + var3.getZ() * var3.getZ());
               if (var22 == 0.0) {
                  this.mxx = 1.0;
                  this.mxy = 0.0;
                  this.mxz = 0.0;
                  this.tx = 0.0;
                  this.myx = 0.0;
                  this.myy = 1.0;
                  this.myz = 0.0;
                  this.ty = 0.0;
                  this.mzx = 0.0;
                  this.mzy = 0.0;
                  this.mzz = 1.0;
                  this.tz = 0.0;
                  this.valid = true;
                  return;
               }

               var16 = var3.getX() / var22;
               var18 = var3.getY() / var22;
               var20 = var3.getZ() / var22;
            } else {
               var16 = var3.getX();
               var18 = var3.getY();
               var20 = var3.getZ();
            }

            this.mxx = var14 + var16 * var16 * (1.0 - var14);
            this.mxy = var16 * var18 * (1.0 - var14) - var20 * var12;
            this.mxz = var16 * var20 * (1.0 - var14) + var18 * var12;
            this.tx = var4 * (1.0 - this.mxx) - var6 * this.mxy - var8 * this.mxz;
            this.myx = var18 * var16 * (1.0 - var14) + var20 * var12;
            this.myy = var14 + var18 * var18 * (1.0 - var14);
            this.myz = var18 * var20 * (1.0 - var14) - var16 * var12;
            this.ty = var6 * (1.0 - this.myy) - var4 * this.myx - var8 * this.myz;
            this.mzx = var20 * var16 * (1.0 - var14) - var18 * var12;
            this.mzy = var20 * var18 * (1.0 - var14) + var16 * var12;
            this.mzz = var14 + var20 * var20 * (1.0 - var14);
            this.tz = var8 * (1.0 - this.mzz) - var4 * this.mzx - var6 * this.mzy;
            this.valid = true;
         } else {
            this.mxx = var14;
            this.mxy = -var12;
            this.tx = var4 * (1.0 - var14) + var6 * var12;
            this.myx = var12;
            this.myy = var14;
            this.ty = var6 * (1.0 - var14) - var4 * var12;
            if (this.is3D) {
               this.mxz = 0.0;
               this.myz = 0.0;
               this.mzx = 0.0;
               this.mzy = 0.0;
               this.mzz = 1.0;
               this.tz = 0.0;
               this.is3D = false;
            }

            this.valid = true;
         }
      }

      public void invalidate() {
         this.valid = false;
      }
   }
}
