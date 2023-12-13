package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGEllipse;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;

public class Ellipse extends Shape {
   private final Ellipse2D shape;
   private static final int NON_RECTILINEAR_TYPE_MASK = -80;
   private DoubleProperty centerX;
   private DoubleProperty centerY;
   private final DoubleProperty radiusX;
   private final DoubleProperty radiusY;

   public Ellipse() {
      this.shape = new Ellipse2D();
      this.radiusX = new DoublePropertyBase() {
         public void invalidated() {
            Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Ellipse.this.impl_geomChanged();
         }

         public Object getBean() {
            return Ellipse.this;
         }

         public String getName() {
            return "radiusX";
         }
      };
      this.radiusY = new DoublePropertyBase() {
         public void invalidated() {
            Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Ellipse.this.impl_geomChanged();
         }

         public Object getBean() {
            return Ellipse.this;
         }

         public String getName() {
            return "radiusY";
         }
      };
   }

   public Ellipse(double var1, double var3) {
      this.shape = new Ellipse2D();
      this.radiusX = new DoublePropertyBase() {
         public void invalidated() {
            Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Ellipse.this.impl_geomChanged();
         }

         public Object getBean() {
            return Ellipse.this;
         }

         public String getName() {
            return "radiusX";
         }
      };
      this.radiusY = new DoublePropertyBase() {
         public void invalidated() {
            Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            Ellipse.this.impl_geomChanged();
         }

         public Object getBean() {
            return Ellipse.this;
         }

         public String getName() {
            return "radiusY";
         }
      };
      this.setRadiusX(var1);
      this.setRadiusY(var3);
   }

   public Ellipse(double var1, double var3, double var5, double var7) {
      this(var5, var7);
      this.setCenterX(var1);
      this.setCenterY(var3);
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
         this.centerX = new DoublePropertyBase() {
            public void invalidated() {
               Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Ellipse.this.impl_geomChanged();
            }

            public Object getBean() {
               return Ellipse.this;
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
         this.centerY = new DoublePropertyBase() {
            public void invalidated() {
               Ellipse.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Ellipse.this.impl_geomChanged();
            }

            public Object getBean() {
               return Ellipse.this;
            }

            public String getName() {
               return "centerY";
            }
         };
      }

      return this.centerY;
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

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGEllipse();
   }

   StrokeLineJoin convertLineJoin(StrokeLineJoin var1) {
      return StrokeLineJoin.BEVEL;
   }

   /** @deprecated */
   @Deprecated
   public BaseBounds impl_computeGeomBounds(BaseBounds var1, BaseTransform var2) {
      if (this.impl_mode == NGShape.Mode.EMPTY) {
         return var1.makeEmpty();
      } else if ((var2.getType() & -80) != 0) {
         return this.computeShapeBounds(var1, var2, this.impl_configShape());
      } else {
         double var3 = this.getCenterX() - this.getRadiusX();
         double var5 = this.getCenterY() - this.getRadiusY();
         double var7 = 2.0 * this.getRadiusX();
         double var9 = 2.0 * this.getRadiusY();
         double var11;
         double var13;
         if (this.impl_mode != NGShape.Mode.FILL && this.getStrokeType() != StrokeType.INSIDE) {
            var11 = this.getStrokeWidth();
            if (this.getStrokeType() == StrokeType.CENTERED) {
               var11 /= 2.0;
            }

            var13 = 0.0;
         } else {
            var13 = 0.0;
            var11 = 0.0;
         }

         return this.computeBounds(var1, var2, var11, var13, var3, var5, var7, var9);
      }
   }

   /** @deprecated */
   @Deprecated
   public Ellipse2D impl_configShape() {
      this.shape.setFrame((float)(this.getCenterX() - this.getRadiusX()), (float)(this.getCenterY() - this.getRadiusY()), (float)(this.getRadiusX() * 2.0), (float)(this.getRadiusY() * 2.0));
      return this.shape;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         NGEllipse var1 = (NGEllipse)this.impl_getPeer();
         var1.updateEllipse((float)this.getCenterX(), (float)this.getCenterY(), (float)this.getRadiusX(), (float)this.getRadiusY());
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Ellipse[");
      String var2 = this.getId();
      if (var2 != null) {
         var1.append("id=").append(var2).append(", ");
      }

      var1.append("centerX=").append(this.getCenterX());
      var1.append(", centerY=").append(this.getCenterY());
      var1.append(", radiusX=").append(this.getRadiusX());
      var1.append(", radiusY=").append(this.getRadiusY());
      var1.append(", fill=").append(this.getFill());
      Paint var3 = this.getStroke();
      if (var3 != null) {
         var1.append(", stroke=").append(var3);
         var1.append(", strokeWidth=").append(this.getStrokeWidth());
      }

      return var1.append("]").toString();
   }
}
