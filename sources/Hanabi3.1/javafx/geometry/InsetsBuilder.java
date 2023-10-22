package javafx.geometry;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class InsetsBuilder implements Builder {
   private double bottom;
   private double left;
   private double right;
   private double top;

   protected InsetsBuilder() {
   }

   public static InsetsBuilder create() {
      return new InsetsBuilder();
   }

   public InsetsBuilder bottom(double var1) {
      this.bottom = var1;
      return this;
   }

   public InsetsBuilder left(double var1) {
      this.left = var1;
      return this;
   }

   public InsetsBuilder right(double var1) {
      this.right = var1;
      return this;
   }

   public InsetsBuilder top(double var1) {
      this.top = var1;
      return this;
   }

   public Insets build() {
      Insets var1 = new Insets(this.top, this.right, this.bottom, this.left);
      return var1;
   }
}
