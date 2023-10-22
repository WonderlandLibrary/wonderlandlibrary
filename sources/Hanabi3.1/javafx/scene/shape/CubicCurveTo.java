package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;

public class CubicCurveTo extends PathElement {
   private DoubleProperty controlX1;
   private DoubleProperty controlY1;
   private DoubleProperty controlX2;
   private DoubleProperty controlY2;
   private DoubleProperty x;
   private DoubleProperty y;

   public CubicCurveTo() {
   }

   public CubicCurveTo(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.setControlX1(var1);
      this.setControlY1(var3);
      this.setControlX2(var5);
      this.setControlY2(var7);
      this.setX(var9);
      this.setY(var11);
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
               CubicCurveTo.this.u();
            }

            public Object getBean() {
               return CubicCurveTo.this;
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
               CubicCurveTo.this.u();
            }

            public Object getBean() {
               return CubicCurveTo.this;
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
               CubicCurveTo.this.u();
            }

            public Object getBean() {
               return CubicCurveTo.this;
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
               CubicCurveTo.this.u();
            }

            public Object getBean() {
               return CubicCurveTo.this;
            }

            public String getName() {
               return "controlY2";
            }
         };
      }

      return this.controlY2;
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
               CubicCurveTo.this.u();
            }

            public Object getBean() {
               return CubicCurveTo.this;
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
               CubicCurveTo.this.u();
            }

            public Object getBean() {
               return CubicCurveTo.this;
            }

            public String getName() {
               return "y";
            }
         };
      }

      return this.y;
   }

   void addTo(NGPath var1) {
      if (this.isAbsolute()) {
         var1.addCubicTo((float)this.getControlX1(), (float)this.getControlY1(), (float)this.getControlX2(), (float)this.getControlY2(), (float)this.getX(), (float)this.getY());
      } else {
         double var2 = (double)var1.getCurrentX();
         double var4 = (double)var1.getCurrentY();
         var1.addCubicTo((float)(this.getControlX1() + var2), (float)(this.getControlY1() + var4), (float)(this.getControlX2() + var2), (float)(this.getControlY2() + var4), (float)(this.getX() + var2), (float)(this.getY() + var4));
      }

   }

   /** @deprecated */
   @Deprecated
   public void impl_addTo(Path2D var1) {
      if (this.isAbsolute()) {
         var1.curveTo((float)this.getControlX1(), (float)this.getControlY1(), (float)this.getControlX2(), (float)this.getControlY2(), (float)this.getX(), (float)this.getY());
      } else {
         double var2 = (double)var1.getCurrentX();
         double var4 = (double)var1.getCurrentY();
         var1.curveTo((float)(this.getControlX1() + var2), (float)(this.getControlY1() + var4), (float)(this.getControlX2() + var2), (float)(this.getControlY2() + var4), (float)(this.getX() + var2), (float)(this.getY() + var4));
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("CubicCurveTo[");
      var1.append("x=").append(this.getX());
      var1.append(", y=").append(this.getY());
      var1.append(", controlX1=").append(this.getControlX1());
      var1.append(", controlY1=").append(this.getControlY1());
      var1.append(", controlX2=").append(this.getControlX2());
      var1.append(", controlY2=").append(this.getControlY2());
      return var1.append("]").toString();
   }
}
