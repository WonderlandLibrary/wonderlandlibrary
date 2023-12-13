package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;

public class ClosePath extends PathElement {
   void addTo(NGPath var1) {
      var1.addClosePath();
   }

   /** @deprecated */
   @Deprecated
   public void impl_addTo(Path2D var1) {
      var1.closePath();
   }

   public String toString() {
      return "ClosePath";
   }
}
