package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class Scale extends Transform {
   private DoubleProperty x;
   private DoubleProperty y;
   private DoubleProperty z;
   private DoubleProperty pivotX;
   private DoubleProperty pivotY;
   private DoubleProperty pivotZ;

   public Scale() {
   }

   public Scale(double var1, double var3) {
      this.setX(var1);
      this.setY(var3);
   }

   public Scale(double var1, double var3, double var5, double var7) {
      this(var1, var3);
      this.setPivotX(var5);
      this.setPivotY(var7);
   }

   public Scale(double var1, double var3, double var5) {
      this(var1, var3);
      this.setZ(var5);
   }

   public Scale(double var1, double var3, double var5, double var7, double var9, double var11) {
      this(var1, var3, var7, var9);
      this.setZ(var5);
      this.setPivotZ(var11);
   }

   public final void setX(double var1) {
      this.xProperty().set(var1);
   }

   public final double getX() {
      return this.x == null ? 1.0 : this.x.get();
   }

   public final DoubleProperty xProperty() {
      if (this.x == null) {
         this.x = new DoublePropertyBase(1.0) {
            public void invalidated() {
               Scale.this.transformChanged();
            }

            public Object getBean() {
               return Scale.this;
            }

            public String getName() {
               return "x";
            }
         };
      }

      return this.x;
   }

   public final void setY(double var1) {
      this.yProperty().set(var1);
   }

   public final double getY() {
      return this.y == null ? 1.0 : this.y.get();
   }

   public final DoubleProperty yProperty() {
      if (this.y == null) {
         this.y = new DoublePropertyBase(1.0) {
            public void invalidated() {
               Scale.this.transformChanged();
            }

            public Object getBean() {
               return Scale.this;
            }

            public String getName() {
               return "y";
            }
         };
      }

      return this.y;
   }

   public final void setZ(double var1) {
      this.zProperty().set(var1);
   }

   public final double getZ() {
      return this.z == null ? 1.0 : this.z.get();
   }

   public final DoubleProperty zProperty() {
      if (this.z == null) {
         this.z = new DoublePropertyBase(1.0) {
            public void invalidated() {
               Scale.this.transformChanged();
            }

            public Object getBean() {
               return Scale.this;
            }

            public String getName() {
               return "z";
            }
         };
      }

      return this.z;
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
               Scale.this.transformChanged();
            }

            public Object getBean() {
               return Scale.this;
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
               Scale.this.transformChanged();
            }

            public Object getBean() {
               return Scale.this;
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
               Scale.this.transformChanged();
            }

            public Object getBean() {
               return Scale.this;
            }

            public String getName() {
               return "pivotZ";
            }
         };
      }

      return this.pivotZ;
   }

   public double getMxx() {
      return this.getX();
   }

   public double getMyy() {
      return this.getY();
   }

   public double getMzz() {
      return this.getZ();
   }

   public double getTx() {
      return (1.0 - this.getX()) * this.getPivotX();
   }

   public double getTy() {
      return (1.0 - this.getY()) * this.getPivotY();
   }

   public double getTz() {
      return (1.0 - this.getZ()) * this.getPivotZ();
   }

   boolean computeIs2D() {
      return this.getZ() == 1.0;
   }

   boolean computeIsIdentity() {
      return this.getX() == 1.0 && this.getY() == 1.0 && this.getZ() == 1.0;
   }

   void fill2DArray(double[] var1) {
      double var2 = this.getX();
      double var4 = this.getY();
      var1[0] = var2;
      var1[1] = 0.0;
      var1[2] = (1.0 - var2) * this.getPivotX();
      var1[3] = 0.0;
      var1[4] = var4;
      var1[5] = (1.0 - var4) * this.getPivotY();
   }

   void fill3DArray(double[] var1) {
      double var2 = this.getX();
      double var4 = this.getY();
      double var6 = this.getZ();
      var1[0] = var2;
      var1[1] = 0.0;
      var1[2] = 0.0;
      var1[3] = (1.0 - var2) * this.getPivotX();
      var1[4] = 0.0;
      var1[5] = var4;
      var1[6] = 0.0;
      var1[7] = (1.0 - var4) * this.getPivotY();
      var1[8] = 0.0;
      var1[9] = 0.0;
      var1[10] = var6;
      var1[11] = (1.0 - var6) * this.getPivotZ();
   }

   public Transform createConcatenation(Transform var1) {
      double var2 = this.getX();
      double var4 = this.getY();
      double var6 = this.getZ();
      if (var1 instanceof Scale) {
         Scale var8 = (Scale)var1;
         if (var8.getPivotX() == this.getPivotX() && var8.getPivotY() == this.getPivotY() && var8.getPivotZ() == this.getPivotZ()) {
            return new Scale(var2 * var8.getX(), var4 * var8.getY(), var6 * var8.getZ(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
         }
      }

      if (var1 instanceof Translate) {
         Translate var32 = (Translate)var1;
         double var9 = var32.getX();
         double var11 = var32.getY();
         double var13 = var32.getZ();
         if ((var9 == 0.0 || var2 != 1.0 && var2 != 0.0) && (var11 == 0.0 || var4 != 1.0 && var4 != 0.0) && (var13 == 0.0 || var6 != 1.0 && var6 != 0.0)) {
            return new Scale(var2, var4, var6, (var2 != 1.0 ? var2 * var9 / (1.0 - var2) : 0.0) + this.getPivotX(), (var4 != 1.0 ? var4 * var11 / (1.0 - var4) : 0.0) + this.getPivotY(), (var6 != 1.0 ? var6 * var13 / (1.0 - var6) : 0.0) + this.getPivotZ());
         }
      }

      if (var1 instanceof Affine) {
         Affine var34 = (Affine)var1.clone();
         var34.prepend(this);
         return var34;
      } else {
         double var33 = var1.getMxx();
         double var10 = var1.getMxy();
         double var12 = var1.getMxz();
         double var14 = var1.getTx();
         double var16 = var1.getMyx();
         double var18 = var1.getMyy();
         double var20 = var1.getMyz();
         double var22 = var1.getTy();
         double var24 = var1.getMzx();
         double var26 = var1.getMzy();
         double var28 = var1.getMzz();
         double var30 = var1.getTz();
         return new Affine(var2 * var33, var2 * var10, var2 * var12, var2 * var14 + (1.0 - var2) * this.getPivotX(), var4 * var16, var4 * var18, var4 * var20, var4 * var22 + (1.0 - var4) * this.getPivotY(), var6 * var24, var6 * var26, var6 * var28, var6 * var30 + (1.0 - var6) * this.getPivotZ());
      }
   }

   public Scale createInverse() throws NonInvertibleTransformException {
      double var1 = this.getX();
      double var3 = this.getY();
      double var5 = this.getZ();
      if (var1 != 0.0 && var3 != 0.0 && var5 != 0.0) {
         return new Scale(1.0 / var1, 1.0 / var3, 1.0 / var5, this.getPivotX(), this.getPivotY(), this.getPivotZ());
      } else {
         throw new NonInvertibleTransformException("Zero scale is not invertible");
      }
   }

   public Scale clone() {
      return new Scale(this.getX(), this.getY(), this.getZ(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
   }

   public Point2D transform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      double var5 = this.getX();
      double var7 = this.getY();
      return new Point2D(var5 * var1 + (1.0 - var5) * this.getPivotX(), var7 * var3 + (1.0 - var7) * this.getPivotY());
   }

   public Point3D transform(double var1, double var3, double var5) {
      double var7 = this.getX();
      double var9 = this.getY();
      double var11 = this.getZ();
      return new Point3D(var7 * var1 + (1.0 - var7) * this.getPivotX(), var9 * var3 + (1.0 - var9) * this.getPivotY(), var11 * var5 + (1.0 - var11) * this.getPivotZ());
   }

   void transform2DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      double var6 = this.getX();
      double var8 = this.getY();
      double var10 = this.getPivotX();
      double var12 = this.getPivotY();

      while(true) {
         --var5;
         if (var5 < 0) {
            return;
         }

         double var14 = var1[var2++];
         double var16 = var1[var2++];
         var3[var4++] = var6 * var14 + (1.0 - var6) * var10;
         var3[var4++] = var8 * var16 + (1.0 - var8) * var12;
      }
   }

   void transform3DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      double var6 = this.getX();
      double var8 = this.getY();
      double var10 = this.getZ();
      double var12 = this.getPivotX();
      double var14 = this.getPivotY();
      double var16 = this.getPivotZ();

      while(true) {
         --var5;
         if (var5 < 0) {
            return;
         }

         var3[var4++] = var6 * var1[var2++] + (1.0 - var6) * var12;
         var3[var4++] = var8 * var1[var2++] + (1.0 - var8) * var14;
         var3[var4++] = var10 * var1[var2++] + (1.0 - var10) * var16;
      }
   }

   public Point2D deltaTransform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      return new Point2D(this.getX() * var1, this.getY() * var3);
   }

   public Point3D deltaTransform(double var1, double var3, double var5) {
      return new Point3D(this.getX() * var1, this.getY() * var3, this.getZ() * var5);
   }

   public Point2D inverseTransform(double var1, double var3) throws NonInvertibleTransformException {
      this.ensureCanTransform2DPoint();
      double var5 = this.getX();
      double var7 = this.getY();
      if (var5 != 0.0 && var7 != 0.0) {
         double var9 = 1.0 / var5;
         double var11 = 1.0 / var7;
         return new Point2D(var9 * var1 + (1.0 - var9) * this.getPivotX(), var11 * var3 + (1.0 - var11) * this.getPivotY());
      } else {
         throw new NonInvertibleTransformException("Zero scale is not invertible");
      }
   }

   public Point3D inverseTransform(double var1, double var3, double var5) throws NonInvertibleTransformException {
      double var7 = this.getX();
      double var9 = this.getY();
      double var11 = this.getZ();
      if (var7 != 0.0 && var9 != 0.0 && var11 != 0.0) {
         double var13 = 1.0 / var7;
         double var15 = 1.0 / var9;
         double var17 = 1.0 / var11;
         return new Point3D(var13 * var1 + (1.0 - var13) * this.getPivotX(), var15 * var3 + (1.0 - var15) * this.getPivotY(), var17 * var5 + (1.0 - var17) * this.getPivotZ());
      } else {
         throw new NonInvertibleTransformException("Zero scale is not invertible");
      }
   }

   void inverseTransform2DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) throws NonInvertibleTransformException {
      double var6 = this.getX();
      double var8 = this.getY();
      if (var6 != 0.0 && var8 != 0.0) {
         double var10 = 1.0 / var6;
         double var12 = 1.0 / var8;
         double var14 = this.getPivotX();
         double var16 = this.getPivotY();

         while(true) {
            --var5;
            if (var5 < 0) {
               return;
            }

            var3[var4++] = var10 * var1[var2++] + (1.0 - var10) * var14;
            var3[var4++] = var12 * var1[var2++] + (1.0 - var12) * var16;
         }
      } else {
         throw new NonInvertibleTransformException("Zero scale is not invertible");
      }
   }

   void inverseTransform3DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) throws NonInvertibleTransformException {
      double var6 = this.getX();
      double var8 = this.getY();
      double var10 = this.getZ();
      if (var6 != 0.0 && var8 != 0.0 && var10 != 0.0) {
         double var12 = 1.0 / var6;
         double var14 = 1.0 / var8;
         double var16 = 1.0 / var10;
         double var18 = this.getPivotX();
         double var20 = this.getPivotY();
         double var22 = this.getPivotZ();

         while(true) {
            --var5;
            if (var5 < 0) {
               return;
            }

            var3[var4++] = var12 * var1[var2++] + (1.0 - var12) * var18;
            var3[var4++] = var14 * var1[var2++] + (1.0 - var14) * var20;
            var3[var4++] = var16 * var1[var2++] + (1.0 - var16) * var22;
         }
      } else {
         throw new NonInvertibleTransformException("Zero scale is not invertible");
      }
   }

   public Point2D inverseDeltaTransform(double var1, double var3) throws NonInvertibleTransformException {
      this.ensureCanTransform2DPoint();
      double var5 = this.getX();
      double var7 = this.getY();
      if (var5 != 0.0 && var7 != 0.0) {
         return new Point2D(1.0 / var5 * var1, 1.0 / var7 * var3);
      } else {
         throw new NonInvertibleTransformException("Zero scale is not invertible");
      }
   }

   public Point3D inverseDeltaTransform(double var1, double var3, double var5) throws NonInvertibleTransformException {
      double var7 = this.getX();
      double var9 = this.getY();
      double var11 = this.getZ();
      if (var7 != 0.0 && var9 != 0.0 && var11 != 0.0) {
         return new Point3D(1.0 / var7 * var1, 1.0 / var9 * var3, 1.0 / var11 * var5);
      } else {
         throw new NonInvertibleTransformException("Zero scale is not invertible");
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Scale [");
      var1.append("x=").append(this.getX());
      var1.append(", y=").append(this.getY());
      var1.append(", z=").append(this.getZ());
      var1.append(", pivotX=").append(this.getPivotX());
      var1.append(", pivotY=").append(this.getPivotY());
      var1.append(", pivotZ=").append(this.getPivotZ());
      return var1.append("]").toString();
   }

   /** @deprecated */
   @Deprecated
   public void impl_apply(Affine3D var1) {
      if (this.getPivotX() == 0.0 && this.getPivotY() == 0.0 && this.getPivotZ() == 0.0) {
         var1.scale(this.getX(), this.getY(), this.getZ());
      } else {
         var1.translate(this.getPivotX(), this.getPivotY(), this.getPivotZ());
         var1.scale(this.getX(), this.getY(), this.getZ());
         var1.translate(-this.getPivotX(), -this.getPivotY(), -this.getPivotZ());
      }

   }

   /** @deprecated */
   @Deprecated
   public BaseTransform impl_derive(BaseTransform var1) {
      if (this.isIdentity()) {
         return var1;
      } else if (this.getPivotX() == 0.0 && this.getPivotY() == 0.0 && this.getPivotZ() == 0.0) {
         return var1.deriveWithScale(this.getX(), this.getY(), this.getZ());
      } else {
         var1 = var1.deriveWithTranslation(this.getPivotX(), this.getPivotY(), this.getPivotZ());
         var1 = var1.deriveWithScale(this.getX(), this.getY(), this.getZ());
         return var1.deriveWithTranslation(-this.getPivotX(), -this.getPivotY(), -this.getPivotZ());
      }
   }

   void validate() {
      this.getX();
      this.getPivotX();
      this.getY();
      this.getPivotY();
      this.getZ();
      this.getPivotZ();
   }

   void appendTo(Affine var1) {
      var1.appendScale(this.getX(), this.getY(), this.getZ(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
   }

   void prependTo(Affine var1) {
      var1.prependScale(this.getX(), this.getY(), this.getZ(), this.getPivotX(), this.getPivotY(), this.getPivotZ());
   }
}
