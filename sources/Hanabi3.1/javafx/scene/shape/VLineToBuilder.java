package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class VLineToBuilder extends PathElementBuilder implements Builder {
   private boolean __set;
   private double y;

   protected VLineToBuilder() {
   }

   public static VLineToBuilder create() {
      return new VLineToBuilder();
   }

   public void applyTo(VLineTo var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setY(this.y);
      }

   }

   public VLineToBuilder y(double var1) {
      this.y = var1;
      this.__set = true;
      return this;
   }

   public VLineTo build() {
      VLineTo var1 = new VLineTo();
      this.applyTo(var1);
      return var1;
   }
}
