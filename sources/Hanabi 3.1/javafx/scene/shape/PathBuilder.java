package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class PathBuilder extends ShapeBuilder implements Builder {
   private int __set;
   private Collection elements;
   private FillRule fillRule;

   protected PathBuilder() {
   }

   public static PathBuilder create() {
      return new PathBuilder();
   }

   public void applyTo(Path var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.getElements().addAll(this.elements);
      }

      if ((var2 & 2) != 0) {
         var1.setFillRule(this.fillRule);
      }

   }

   public PathBuilder elements(Collection var1) {
      this.elements = var1;
      this.__set |= 1;
      return this;
   }

   public PathBuilder elements(PathElement... var1) {
      return this.elements((Collection)Arrays.asList(var1));
   }

   public PathBuilder fillRule(FillRule var1) {
      this.fillRule = var1;
      this.__set |= 2;
      return this;
   }

   public Path build() {
      Path var1 = new Path();
      this.applyTo(var1);
      return var1;
   }
}
