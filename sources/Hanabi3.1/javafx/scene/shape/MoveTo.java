package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;

public class MoveTo extends PathElement {
   private DoubleProperty x;
   private DoubleProperty y;

   public MoveTo() {
   }

   public MoveTo(double var1, double var3) {
      this.setX(var1);
      this.setY(var3);
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
               MoveTo.this.u();
            }

            public Object getBean() {
               return MoveTo.this;
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
               MoveTo.this.u();
            }

            public Object getBean() {
               return MoveTo.this;
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
         var1.addMoveTo((float)this.getX(), (float)this.getY());
      } else {
         var1.addMoveTo((float)((double)var1.getCurrentX() + this.getX()), (float)((double)var1.getCurrentY() + this.getY()));
      }

   }

   /** @deprecated */
   @Deprecated
   public void impl_addTo(Path2D var1) {
      if (this.isAbsolute()) {
         var1.moveTo((float)this.getX(), (float)this.getY());
      } else {
         var1.moveTo((float)((double)var1.getCurrentX() + this.getX()), (float)((double)var1.getCurrentY() + this.getY()));
      }

   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("MoveTo[");
      var1.append("x=").append(this.getX());
      var1.append(", y=").append(this.getY());
      return var1.append("]").toString();
   }
}
