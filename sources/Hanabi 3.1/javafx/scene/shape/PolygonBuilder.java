package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class PolygonBuilder extends ShapeBuilder implements Builder {
   private boolean __set;
   private Collection points;

   protected PolygonBuilder() {
   }

   public static PolygonBuilder create() {
      return new PolygonBuilder();
   }

   public void applyTo(Polygon var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.getPoints().addAll(this.points);
      }

   }

   public PolygonBuilder points(Collection var1) {
      this.points = var1;
      this.__set = true;
      return this;
   }

   public PolygonBuilder points(Double... var1) {
      return this.points((Collection)Arrays.asList(var1));
   }

   public Polygon build() {
      Polygon var1 = new Polygon();
      this.applyTo(var1);
      return var1;
   }
}
