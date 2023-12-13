package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class PolylineBuilder extends ShapeBuilder implements Builder {
   private boolean __set;
   private Collection points;

   protected PolylineBuilder() {
   }

   public static PolylineBuilder create() {
      return new PolylineBuilder();
   }

   public void applyTo(Polyline var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.getPoints().addAll(this.points);
      }

   }

   public PolylineBuilder points(Collection var1) {
      this.points = var1;
      this.__set = true;
      return this;
   }

   public PolylineBuilder points(Double... var1) {
      return this.points((Collection)Arrays.asList(var1));
   }

   public Polyline build() {
      Polyline var1 = new Polyline();
      this.applyTo(var1);
      return var1;
   }
}
