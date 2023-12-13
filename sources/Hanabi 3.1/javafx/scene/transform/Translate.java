package javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class Translate extends Transform {
   private DoubleProperty x;
   private DoubleProperty y;
   private DoubleProperty z;

   public Translate() {
   }

   public Translate(double var1, double var3) {
      this.setX(var1);
      this.setY(var3);
   }

   public Translate(double var1, double var3, double var5) {
      this(var1, var3);
      this.setZ(var5);
   }

   public final void setX(double var1) {
      this.xProperty().set(var1);
   }

   public final double getX() {
      return this.x == null ? 0.0 : this.x.get();
   }

   public final DoubleProperty xProperty() {
      if (this.x == null) {
         this.x = new DoublePropertyBase() {
            public void invalidated() {
               Translate.this.transformChanged();
            }

            public Object getBean() {
               return Translate.this;
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
      return this.y == null ? 0.0 : this.y.get();
   }

   public final DoubleProperty yProperty() {
      if (this.y == null) {
         this.y = new DoublePropertyBase() {
            public void invalidated() {
               Translate.this.transformChanged();
            }

            public Object getBean() {
               return Translate.this;
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
      return this.z == null ? 0.0 : this.z.get();
   }

   public final DoubleProperty zProperty() {
      if (this.z == null) {
         this.z = new DoublePropertyBase() {
            public void invalidated() {
               Translate.this.transformChanged();
            }

            public Object getBean() {
               return Translate.this;
            }

            public String getName() {
               return "z";
            }
         };
      }

      return this.z;
   }

   public double getTx() {
      return this.getX();
   }

   public double getTy() {
      return this.getY();
   }

   public double getTz() {
      return this.getZ();
   }

   boolean computeIs2D() {
      return this.getZ() == 0.0;
   }

   boolean computeIsIdentity() {
      return this.getX() == 0.0 && this.getY() == 0.0 && this.getZ() == 0.0;
   }

   void fill2DArray(double[] var1) {
      var1[0] = 1.0;
      var1[1] = 0.0;
      var1[2] = this.getX();
      var1[3] = 0.0;
      var1[4] = 1.0;
      var1[5] = this.getY();
   }

   void fill3DArray(double[] var1) {
      var1[0] = 1.0;
      var1[1] = 0.0;
      var1[2] = 0.0;
      var1[3] = this.getX();
      var1[4] = 0.0;
      var1[5] = 1.0;
      var1[6] = 0.0;
      var1[7] = this.getY();
      var1[8] = 0.0;
      var1[9] = 0.0;
      var1[10] = 1.0;
      var1[11] = this.getZ();
   }

   public Transform createConcatenation(Transform var1) {
      if (var1 instanceof Translate) {
         Translate var28 = (Translate)var1;
         return new Translate(this.getX() + var28.getX(), this.getY() + var28.getY(), this.getZ() + var28.getZ());
      } else {
         if (var1 instanceof Scale) {
            Scale var2 = (Scale)var1;
            double var3 = var2.getX();
            double var5 = var2.getY();
            double var7 = var2.getZ();
            double var9 = this.getX();
            double var11 = this.getY();
            double var13 = this.getZ();
            if ((var9 == 0.0 || var3 != 1.0) && (var11 == 0.0 || var5 != 1.0) && (var13 == 0.0 || var7 != 1.0)) {
               return new Scale(var3, var5, var7, var2.getPivotX() + (var3 == 1.0 ? 0.0 : var9 / (1.0 - var3)), var2.getPivotY() + (var5 == 1.0 ? 0.0 : var11 / (1.0 - var5)), var2.getPivotZ() + (var7 == 1.0 ? 0.0 : var13 / (1.0 - var7)));
            }
         }

         if (var1 instanceof Affine) {
            Affine var27 = (Affine)var1.clone();
            var27.prepend(this);
            return var27;
         } else {
            double var26 = var1.getMxx();
            double var4 = var1.getMxy();
            double var6 = var1.getMxz();
            double var8 = var1.getTx();
            double var10 = var1.getMyx();
            double var12 = var1.getMyy();
            double var14 = var1.getMyz();
            double var16 = var1.getTy();
            double var18 = var1.getMzx();
            double var20 = var1.getMzy();
            double var22 = var1.getMzz();
            double var24 = var1.getTz();
            return new Affine(var26, var4, var6, var8 + this.getX(), var10, var12, var14, var16 + this.getY(), var18, var20, var22, var24 + this.getZ());
         }
      }
   }

   public Translate createInverse() {
      return new Translate(-this.getX(), -this.getY(), -this.getZ());
   }

   public Translate clone() {
      return new Translate(this.getX(), this.getY(), this.getZ());
   }

   public Point2D transform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      return new Point2D(var1 + this.getX(), var3 + this.getY());
   }

   public Point3D transform(double var1, double var3, double var5) {
      return new Point3D(var1 + this.getX(), var3 + this.getY(), var5 + this.getZ());
   }

   void transform2DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      double var6 = this.getX();
      double var8 = this.getY();

      while(true) {
         --var5;
         if (var5 < 0) {
            return;
         }

         double var10 = var1[var2++];
         double var12 = var1[var2++];
         var3[var4++] = var10 + var6;
         var3[var4++] = var12 + var8;
      }
   }

   void transform3DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      double var6 = this.getX();
      double var8 = this.getY();
      double var10 = this.getZ();

      while(true) {
         --var5;
         if (var5 < 0) {
            return;
         }

         double var12 = var1[var2++];
         double var14 = var1[var2++];
         double var16 = var1[var2++];
         var3[var4++] = var12 + var6;
         var3[var4++] = var14 + var8;
         var3[var4++] = var16 + var10;
      }
   }

   public Point2D deltaTransform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      return new Point2D(var1, var3);
   }

   public Point2D deltaTransform(Point2D var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.ensureCanTransform2DPoint();
         return var1;
      }
   }

   public Point3D deltaTransform(double var1, double var3, double var5) {
      return new Point3D(var1, var3, var5);
   }

   public Point3D deltaTransform(Point3D var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var1;
      }
   }

   public Point2D inverseTransform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      return new Point2D(var1 - this.getX(), var3 - this.getY());
   }

   public Point3D inverseTransform(double var1, double var3, double var5) {
      return new Point3D(var1 - this.getX(), var3 - this.getY(), var5 - this.getZ());
   }

   void inverseTransform2DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      double var6 = this.getX();
      double var8 = this.getY();

      while(true) {
         --var5;
         if (var5 < 0) {
            return;
         }

         var3[var4++] = var1[var2++] - var6;
         var3[var4++] = var1[var2++] - var8;
      }
   }

   void inverseTransform3DPointsImpl(double[] var1, int var2, double[] var3, int var4, int var5) {
      double var6 = this.getX();
      double var8 = this.getY();
      double var10 = this.getZ();

      while(true) {
         --var5;
         if (var5 < 0) {
            return;
         }

         var3[var4++] = var1[var2++] - var6;
         var3[var4++] = var1[var2++] - var8;
         var3[var4++] = var1[var2++] - var10;
      }
   }

   public Point2D inverseDeltaTransform(double var1, double var3) {
      this.ensureCanTransform2DPoint();
      return new Point2D(var1, var3);
   }

   public Point2D inverseDeltaTransform(Point2D var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.ensureCanTransform2DPoint();
         return var1;
      }
   }

   public Point3D inverseDeltaTransform(double var1, double var3, double var5) {
      return new Point3D(var1, var3, var5);
   }

   public Point3D inverseDeltaTransform(Point3D var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var1;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Translate [");
      var1.append("x=").append(this.getX());
      var1.append(", y=").append(this.getY());
      var1.append(", z=").append(this.getZ());
      return var1.append("]").toString();
   }

   /** @deprecated */
   @Deprecated
   public void impl_apply(Affine3D var1) {
      var1.translate(this.getX(), this.getY(), this.getZ());
   }

   /** @deprecated */
   @Deprecated
   public BaseTransform impl_derive(BaseTransform var1) {
      return var1.deriveWithTranslation(this.getX(), this.getY(), this.getZ());
   }

   void validate() {
      this.getX();
      this.getY();
      this.getZ();
   }

   void appendTo(Affine var1) {
      var1.appendTranslation(this.getTx(), this.getTy(), this.getTz());
   }

   void prependTo(Affine var1) {
      var1.prependTranslation(this.getTx(), this.getTy(), this.getTz());
   }
}
