package javafx.scene.shape;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGArc;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.paint.Paint;

public class Arc extends Shape {
   private final Arc2D shape = new Arc2D();
   private DoubleProperty centerX;
   private DoubleProperty centerY;
   private final DoubleProperty radiusX = new DoublePropertyBase() {
      public void invalidated() {
         Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
         Arc.this.impl_geomChanged();
      }

      public Object getBean() {
         return Arc.this;
      }

      public String getName() {
         return "radiusX";
      }
   };
   private final DoubleProperty radiusY = new DoublePropertyBase() {
      public void invalidated() {
         Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
         Arc.this.impl_geomChanged();
      }

      public Object getBean() {
         return Arc.this;
      }

      public String getName() {
         return "radiusY";
      }
   };
   private DoubleProperty startAngle;
   private final DoubleProperty length = new DoublePropertyBase() {
      public void invalidated() {
         Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
         Arc.this.impl_geomChanged();
      }

      public Object getBean() {
         return Arc.this;
      }

      public String getName() {
         return "length";
      }
   };
   private ObjectProperty type;

   public Arc() {
   }

   public Arc(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.setCenterX(var1);
      this.setCenterY(var3);
      this.setRadiusX(var5);
      this.setRadiusY(var7);
      this.setStartAngle(var9);
      this.setLength(var11);
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
               Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Arc.this.impl_geomChanged();
            }

            public Object getBean() {
               return Arc.this;
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
               Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Arc.this.impl_geomChanged();
            }

            public Object getBean() {
               return Arc.this;
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

   public final void setStartAngle(double var1) {
      if (this.startAngle != null || var1 != 0.0) {
         this.startAngleProperty().set(var1);
      }

   }

   public final double getStartAngle() {
      return this.startAngle == null ? 0.0 : this.startAngle.get();
   }

   public final DoubleProperty startAngleProperty() {
      if (this.startAngle == null) {
         this.startAngle = new DoublePropertyBase() {
            public void invalidated() {
               Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Arc.this.impl_geomChanged();
            }

            public Object getBean() {
               return Arc.this;
            }

            public String getName() {
               return "startAngle";
            }
         };
      }

      return this.startAngle;
   }

   public final void setLength(double var1) {
      this.length.set(var1);
   }

   public final double getLength() {
      return this.length.get();
   }

   public final DoubleProperty lengthProperty() {
      return this.length;
   }

   public final void setType(ArcType var1) {
      if (this.type != null || var1 != ArcType.OPEN) {
         this.typeProperty().set(var1);
      }

   }

   public final ArcType getType() {
      return this.type == null ? ArcType.OPEN : (ArcType)this.type.get();
   }

   public final ObjectProperty typeProperty() {
      if (this.type == null) {
         this.type = new ObjectPropertyBase(ArcType.OPEN) {
            public void invalidated() {
               Arc.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               Arc.this.impl_geomChanged();
            }

            public Object getBean() {
               return Arc.this;
            }

            public String getName() {
               return "type";
            }
         };
      }

      return this.type;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGArc();
   }

   /** @deprecated */
   @Deprecated
   public Arc2D impl_configShape() {
      byte var1;
      switch (this.getTypeInternal()) {
         case OPEN:
            var1 = 0;
            break;
         case CHORD:
            var1 = 1;
            break;
         default:
            var1 = 2;
      }

      this.shape.setArc((float)(this.getCenterX() - this.getRadiusX()), (float)(this.getCenterY() - this.getRadiusY()), (float)(this.getRadiusX() * 2.0), (float)(this.getRadiusY() * 2.0), (float)this.getStartAngle(), (float)this.getLength(), var1);
      return this.shape;
   }

   private final ArcType getTypeInternal() {
      ArcType var1 = this.getType();
      return var1 == null ? ArcType.OPEN : var1;
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         NGArc var1 = (NGArc)this.impl_getPeer();
         var1.updateArc((float)this.getCenterX(), (float)this.getCenterY(), (float)this.getRadiusX(), (float)this.getRadiusY(), (float)this.getStartAngle(), (float)this.getLength(), this.getTypeInternal());
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Arc[");
      String var2 = this.getId();
      if (var2 != null) {
         var1.append("id=").append(var2).append(", ");
      }

      var1.append("centerX=").append(this.getCenterX());
      var1.append(", centerY=").append(this.getCenterY());
      var1.append(", radiusX=").append(this.getRadiusX());
      var1.append(", radiusY=").append(this.getRadiusY());
      var1.append(", startAngle=").append(this.getStartAngle());
      var1.append(", length=").append(this.getLength());
      var1.append(", type=").append(this.getType());
      var1.append(", fill=").append(this.getFill());
      Paint var3 = this.getStroke();
      if (var3 != null) {
         var1.append(", stroke=").append(var3);
         var1.append(", strokeWidth=").append(this.getStrokeWidth());
      }

      return var1.append("]").toString();
   }
}
