package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class HLineToBuilder extends PathElementBuilder implements Builder {
   private boolean __set;
   private double x;

   protected HLineToBuilder() {
   }

   public static HLineToBuilder create() {
      return new HLineToBuilder();
   }

   public void applyTo(HLineTo var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setX(this.x);
      }

   }

   public HLineToBuilder x(double var1) {
      this.x = var1;
      this.__set = true;
      return this;
   }

   public HLineTo build() {
      HLineTo var1 = new HLineTo();
      this.applyTo(var1);
      return var1;
   }
}
