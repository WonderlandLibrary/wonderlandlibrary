package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class LineToBuilder extends PathElementBuilder implements Builder {
   private int __set;
   private double x;
   private double y;

   protected LineToBuilder() {
   }

   public static LineToBuilder create() {
      return new LineToBuilder();
   }

   public void applyTo(LineTo var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setX(this.x);
      }

      if ((var2 & 2) != 0) {
         var1.setY(this.y);
      }

   }

   public LineToBuilder x(double var1) {
      this.x = var1;
      this.__set |= 1;
      return this;
   }

   public LineToBuilder y(double var1) {
      this.y = var1;
      this.__set |= 2;
      return this;
   }

   public LineTo build() {
      LineTo var1 = new LineTo();
      this.applyTo(var1);
      return var1;
   }
}
