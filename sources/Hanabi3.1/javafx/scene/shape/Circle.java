package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGCircle;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;

public class Circle extends Shape {
   private final Ellipse2D shape = new Ellipse2D();
   private DoubleProperty centerX;
   private DoubleProperty centerY;
   private final DoubleProperty radius = new DoublePropertyBase() {
      public void invalidated() {
         Circle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
         Circle.this.impl_geomChanged();
      }

      public Object getBean() {
         return Circle.this;
      }

      public String getName() {
         return "radius";
      }
   };

   public Circle(double var1) {
      this.setRadius(var1);
   }

   public Circle(double var1, Paint var3) {
      this.setRadius(var1);
      this.setFill(var3);
   }

   public Circle() {
   }

   public Circle(double var1, double var3, double var5) {
      this.setCenterX(var1);
      this.setCenterY(var3);
      this.setRadius(var5);
   }

   public Circle(double var1, double var3, double var5, Paint var7) {
      this.setCenterX(var1);
      this.setCenterY(var3);
      this.setRadius(var5);
      this.setFill(var7);
   }

   public final void setCenterX(double var1) {
      if (this.centerX != null || var1 != 0.0) {
         this.centerXProperty().set(var1);
      }

   }

   public final double getCenterX() {
      return this.centerX == null ? 0.0 : this.centerX.get();
   }

   public final DoubleProperty centerXProperty() {
      if (this.centerX == null) {
         this.centerX = new DoublePropertyBase(0.0) {
            public void invalidated() {
               Circle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Circle.this.impl_geomChanged();
            }

            public Object getBean() {
               return Circle.this;
            }

            public String getName() {
               return "centerX";
            }
         };
      }

      return this.centerX;
   }

   public final void setCenterY(double var1) {
      if (this.centerY != null || var1 != 0.0) {
         this.centerYProperty().set(var1);
      }

   }

   public final double getCenterY() {
      return this.centerY == null ? 0.0 : this.centerY.get();
   }

   public final DoubleProperty centerYProperty() {
      if (this.centerY == null) {
         this.centerY = new DoublePropertyBase(0.0) {
            public void invalidated() {
               Circle.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Circle.this.impl_geomChanged();
            }

            public Object getBean() {
               return Circle.this;
            }

            public String getName() {
               return "centerY";
            }
         };
      }

      return this.centerY;
   }

   public final void setRadius(double var1) {
      this.radius.set(var1);
   }

   public final double getRadius() {
      return this.radius.get();
   }

   public final DoubleProperty radiusProperty() {
      return this.radius;
   }

   StrokeLineJoin convertLineJoin(StrokeLineJoin var1) {
      return StrokeLineJoin.BEVEL;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGCircle();
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      if (this.impl_mode == NGShape.Mode.EMPTY) {
         return var1.makeEmpty();
      } else {
         double var3 = this.getCenterX();
         double var5 = this.getCenterY();
         double var7;
         double var9;
         double var11;
         double var13;
         if ((var2.getType() & -26) == 0) {
            var7 = var3 * var2.getMxx() + var5 * var2.getMxy() + var2.getMxt();
            var9 = var3 * var2.getMyx() + var5 * var2.getMyy() + var2.getMyt();
            var11 = this.getRadius();
            if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
               var13 = this.getStrokeWidth();
               if (this.getStrokeType() == StrokeType.CENTERED) {
                  var13 /= 2.0;
               }

               var11 += var13;
            }

            return var1.deriveWithNewBounds((float)(var7 - var11), (float)(var9 - var11), 0.0F, (float)(var7 + var11), (float)(var9 + var11), 0.0F);
         } else if ((var2.getType() & -72) != 0) {
            return this.computeShapeBounds(var1, var2, this.impl_configShape());
         } else {
            var7 = this.getRadius();
            var9 = this.getCenterX() - var7;
            var11 = this.getCenterY() - var7;
            var13 = 2.0 * var7;
            double var17;
            if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
               var17 = this.getStrokeWidth();
            } else {
               var17 = 0.0;
            }

            return this.computeBounds(var1, var2, var17, 0.0, var9, var11, var13, var13);
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public Ellipse2D impl_configShape() {
      double var1 = this.getRadius();
      this.shape.setFrame((float)(this.getCenterX() - var1), (float)(this.getCenterY() - var1), (float)(var1 * 2.0), (float)(var1 * 2.0));
      return this.shape;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         NGCircle var1 = (NGCircle)this.impl_getPeer();
         var1.updateCircle((float)this.getCenterX(), (float)this.getCenterY(), (float)this.getRadius());
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Circle[");
      String var2 = this.getId();
      if (var2 != null) {
         var1.append("id=").append(var2).append(", ");
      }

      var1.append("centerX=").append(this.getCenterX());
      var1.append(", centerY=").append(this.getCenterY());
      var1.append(", radius=").append(this.getRadius());
      var1.append(", fill=").append(this.getFill());
      Paint var3 = this.getStroke();
      if (var3 != null) {
         var1.append(", stroke=").append(var3);
         var1.append(", strokeWidth=").append(this.getStrokeWidth());
      }

      return var1.append("]").toString();
   }
}
