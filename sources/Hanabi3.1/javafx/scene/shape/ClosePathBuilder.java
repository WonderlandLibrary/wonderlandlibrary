package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ClosePathBuilder extends PathElementBuilder implements Builder {
   protected ClosePathBuilder() {
   }

   public static ClosePathBuilder create() {
      return new ClosePathBuilder();
   }

   public ClosePath build() {
      ClosePath var1 = new ClosePath();
      this.applyTo(var1);
      return var1;
   }
}
