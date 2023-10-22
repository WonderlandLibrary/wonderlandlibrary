package javafx.scene.canvas;

import javafx.scene.NodeBuilder;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class CanvasBuilder extends NodeBuilder implements Builder {
   private int __set;
   private double height;
   private double width;

   protected CanvasBuilder() {
   }

   public static CanvasBuilder create() {
      return new CanvasBuilder();
   }

   public void applyTo(Canvas var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setHeight(this.height);
      }

      if ((var2 & 2) != 0) {
         var1.setWidth(this.width);
      }

   }

   public CanvasBuilder height(double var1) {
      this.height = var1;
      this.__set |= 1;
      return this;
   }

   public CanvasBuilder width(double var1) {
      this.width = var1;
      this.__set |= 2;
      return this;
   }

   public Canvas build() {
      Canvas var1 = new Canvas();
      this.applyTo(var1);
      return var1;
   }
}
