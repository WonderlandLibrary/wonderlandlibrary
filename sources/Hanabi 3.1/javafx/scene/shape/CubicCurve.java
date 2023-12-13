package javafx.scene.shape;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGCubicCurve;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.paint.Paint;

public class CubicCurve extends Shape {
   private final CubicCurve2D shape = new CubicCurve2D();
   private DoubleProperty startX;
   private DoubleProperty startY;
   private DoubleProperty controlX1;
   private DoubleProperty controlY1;
   private DoubleProperty controlX2;
   private DoubleProperty controlY2;
   private DoubleProperty endX;
   private DoubleProperty endY;

   public CubicCurve() {
   }

   public CubicCurve(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      this.setStartX(var1);
      this.setStartY(var3);
      this.setControlX1(var5);
      this.setControlY1(var7);
      this.setControlX2(var9);
      this.setControlY2(var11);
      this.setEndX(var13);
      this.setEndY(var15);
   }

   public final void setStartX(double var1) {
      if (this.startX != null || var1 != 0.0) {
         this.startXProperty().set(var1);
      }

   }

   public final double getStartX() {
      return this.startX == null ? 0.0 : this.startX.get();
   }

   public final DoubleProperty startXProperty() {
      if (this.startX == null) {
         this.startX = new DoublePropertyBase() {
            public void invalidated() {
               CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               CubicCurve.this.impl_geomChanged();
            }

            public Object getBean() {
               return CubicCurve.this;
            }

            public String getName() {
               return "startX";
            }
         };
      }

      return this.startX;
   }

   public final void setStartY(double var1) {
      if (this.startY != null || var1 != 0.0) {
         this.startYProperty().set(var1);
      }

   }

   public final double getStartY() {
      return this.startY == null ? 0.0 : this.startY.get();
   }

   public final DoubleProperty startYProperty() {
      if (this.startY == null) {
         this.startY = new DoublePropertyBase() {
            public void invalidated() {
               CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               CubicCurve.this.impl_geomChanged();
            }

            public Object getBean() {
               return CubicCurve.this;
            }

            public String getName() {
               return "startY";
            }
         };
      }

      return this.startY;
   }

   public final void setControlX1(double var1) {
      if (this.controlX1 != null || var1 != 0.0) {
         this.controlX1Property().set(var1);
      }

   }

   public final double getControlX1() {
      return this.controlX1 == null ? 0.0 : this.controlX1.get();
   }

   public final DoubleProperty controlX1Property() {
      if (this.controlX1 == null) {
         this.controlX1 = new DoublePropertyBase() {
            public void invalidated() {
               CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               CubicCurve.this.impl_geomChanged();
            }

            public Object getBean() {
               return CubicCurve.this;
            }

            public String getName() {
               return "controlX1";
            }
         };
      }

      return this.controlX1;
   }

   public final void setControlY1(double var1) {
      if (this.controlY1 != null || var1 != 0.0) {
         this.controlY1Property().set(var1);
      }

   }

   public final double getControlY1() {
      return this.controlY1 == null ? 0.0 : this.controlY1.get();
   }

   public final DoubleProperty controlY1Property() {
      if (this.controlY1 == null) {
         this.controlY1 = new DoublePropertyBase() {
            public void invalidated() {
               CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               CubicCurve.this.impl_geomChanged();
            }

            public Object getBean() {
               return CubicCurve.this;
            }

            public String getName() {
               return "controlY1";
            }
         };
      }

      return this.controlY1;
   }

   public final void setControlX2(double var1) {
      if (this.controlX2 != null || var1 != 0.0) {
         this.controlX2Property().set(var1);
      }

   }

   public final double getControlX2() {
      return this.controlX2 == null ? 0.0 : this.controlX2.get();
   }

   public final DoubleProperty controlX2Property() {
      if (this.controlX2 == null) {
         this.controlX2 = new DoublePropertyBase() {
            public void invalidated() {
               CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               CubicCurve.this.impl_geomChanged();
            }

            public Object getBean() {
               return CubicCurve.this;
            }

            public String getName() {
               return "controlX2";
            }
         };
      }

      return this.controlX2;
   }

   public final void setControlY2(double var1) {
      if (this.controlY2 != null || var1 != 0.0) {
         this.controlY2Property().set(var1);
      }

   }

   public final double getControlY2() {
      return this.controlY2 == null ? 0.0 : this.controlY2.get();
   }

   public final DoubleProperty controlY2Property() {
      if (this.controlY2 == null) {
         this.controlY2 = new DoublePropertyBase() {
            public void invalidated() {
               CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               CubicCurve.this.impl_geomChanged();
            }

            public Object getBean() {
               return CubicCurve.this;
            }

            public String getName() {
               return "controlY2";
            }
         };
      }

      return this.controlY2;
   }

   public final void setEndX(double var1) {
      if (this.endX != null || var1 != 0.0) {
         this.endXProperty().set(var1);
      }

   }

   public final double getEndX() {
      return this.endX == null ? 0.0 : this.endX.get();
   }

   public final DoubleProperty endXProperty() {
      if (this.endX == null) {
         this.endX = new DoublePropertyBase() {
            public void invalidated() {
               CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               CubicCurve.this.impl_geomChanged();
            }

            public Object getBean() {
               return CubicCurve.this;
            }

            public String getName() {
               return "endX";
            }
         };
      }

      return this.endX;
   }

   public final void setEndY(double var1) {
      if (this.endY != null || var1 != 0.0) {
         this.endYProperty().set(var1);
      }

   }

   public final double getEndY() {
      return this.endY == null ? 0.0 : this.endY.get();
   }

   public final DoubleProperty endYProperty() {
      if (this.endY == null) {
         this.endY = new DoublePropertyBase() {
            public void invalidated() {
               CubicCurve.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
               CubicCurve.this.impl_geomChanged();
            }

            public Object getBean() {
               return CubicCurve.this;
            }

            public String getName() {
               return "endY";
            }
         };
      }

      return this.endY;
   }

   /** @deprecated */
   @Deprecated
   public CubicCurve2D impl_configShape() {
      this.shape.x1 = (float)this.getStartX();
      this.shape.y1 = (float)this.getStartY();
      this.shape.ctrlx1 = (float)this.getControlX1();
      this.shape.ctrly1 = (float)this.getControlY1();
      this.shape.ctrlx2 = (float)this.getControlX2();
      this.shape.ctrly2 = (float)this.getControlY2();
      this.shape.x2 = (float)this.getEndX();
      this.shape.y2 = (float)this.getEndY();
      return this.shape;
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGCubicCurve();
   }

   /** @deprecated */
   @Deprecated
   public void impl_updatePeer() {
      super.impl_updatePeer();
      if (this.impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
         NGCubicCurve var1 = (NGCubicCurve)this.impl_getPeer();
         var1.updateCubicCurve((float)this.getStartX(), (float)this.getStartY(), (float)this.getEndX(), (float)this.getEndY(), (float)this.getControlX1(), (float)this.getControlY1(), (float)this.getControlX2(), (float)this.getControlY2());
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("CubicCurve[");
      String var2 = this.getId();
      if (var2 != null) {
         var1.append("id=").append(var2).append(", ");
      }

      var1.append("startX=").append(this.getStartX());
      var1.append(", startY=").append(this.getStartY());
      var1.append(", controlX1=").append(this.getControlX1());
      var1.append(", controlY1=").append(this.getControlY1());
      var1.append(", controlX2=").append(this.getControlX2());
      var1.append(", controlY2=").append(this.getControlY2());
      var1.append(", endX=").append(this.getEndX());
      var1.append(", endY=").append(this.getEndY());
      var1.append(", fill=").append(this.getFill());
      Paint var3 = this.getStroke();
      if (var3 != null) {
         var1.append(", stroke=").append(var3);
         var1.append(", strokeWidth=").append(this.getStrokeWidth());
      }

      return var1.append("]").toString();
   }
}
