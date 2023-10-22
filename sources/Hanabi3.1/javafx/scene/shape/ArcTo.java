package javafx.scene.shape;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;

public class ArcTo extends PathElement {
   private DoubleProperty radiusX = new DoublePropertyBase() {
      public void invalidated() {
         ArcTo.this.u();
      }

      public Object getBean() {
         return ArcTo.this;
      }

      public String getName() {
         return "radiusX";
      }
   };
   private DoubleProperty radiusY = new DoublePropertyBase() {
      public void invalidated() {
         ArcTo.this.u();
      }

      public Object getBean() {
         return ArcTo.this;
      }

      public String getName() {
         return "radiusY";
      }
   };
   private DoubleProperty xAxisRotation;
   private BooleanProperty largeArcFlag;
   private BooleanProperty sweepFlag;
   private DoubleProperty x;
   private DoubleProperty y;

   public ArcTo() {
   }

   public ArcTo(double var1, double var3, double var5, double var7, double var9, boolean var11, boolean var12) {
      this.setRadiusX(var1);
      this.setRadiusY(var3);
      this.setXAxisRotation(var5);
      this.setX(var7);
      this.setY(var9);
      this.setLargeArcFlag(var11);
      this.setSweepFlag(var12);
   }

   public final void setRadiusX(double var1) {
      this.radiusX.set(var1);
   }

   public final double getRadiusX() {
      return this.radiusX.get();
   }

   public final DoubleProperty radiusXProperty() {
      return this.radiusX;
   }

   public final void setRadiusY(double var1) {
      this.radiusY.set(var1);
   }

   public final double getRadiusY() {
      return this.radiusY.get();
   }

   public final DoubleProperty radiusYProperty() {
      return this.radiusY;
   }

   public final void setXAxisRotation(double var1) {
      if (this.xAxisRotation != null || var1 != 0.0) {
         this.XAxisRotationProperty().set(var1);
      }

   }

   public final double getXAxisRotation() {
      return this.xAxisRotation == null ? 0.0 : this.xAxisRotation.get();
   }

   public final DoubleProperty XAxisRotationProperty() {
      if (this.xAxisRotation == null) {
         this.xAxisRotation = new DoublePropertyBase() {
            public void invalidated() {
               ArcTo.this.u();
            }

            public Object getBean() {
               return ArcTo.this;
            }

            public String getName() {
               return "XAxisRotation";
            }
         };
      }

      return this.xAxisRotation;
   }

   public final void setLargeArcFlag(boolean var1) {
      if (this.largeArcFlag != null || var1) {
         this.largeArcFlagProperty().set(var1);
      }

   }

   public final boolean isLargeArcFlag() {
      return this.largeArcFlag == null ? false : this.largeArcFlag.get();
   }

   public final BooleanProperty largeArcFlagProperty() {
      if (this.largeArcFlag == null) {
         this.largeArcFlag = new BooleanPropertyBase() {
            public void invalidated() {
               ArcTo.this.u();
            }

            public Object getBean() {
               return ArcTo.this;
            }

            public String getName() {
               return "largeArcFlag";
            }
         };
      }

      return this.largeArcFlag;
   }

   public final void setSweepFlag(boolean var1) {
      if (this.sweepFlag != null || var1) {
         this.sweepFlagProperty().set(var1);
      }

   }

   public final boolean isSweepFlag() {
      return this.sweepFlag == null ? false : this.sweepFlag.get();
   }

   public final BooleanProperty sweepFlagProperty() {
      if (this.sweepFlag == null) {
         this.sweepFlag = new BooleanPropertyBase() {
            public void invalidated() {
               ArcTo.this.u();
            }

            public Object getBean() {
               return ArcTo.this;
            }

            public String getName() {
               return "sweepFlag";
            }
         };
      }

      return this.sweepFlag;
   }

   public final void setX(double var1) {
      if (this.x != null || var1 != 0.0) {
         this.xProperty().set(var1);
      }

   }

   public final double getX() {
      return this.x == null ? 0.0 : this.x.get();
   }

   public final DoubleProperty xProperty() {
      if (this.x == null) {
         this.x = new DoublePropertyBase() {
            public void invalidated() {
               ArcTo.this.u();
            }

            public Object getBean() {
               return ArcTo.this;
            }

            public String getName() {
               return "x";
            }
         };
      }

      return this.x;
   }

   public final void setY(double var1) {
      if (this.y != null || var1 != 0.0) {
         this.yProperty().set(var1);
      }

   }

   public final double getY() {
      return this.y == null ? 0.0 : this.y.get();
   }

   public final DoubleProperty yProperty() {
      if (this.y == null) {
         this.y = new DoublePropertyBase() {
            public void invalidated() {
               ArcTo.this.u();
            }

            public Object getBean() {
               return ArcTo.this;
            }

            public String getName() {
               return "y";
            }
         };
      }

      return this.y;
   }

   void addTo(NGPath var1) {
      this.addArcTo(var1, (Path2D)null, (double)var1.getCurrentX(), (double)var1.getCurrentY());
   }

   /** @deprecated */
   @Deprecated
   public void impl_addTo(Path2D var1) {
      this.addArcTo((NGPath)null, var1, (double)var1.getCurrentX(), (double)var1.getCurrentY());
   }

   private void addArcTo(NGPath var1, Path2D var2, double var3, double var5) {
      double var7 = this.getX();
      double var9 = this.getY();
      boolean var11 = this.isSweepFlag();
      boolean var12 = this.isLargeArcFlag();
      double var13 = this.isAbsolute() ? var7 : var7 + var3;
      double var15 = this.isAbsolute() ? var9 : var9 + var5;
      double var17 = (var3 - var13) / 2.0;
      double var19 = (var5 - var15) / 2.0;
      double var21 = Math.toRadians(this.getXAxisRotation());
      double var23 = Math.cos(var21);
      double var25 = Math.sin(var21);
      double var27 = var23 * var17 + var25 * var19;
      double var29 = -var25 * var17 + var23 * var19;
      double var31 = Math.abs(this.getRadiusX());
      double var33 = Math.abs(this.getRadiusY());
      double var35 = var31 * var31;
      double var37 = var33 * var33;
      double var39 = var27 * var27;
      double var41 = var29 * var29;
      double var43 = var39 / var35 + var41 / var37;
      if (var43 > 1.0) {
         var31 = Math.sqrt(var43) * var31;
         var33 = Math.sqrt(var43) * var33;
         if (var31 != var31 || var33 != var33) {
            if (var1 == null) {
               var2.lineTo((float)var13, (float)var15);
            } else {
               var1.addLineTo((float)var13, (float)var15);
            }

            return;
         }

         var35 = var31 * var31;
         var37 = var33 * var33;
      }

      double var45 = var12 == var11 ? -1.0 : 1.0;
      double var47 = (var35 * var37 - var35 * var41 - var37 * var39) / (var35 * var41 + var37 * var39);
      var47 = var47 < 0.0 ? 0.0 : var47;
      double var49 = var45 * Math.sqrt(var47);
      double var51 = var49 * (var31 * var29 / var33);
      double var53 = var49 * -(var33 * var27 / var31);
      double var55 = (var3 + var13) / 2.0;
      double var57 = (var5 + var15) / 2.0;
      double var59 = var55 + (var23 * var51 - var25 * var53);
      double var61 = var57 + var25 * var51 + var23 * var53;
      double var63 = (var27 - var51) / var31;
      double var65 = (var29 - var53) / var33;
      double var67 = (-var27 - var51) / var31;
      double var69 = (-var29 - var53) / var33;
      double var71 = Math.sqrt(var63 * var63 + var65 * var65);
      var45 = var65 < 0.0 ? -1.0 : 1.0;
      double var75 = Math.toDegrees(var45 * Math.acos(var63 / var71));
      var71 = Math.sqrt((var63 * var63 + var65 * var65) * (var67 * var67 + var69 * var69));
      double var73 = var63 * var67 + var65 * var69;
      var45 = var63 * var69 - var65 * var67 < 0.0 ? -1.0 : 1.0;
      double var77 = Math.toDegrees(var45 * Math.acos(var73 / var71));
      if (!var11 && var77 > 0.0) {
         var77 -= 360.0;
      } else if (var11 && var77 < 0.0) {
         var77 += 360.0;
      }

      var77 %= 360.0;
      var75 %= 360.0;
      float var79 = (float)(var59 - var31);
      float var80 = (float)(var61 - var33);
      float var81 = (float)(var31 * 2.0);
      float var82 = (float)(var33 * 2.0);
      float var83 = (float)(-var75);
      float var84 = (float)(-var77);
      if (var1 == null) {
         Arc2D var85 = new Arc2D(var79, var80, var81, var82, var83, var84, 0);
         BaseTransform var86 = var21 == 0.0 ? null : BaseTransform.getRotateInstance(var21, var59, var61);
         PathIterator var87 = var85.getPathIterator(var86);
         var87.next();
         var2.append(var87, true);
      } else {
         var1.addArcTo(var79, var80, var81, var82, var83, var84, (float)var21);
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("ArcTo[");
      var1.append("x=").append(this.getX());
      var1.append(", y=").append(this.getY());
      var1.append(", radiusX=").append(this.getRadiusX());
      var1.append(", radiusY=").append(this.getRadiusY());
      var1.append(", xAxisRotation=").append(this.getXAxisRotation());
      if (this.isLargeArcFlag()) {
         var1.append(", lartArcFlag");
      }

      if (this.isSweepFlag()) {
         var1.append(", sweepFlag");
      }

      return var1.append("]").toString();
   }
}
