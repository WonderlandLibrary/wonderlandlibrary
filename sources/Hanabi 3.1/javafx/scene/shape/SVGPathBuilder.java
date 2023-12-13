package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class SVGPathBuilder extends ShapeBuilder implements Builder {
   private int __set;
   private String content;
   private FillRule fillRule;

   protected SVGPathBuilder() {
   }

   public static SVGPathBuilder create() {
      return new SVGPathBuilder();
   }

   public void applyTo(SVGPath var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setContent(this.content);
      }

      if ((var2 & 2) != 0) {
         var1.setFillRule(this.fillRule);
      }

   }

   public SVGPathBuilder content(String var1) {
      this.content = var1;
      this.__set |= 1;
      return this;
   }

   public SVGPathBuilder fillRule(FillRule var1) {
      this.fillRule = var1;
      this.__set |= 2;
      return this;
   }

   public SVGPath build() {
      SVGPath var1 = new SVGPath();
      this.applyTo(var1);
      return var1;
   }
}
