package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;

public class QuadCurveTo extends PathElement {
   private DoubleProperty controlX = new DoublePropertyBase() {
      public void invalidated() {
         QuadCurveTo.this.u();
      }

      public Object getBean() {
         return QuadCurveTo.this;
      }

      public String getName() {
         return "controlX";
      }
   };
   private DoubleProperty controlY = new DoublePropertyBase() {
      public void invalidated() {
         QuadCurveTo.this.u();
      }

      public Object getBean() {
         return QuadCurveTo.this;
      }

      public String getName() {
         return "controlY";
      }
   };
   private DoubleProperty x;
   private DoubleProperty y;

   public QuadCurveTo() {
   }

   public QuadCurveTo(double var1, double var3, double var5, double var7) {
      this.setControlX(var1);
      this.setControlY(var3);
      this.setX(var5);
      this.setY(var7);
   }

   public final void setControlX(double var1) {
      this.controlX.set(var1);
   }

   public final double getControlX() {
      return this.controlX.get();
   }

   public final DoubleProperty controlXProperty() {
      return this.controlX;
   }

   public final void setControlY(double var1) {
      this.controlY.set(var1);
   }

   public final double getControlY() {
      return this.controlY.get();
   }

   public final DoubleProperty controlYProperty() {
      return this.controlY;
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
               QuadCurveTo.this.u();
            }

            public Object getBean() {
               return QuadCurveTo.this;
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
               QuadCurveTo.this.u();
            }

            public Object getBean() {
               return QuadCurveTo.this;
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
         var1.addQuadTo((float)this.getControlX(), (float)this.getControlY(), (float)this.getX(), (float)this.getY());
      } else {
         double var2 = (double)var1.getCurrentX();
         double var4 = (double)var1.getCurrentY();
         var1.addQuadTo((float)(this.getControlX() + var2), (float)(this.getControlY() + var4), (float)(this.getX() + var2), (float)(this.getY() + var4));
      }

   }

   /** @deprecated */
   @Deprecated
   public void impl_addTo(Path2D var1) {
      if (this.isAbsolute()) {
         var1.quadTo((float)this.getControlX(), (float)this.getControlY(), (float)this.getX(), (float)this.getY());
      } else {
         double var2 = (double)var1.getCurrentX();
         double var4 = (double)var1.getCurrentY();
         var1.quadTo((float)(this.getControlX() + var2), (float)(this.getControlY() + var4), (float)(this.getX() + var2), (float)(this.getY() + var4));
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("CubicCurveTo[");
      var1.append("x=").append(this.getX());
      var1.append(", y=").append(this.getY());
      var1.append(", controlX=").append(this.getControlX());
      var1.append(", controlY=").append(this.getControlY());
      return var1.append("]").toString();
   }
}
