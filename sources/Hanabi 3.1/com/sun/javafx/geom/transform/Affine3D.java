package com.sun.javafx.geom.transform;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec3d;

public class Affine3D extends AffineBase {
   private double mxz;
   private double myz;
   private double mzx;
   private double mzy;
   private double mzz;
   private double mzt;

   public Affine3D() {
      this.mxx = this.myy = this.mzz = 1.0;
   }

   public Affine3D(BaseTransform var1) {
      this.setTransform(var1);
   }

   public Affine3D(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      this.mxx = var1;
      this.mxy = var3;
      this.mxz = var5;
      this.mxt = var7;
      this.myx = var9;
      this.myy = var11;
      this.myz = var13;
      this.myt = var15;
      this.mzx = var17;
      this.mzy = var19;
      this.mzz = var21;
      this.mzt = var23;
      this.updateState();
   }

   public Affine3D(Affine3D var1) {
      this.mxx = var1.mxx;
      this.mxy = var1.mxy;
      this.mxz = var1.mxz;
      this.mxt = var1.mxt;
      this.myx = var1.myx;
      this.myy = var1.myy;
      this.myz = var1.myz;
      this.myt = var1.myt;
      this.mzx = var1.mzx;
      this.mzy = var1.mzy;
      this.mzz = var1.mzz;
      this.mzt = var1.mzt;
      this.state = var1.state;
      this.type = var1.type;
   }

   public BaseTransform copy() {
      return new Affine3D(this);
   }

   public BaseTransform.Degree getDegree() {
      return BaseTransform.Degree.AFFINE_3D;
   }

   protected void reset3Delements() {
      this.mxz = 0.0;
      this.myz = 0.0;
      this.mzx = 0.0;
      this.mzy = 0.0;
      this.mzz = 1.0;
      this.mzt = 0.0;
   }

   protected void updateState() {
      super.updateState();
      if (!almostZero(this.mxz) || !almostZero(this.myz) || !almostZero(this.mzx) || !almostZero(this.mzy) || !almostOne(this.mzz) || !almostZero(this.mzt)) {
         this.state |= 8;
         if (this.type != -1) {
            this.type |= 128;
         }
      }

   }

   public double getMxz() {
      return this.mxz;
   }

   public double getMyz() {
      return this.myz;
   }

   public double getMzx() {
      return this.mzx;
   }

   public double getMzy() {
      return this.mzy;
   }

   public double getMzz() {
      return this.mzz;
   }

   public double getMzt() {
      return this.mzt;
   }

   public double getDeterminant() {
      return (this.state & 8) == 0 ? super.getDeterminant() : this.mxx * (this.myy * this.mzz - this.mzy * this.myz) + this.mxy * (this.myz * this.mzx - this.mzz * this.myx) + this.mxz * (this.myx * this.mzy - this.mzx * this.myy);
   }

   public void setTransform(BaseTransform var1) {
      this.mxx = var1.getMxx();
      this.mxy = var1.getMxy();
      this.mxz = var1.getMxz();
      this.mxt = var1.getMxt();
      this.myx = var1.getMyx();
      this.myy = var1.getMyy();
      this.myz = var1.getMyz();
      this.myt = var1.getMyt();
      this.mzx = var1.getMzx();
      this.mzy = var1.getMzy();
      this.mzz = var1.getMzz();
      this.mzt = var1.getMzt();
      this.updateState();
   }

   public void setTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      this.mxx = var1;
      this.mxy = var3;
      this.mxz = var5;
      this.mxt = var7;
      this.myx = var9;
      this.myy = var11;
      this.myz = var13;
      this.myt = var15;
      this.mzx = var17;
      this.mzy = var19;
      this.mzz = var21;
      this.mzt = var23;
      this.updateState();
   }

   public void setToTranslation(double var1, double var3, double var5) {
      this.mxx = 1.0;
      this.mxy = 0.0;
      this.mxz = 0.0;
      this.mxt = var1;
      this.myx = 0.0;
      this.myy = 1.0;
      this.myz = 0.0;
      this.myt = var3;
      this.mzx = 0.0;
      this.mzy = 0.0;
      this.mzz = 1.0;
      this.mzt = var5;
      if (var5 == 0.0) {
         if (var1 == 0.0 && var3 == 0.0) {
            this.state = 0;
            this.type = 0;
         } else {
            this.state = 1;
            this.type = 1;
         }
      } else if (var1 == 0.0 && var3 == 0.0) {
         this.state = 8;
         this.type = 128;
      } else {
         this.state = 9;
         this.type = 129;
      }

   }

   public void setToScale(double var1, double var3, double var5) {
      this.mxx = var1;
      this.mxy = 0.0;
      this.mxz = 0.0;
      this.mxt = 0.0;
      this.myx = 0.0;
      this.myy = var3;
      this.myz = 0.0;
      this.myt = 0.0;
      this.mzx = 0.0;
      this.mzy = 0.0;
      this.mzz = var5;
      this.mzt = 0.0;
      if (var5 == 1.0) {
         if (var1 == 1.0 && var3 == 1.0) {
            this.state = 0;
            this.type = 0;
         } else {
            this.state = 2;
            this.type = -1;
         }
      } else if (var1 == 1.0 && var3 == 1.0) {
         this.state = 8;
         this.type = 128;
      } else {
         this.state = 10;
         this.type = -1;
      }

   }

   public void setToRotation(double var1, double var3, double var5, double var7, double var9, double var11, double var13) {
      this.setToRotation(var1, var3, var5, var7);
      if (var9 != 0.0 || var11 != 0.0 || var13 != 0.0) {
         this.preTranslate(var9, var11, var13);
         this.translate(-var9, -var11, -var13);
      }

   }

   public void setToRotation(double var1, double var3, double var5, double var7) {
      double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
      if (almostZero(var9)) {
         this.setToIdentity();
      } else {
         var9 = 1.0 / var9;
         double var11 = var3 * var9;
         double var13 = var5 * var9;
         double var15 = var7 * var9;
         double var17 = Math.sin(var1);
         double var19 = Math.cos(var1);
         double var21 = 1.0 - var19;
         double var23 = var11 * var15;
         double var25 = var11 * var13;
         double var27 = var13 * var15;
         this.mxx = var21 * var11 * var11 + var19;
         this.mxy = var21 * var25 - var17 * var15;
         this.mxz = var21 * var23 + var17 * var13;
         this.mxt = 0.0;
         this.myx = var21 * var25 + var17 * var15;
         this.myy = var21 * var13 * var13 + var19;
         this.myz = var21 * var27 - var17 * var11;
         this.myt = 0.0;
         this.mzx = var21 * var23 - var17 * var13;
         this.mzy = var21 * var27 + var17 * var11;
         this.mzz = var21 * var15 * var15 + var19;
         this.mzt = 0.0;
         this.updateState();
      }
   }

   public BaseBounds transform(BaseBounds var1, BaseBounds var2) {
      if ((this.state & 8) == 0) {
         return super.transform(var1, var2);
      } else {
         switch (this.state) {
            case 0:
               if (var1 != var2) {
                  var2 = var2.deriveWithNewBounds(var1);
               }
               break;
            case 1:
               var2 = var2.deriveWithNewBounds((float)((double)var1.getMinX() + this.mxt), (float)((double)var1.getMinY() + this.myt), (float)((double)var1.getMinZ() + this.mzt), (float)((double)var1.getMaxX() + this.mxt), (float)((double)var1.getMaxY() + this.myt), (float)((double)var1.getMaxZ() + this.mzt));
               break;
            case 2:
               var2 = var2.deriveWithNewBoundsAndSort((float)((double)var1.getMinX() * this.mxx), (float)((double)var1.getMinY() * this.myy), (float)((double)var1.getMinZ() * this.mzz), (float)((double)var1.getMaxX() * this.mxx), (float)((double)var1.getMaxY() * this.myy), (float)((double)var1.getMaxZ() * this.mzz));
               break;
            case 3:
               var2 = var2.deriveWithNewBoundsAndSort((float)((double)var1.getMinX() * this.mxx + this.mxt), (float)((double)var1.getMinY() * this.myy + this.myt), (float)((double)var1.getMinZ() * this.mzz + this.mzt), (float)((double)var1.getMaxX() * this.mxx + this.mxt), (float)((double)var1.getMaxY() * this.myy + this.myt), (float)((double)var1.getMaxZ() * this.mzz + this.mzt));
               break;
            case 4:
            case 5:
            case 6:
            case 7:
            default:
               Vec3d var3 = new Vec3d();
               var2 = TransformHelper.general3dBoundsTransform(this, var1, var2, var3);
         }

         return var2;
      }
   }

   public Vec3d transform(Vec3d var1, Vec3d var2) {
      if ((this.state & 8) == 0) {
         return super.transform(var1, var2);
      } else {
         if (var2 == null) {
            var2 = new Vec3d();
         }

         double var3 = var1.x;
         double var5 = var1.y;
         double var7 = var1.z;
         var2.x = this.mxx * var3 + this.mxy * var5 + this.mxz * var7 + this.mxt;
         var2.y = this.myx * var3 + this.myy * var5 + this.myz * var7 + this.myt;
         var2.z = this.mzx * var3 + this.mzy * var5 + this.mzz * var7 + this.mzt;
         return var2;
      }
   }

   public Vec3d deltaTransform(Vec3d var1, Vec3d var2) {
      if ((this.state & 8) == 0) {
         return super.deltaTransform(var1, var2);
      } else {
         if (var2 == null) {
            var2 = new Vec3d();
         }

         double var3 = var1.x;
         double var5 = var1.y;
         double var7 = var1.z;
         var2.x = this.mxx * var3 + this.mxy * var5 + this.mxz * var7;
         var2.y = this.myx * var3 + this.myy * var5 + this.myz * var7;
         var2.z = this.mzx * var3 + this.mzy * var5 + this.mzz * var7;
         return var2;
      }
   }

   public void inverseTransform(float[] var1, int var2, float[] var3, int var4, int var5) throws NoninvertibleTransformException {
      if ((this.state & 8) == 0) {
         super.inverseTransform(var1, var2, var3, var4, var5);
      } else {
         this.createInverse().transform(var1, var2, var3, var4, var5);
      }

   }

   public void inverseDeltaTransform(float[] var1, int var2, float[] var3, int var4, int var5) throws NoninvertibleTransformException {
      if ((this.state & 8) == 0) {
         super.inverseDeltaTransform(var1, var2, var3, var4, var5);
      } else {
         this.createInverse().deltaTransform(var1, var2, var3, var4, var5);
      }

   }

   public void inverseTransform(double[] var1, int var2, double[] var3, int var4, int var5) throws NoninvertibleTransformException {
      if ((this.state & 8) == 0) {
         super.inverseTransform(var1, var2, var3, var4, var5);
      } else {
         this.createInverse().transform(var1, var2, var3, var4, var5);
      }

   }

   public Point2D inverseTransform(Point2D var1, Point2D var2) throws NoninvertibleTransformException {
      return (this.state & 8) == 0 ? super.inverseTransform(var1, var2) : this.createInverse().transform(var1, var2);
   }

   public Vec3d inverseTransform(Vec3d var1, Vec3d var2) throws NoninvertibleTransformException {
      return (this.state & 8) == 0 ? super.inverseTransform(var1, var2) : this.createInverse().transform(var1, var2);
   }

   public Vec3d inverseDeltaTransform(Vec3d var1, Vec3d var2) throws NoninvertibleTransformException {
      return (this.state & 8) == 0 ? super.inverseDeltaTransform(var1, var2) : this.createInverse().deltaTransform(var1, var2);
   }

   public BaseBounds inverseTransform(BaseBounds var1, BaseBounds var2) throws NoninvertibleTransformException {
      if ((this.state & 8) == 0) {
         var2 = super.inverseTransform(var1, var2);
      } else {
         var2 = this.createInverse().transform(var1, var2);
      }

      return var2;
   }

   public void inverseTransform(Rectangle var1, Rectangle var2) throws NoninvertibleTransformException {
      if ((this.state & 8) == 0) {
         super.inverseTransform(var1, var2);
      } else {
         this.createInverse().transform(var1, var2);
      }

   }

   public BaseTransform createInverse() throws NoninvertibleTransformException {
      BaseTransform var1 = this.copy();
      var1.invert();
      return var1;
   }

   public void invert() throws NoninvertibleTransformException {
      if ((this.state & 8) == 0) {
         super.invert();
      } else {
         double var1 = this.minor(0, 0);
         double var3 = -this.minor(0, 1);
         double var5 = this.minor(0, 2);
         double var7 = -this.minor(1, 0);
         double var9 = this.minor(1, 1);
         double var11 = -this.minor(1, 2);
         double var13 = this.minor(2, 0);
         double var15 = -this.minor(2, 1);
         double var17 = this.minor(2, 2);
         double var19 = -this.minor(3, 0);
         double var21 = this.minor(3, 1);
         double var23 = -this.minor(3, 2);
         double var25 = this.getDeterminant();
         this.mxx = var1 / var25;
         this.mxy = var7 / var25;
         this.mxz = var13 / var25;
         this.mxt = var19 / var25;
         this.myx = var3 / var25;
         this.myy = var9 / var25;
         this.myz = var15 / var25;
         this.myt = var21 / var25;
         this.mzx = var5 / var25;
         this.mzy = var11 / var25;
         this.mzz = var17 / var25;
         this.mzt = var23 / var25;
         this.updateState();
      }
   }

   private double minor(int var1, int var2) {
      double var3 = this.mxx;
      double var5 = this.mxy;
      double var7 = this.mxz;
      double var9 = this.myx;
      double var11 = this.myy;
      double var13 = this.myz;
      double var15 = this.mzx;
      double var17 = this.mzy;
      double var19 = this.mzz;
      switch (var2) {
         case 0:
            var3 = var5;
            var9 = var11;
            var15 = var17;
         case 1:
            var5 = var7;
            var11 = var13;
            var17 = var19;
         case 2:
            var7 = this.mxt;
            var13 = this.myt;
            var19 = this.mzt;
         default:
            switch (var1) {
               case 0:
                  var3 = var9;
                  var5 = var11;
               case 1:
                  var9 = var15;
                  var11 = var17;
               case 2:
               default:
                  return var3 * var11 - var5 * var9;
               case 3:
                  return var3 * (var11 * var19 - var17 * var13) + var5 * (var13 * var15 - var19 * var9) + var7 * (var9 * var17 - var15 * var11);
            }
      }
   }

   public Affine3D deriveWithNewTransform(BaseTransform var1) {
      this.setTransform(var1);
      return this;
   }

   public Affine3D deriveWithTranslation(double var1, double var3) {
      this.translate(var1, var3, 0.0);
      return this;
   }

   public void translate(double var1, double var3) {
      if ((this.state & 8) == 0) {
         super.translate(var1, var3);
      } else {
         this.translate(var1, var3, 0.0);
      }

   }

   public void translate(double var1, double var3, double var5) {
      if ((this.state & 8) == 0) {
         super.translate(var1, var3);
         if (var5 != 0.0) {
            this.mzt = var5;
            this.state |= 8;
            if (this.type != -1) {
               this.type |= 128;
            }
         }

      } else {
         this.mxt += var1 * this.mxx + var3 * this.mxy + var5 * this.mxz;
         this.myt += var1 * this.myx + var3 * this.myy + var5 * this.myz;
         this.mzt += var1 * this.mzx + var3 * this.mzy + var5 * this.mzz;
         this.updateState();
      }
   }

   public Affine3D deriveWithPreTranslation(double var1, double var3) {
      this.preTranslate(var1, var3, 0.0);
      return this;
   }

   public BaseTransform deriveWithTranslation(double var1, double var3, double var5) {
      this.translate(var1, var3, var5);
      return this;
   }

   public BaseTransform deriveWithScale(double var1, double var3, double var5) {
      this.scale(var1, var3, var5);
      return this;
   }

   public BaseTransform deriveWithRotation(double var1, double var3, double var5, double var7) {
      this.rotate(var1, var3, var5, var7);
      return this;
   }

   public void preTranslate(double var1, double var3, double var5) {
      this.mxt += var1;
      this.myt += var3;
      this.mzt += var5;
      byte var7 = 0;
      int var8 = 0;
      if (this.mzt == 0.0) {
         if ((this.state & 8) != 0) {
            this.updateState();
            return;
         }
      } else {
         this.state |= 8;
         var8 = 128;
      }

      if (this.mxt == 0.0 && this.myt == 0.0) {
         this.state &= -2;
         var7 = 1;
      } else {
         this.state |= 1;
         var8 |= 1;
      }

      if (this.type != -1) {
         this.type = this.type & ~var7 | var8;
      }

   }

   public void scale(double var1, double var3) {
      if ((this.state & 8) == 0) {
         super.scale(var1, var3);
      } else {
         this.scale(var1, var3, 1.0);
      }

   }

   public void scale(double var1, double var3, double var5) {
      if ((this.state & 8) == 0) {
         super.scale(var1, var3);
         if (var5 != 1.0) {
            this.mzz = var5;
            this.state |= 8;
            if (this.type != -1) {
               this.type |= 128;
            }
         }

      } else {
         this.mxx *= var1;
         this.mxy *= var3;
         this.mxz *= var5;
         this.myx *= var1;
         this.myy *= var3;
         this.myz *= var5;
         this.mzx *= var1;
         this.mzy *= var3;
         this.mzz *= var5;
         this.updateState();
      }
   }

   public void rotate(double var1) {
      if ((this.state & 8) == 0) {
         super.rotate(var1);
      } else {
         this.rotate(var1, 0.0, 0.0, 1.0);
      }

   }

   public void rotate(double var1, double var3, double var5, double var7) {
      if ((this.state & 8) == 0 && almostZero(var3) && almostZero(var5)) {
         if (var7 > 0.0) {
            super.rotate(var1);
         } else if (var7 < 0.0) {
            super.rotate(-var1);
         }

      } else {
         double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
         if (!almostZero(var9)) {
            var9 = 1.0 / var9;
            double var11 = var3 * var9;
            double var13 = var5 * var9;
            double var15 = var7 * var9;
            double var17 = Math.sin(var1);
            double var19 = Math.cos(var1);
            double var21 = 1.0 - var19;
            double var23 = var11 * var15;
            double var25 = var11 * var13;
            double var27 = var13 * var15;
            double var29 = var21 * var11 * var11 + var19;
            double var31 = var21 * var25 - var17 * var15;
            double var33 = var21 * var23 + var17 * var13;
            double var35 = var21 * var25 + var17 * var15;
            double var37 = var21 * var13 * var13 + var19;
            double var39 = var21 * var27 - var17 * var11;
            double var41 = var21 * var23 - var17 * var13;
            double var43 = var21 * var27 + var17 * var11;
            double var45 = var21 * var15 * var15 + var19;
            double var47 = this.mxx * var29 + this.mxy * var35 + this.mxz * var41;
            double var49 = this.mxx * var31 + this.mxy * var37 + this.mxz * var43;
            double var51 = this.mxx * var33 + this.mxy * var39 + this.mxz * var45;
            double var53 = this.myx * var29 + this.myy * var35 + this.myz * var41;
            double var55 = this.myx * var31 + this.myy * var37 + this.myz * var43;
            double var57 = this.myx * var33 + this.myy * var39 + this.myz * var45;
            double var59 = this.mzx * var29 + this.mzy * var35 + this.mzz * var41;
            double var61 = this.mzx * var31 + this.mzy * var37 + this.mzz * var43;
            double var63 = this.mzx * var33 + this.mzy * var39 + this.mzz * var45;
            this.mxx = var47;
            this.mxy = var49;
            this.mxz = var51;
            this.myx = var53;
            this.myy = var55;
            this.myz = var57;
            this.mzx = var59;
            this.mzy = var61;
            this.mzz = var63;
            this.updateState();
         }
      }
   }

   public void shear(double var1, double var3) {
      if ((this.state & 8) == 0) {
         super.shear(var1, var3);
      } else {
         double var5 = this.mxx + this.mxy * var3;
         double var7 = this.mxy + this.mxx * var1;
         double var9 = this.myx + this.myy * var3;
         double var11 = this.myy + this.myx * var1;
         double var13 = this.mzx + this.mzy * var3;
         double var15 = this.mzy + this.mzx * var1;
         this.mxx = var5;
         this.mxy = var7;
         this.myx = var9;
         this.myy = var11;
         this.mzx = var13;
         this.mzy = var15;
         this.updateState();
      }
   }

   public Affine3D deriveWithConcatenation(BaseTransform var1) {
      this.concatenate(var1);
      return this;
   }

   public Affine3D deriveWithPreConcatenation(BaseTransform var1) {
      this.preConcatenate(var1);
      return this;
   }

   public void concatenate(BaseTransform var1) {
      switch (var1.getDegree()) {
         case IDENTITY:
            return;
         case TRANSLATE_2D:
            this.translate(var1.getMxt(), var1.getMyt());
            return;
         case TRANSLATE_3D:
            this.translate(var1.getMxt(), var1.getMyt(), var1.getMzt());
            return;
         case AFFINE_3D:
            if (!var1.is2D()) {
               break;
            }
         case AFFINE_2D:
            if ((this.state & 8) == 0) {
               super.concatenate(var1);
               return;
            }
      }

      double var2 = var1.getMxx();
      double var4 = var1.getMxy();
      double var6 = var1.getMxz();
      double var8 = var1.getMxt();
      double var10 = var1.getMyx();
      double var12 = var1.getMyy();
      double var14 = var1.getMyz();
      double var16 = var1.getMyt();
      double var18 = var1.getMzx();
      double var20 = var1.getMzy();
      double var22 = var1.getMzz();
      double var24 = var1.getMzt();
      double var26 = this.mxx * var2 + this.mxy * var10 + this.mxz * var18;
      double var28 = this.mxx * var4 + this.mxy * var12 + this.mxz * var20;
      double var30 = this.mxx * var6 + this.mxy * var14 + this.mxz * var22;
      double var32 = this.mxx * var8 + this.mxy * var16 + this.mxz * var24 + this.mxt;
      double var34 = this.myx * var2 + this.myy * var10 + this.myz * var18;
      double var36 = this.myx * var4 + this.myy * var12 + this.myz * var20;
      double var38 = this.myx * var6 + this.myy * var14 + this.myz * var22;
      double var40 = this.myx * var8 + this.myy * var16 + this.myz * var24 + this.myt;
      double var42 = this.mzx * var2 + this.mzy * var10 + this.mzz * var18;
      double var44 = this.mzx * var4 + this.mzy * var12 + this.mzz * var20;
      double var46 = this.mzx * var6 + this.mzy * var14 + this.mzz * var22;
      double var48 = this.mzx * var8 + this.mzy * var16 + this.mzz * var24 + this.mzt;
      this.mxx = var26;
      this.mxy = var28;
      this.mxz = var30;
      this.mxt = var32;
      this.myx = var34;
      this.myy = var36;
      this.myz = var38;
      this.myt = var40;
      this.mzx = var42;
      this.mzy = var44;
      this.mzz = var46;
      this.mzt = var48;
      this.updateState();
   }

   public void concatenate(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      double var25 = this.mxx * var1 + this.mxy * var9 + this.mxz * var17;
      double var27 = this.mxx * var3 + this.mxy * var11 + this.mxz * var19;
      double var29 = this.mxx * var5 + this.mxy * var13 + this.mxz * var21;
      double var31 = this.mxx * var7 + this.mxy * var15 + this.mxz * var23 + this.mxt;
      double var33 = this.myx * var1 + this.myy * var9 + this.myz * var17;
      double var35 = this.myx * var3 + this.myy * var11 + this.myz * var19;
      double var37 = this.myx * var5 + this.myy * var13 + this.myz * var21;
      double var39 = this.myx * var7 + this.myy * var15 + this.myz * var23 + this.myt;
      double var41 = this.mzx * var1 + this.mzy * var9 + this.mzz * var17;
      double var43 = this.mzx * var3 + this.mzy * var11 + this.mzz * var19;
      double var45 = this.mzx * var5 + this.mzy * var13 + this.mzz * var21;
      double var47 = this.mzx * var7 + this.mzy * var15 + this.mzz * var23 + this.mzt;
      this.mxx = var25;
      this.mxy = var27;
      this.mxz = var29;
      this.mxt = var31;
      this.myx = var33;
      this.myy = var35;
      this.myz = var37;
      this.myt = var39;
      this.mzx = var41;
      this.mzy = var43;
      this.mzz = var45;
      this.mzt = var47;
      this.updateState();
   }

   public Affine3D deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = this.mxx * var1 + this.mxy * var3;
      double var15 = this.mxx * var5 + this.mxy * var7;
      double var17 = this.mxx * var9 + this.mxy * var11 + this.mxt;
      double var19 = this.myx * var1 + this.myy * var3;
      double var21 = this.myx * var5 + this.myy * var7;
      double var23 = this.myx * var9 + this.myy * var11 + this.myt;
      double var25 = this.mzx * var1 + this.mzy * var3;
      double var27 = this.mzx * var5 + this.mzy * var7;
      double var29 = this.mzx * var9 + this.mzy * var11 + this.mzt;
      this.mxx = var13;
      this.mxy = var15;
      this.mxt = var17;
      this.myx = var19;
      this.myy = var21;
      this.myt = var23;
      this.mzx = var25;
      this.mzy = var27;
      this.mzt = var29;
      this.updateState();
      return this;
   }

   public BaseTransform deriveWithConcatenation(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      this.concatenate(var1, var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23);
      return this;
   }

   public void preConcatenate(BaseTransform var1) {
      switch (var1.getDegree()) {
         case IDENTITY:
            return;
         case TRANSLATE_2D:
            this.preTranslate(var1.getMxt(), var1.getMyt(), 0.0);
            return;
         case TRANSLATE_3D:
            this.preTranslate(var1.getMxt(), var1.getMyt(), var1.getMzt());
            return;
         default:
            double var2 = var1.getMxx();
            double var4 = var1.getMxy();
            double var6 = var1.getMxz();
            double var8 = var1.getMxt();
            double var10 = var1.getMyx();
            double var12 = var1.getMyy();
            double var14 = var1.getMyz();
            double var16 = var1.getMyt();
            double var18 = var1.getMzx();
            double var20 = var1.getMzy();
            double var22 = var1.getMzz();
            double var24 = var1.getMzt();
            double var26 = var2 * this.mxx + var4 * this.myx + var6 * this.mzx;
            double var28 = var2 * this.mxy + var4 * this.myy + var6 * this.mzy;
            double var30 = var2 * this.mxz + var4 * this.myz + var6 * this.mzz;
            double var32 = var2 * this.mxt + var4 * this.myt + var6 * this.mzt + var8;
            double var34 = var10 * this.mxx + var12 * this.myx + var14 * this.mzx;
            double var36 = var10 * this.mxy + var12 * this.myy + var14 * this.mzy;
            double var38 = var10 * this.mxz + var12 * this.myz + var14 * this.mzz;
            double var40 = var10 * this.mxt + var12 * this.myt + var14 * this.mzt + var16;
            double var42 = var18 * this.mxx + var20 * this.myx + var22 * this.mzx;
            double var44 = var18 * this.mxy + var20 * this.myy + var22 * this.mzy;
            double var46 = var18 * this.mxz + var20 * this.myz + var22 * this.mzz;
            double var48 = var18 * this.mxt + var20 * this.myt + var22 * this.mzt + var24;
            this.mxx = var26;
            this.mxy = var28;
            this.mxz = var30;
            this.mxt = var32;
            this.myx = var34;
            this.myy = var36;
            this.myz = var38;
            this.myt = var40;
            this.mzx = var42;
            this.mzy = var44;
            this.mzz = var46;
            this.mzt = var48;
            this.updateState();
      }
   }

   public void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11) {
      throw new InternalError("must use Affine3D restore method to prevent loss of information");
   }

   public void restoreTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      this.mxx = var1;
      this.mxy = var3;
      this.mxz = var5;
      this.mxt = var7;
      this.myx = var9;
      this.myy = var11;
      this.myz = var13;
      this.myt = var15;
      this.mzx = var17;
      this.mzy = var19;
      this.mzz = var21;
      this.mzt = var23;
      this.updateState();
   }

   public Affine3D lookAt(Vec3d var1, Vec3d var2, Vec3d var3) {
      double var4 = var1.x - var2.x;
      double var6 = var1.y - var2.y;
      double var8 = var1.z - var2.z;
      double var10 = 1.0 / Math.sqrt(var4 * var4 + var6 * var6 + var8 * var8);
      var4 *= var10;
      var6 *= var10;
      var8 *= var10;
      var10 = 1.0 / Math.sqrt(var3.x * var3.x + var3.y * var3.y + var3.z * var3.z);
      double var12 = var3.x * var10;
      double var14 = var3.y * var10;
      double var16 = var3.z * var10;
      double var18 = var14 * var8 - var6 * var16;
      double var20 = var16 * var4 - var12 * var8;
      double var22 = var12 * var6 - var14 * var4;
      var10 = 1.0 / Math.sqrt(var18 * var18 + var20 * var20 + var22 * var22);
      var18 *= var10;
      var20 *= var10;
      var22 *= var10;
      var12 = var6 * var22 - var20 * var8;
      var14 = var8 * var18 - var4 * var22;
      var16 = var4 * var20 - var6 * var18;
      this.mxx = var18;
      this.mxy = var20;
      this.mxz = var22;
      this.myx = var12;
      this.myy = var14;
      this.myz = var16;
      this.mzx = var4;
      this.mzy = var6;
      this.mzz = var8;
      this.mxt = -var1.x * this.mxx + -var1.y * this.mxy + -var1.z * this.mxz;
      this.myt = -var1.x * this.myx + -var1.y * this.myy + -var1.z * this.myz;
      this.mzt = -var1.x * this.mzx + -var1.y * this.mzy + -var1.z * this.mzz;
      this.updateState();
      return this;
   }

   static boolean almostOne(double var0) {
      return var0 < 1.00001 && var0 > 0.99999;
   }

   private static double _matround(double var0) {
      return Math.rint(var0 * 1.0E15) / 1.0E15;
   }

   public String toString() {
      return "Affine3D[[" + _matround(this.mxx) + ", " + _matround(this.mxy) + ", " + _matround(this.mxz) + ", " + _matround(this.mxt) + "], [" + _matround(this.myx) + ", " + _matround(this.myy) + ", " + _matround(this.myz) + ", " + _matround(this.myt) + "], [" + _matround(this.mzx) + ", " + _matround(this.mzy) + ", " + _matround(this.mzz) + ", " + _matround(this.mzt) + "]]";
   }
}
