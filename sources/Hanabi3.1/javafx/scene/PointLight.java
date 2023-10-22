package javafx.scene;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGPointLight;
import javafx.scene.paint.Color;

public class PointLight extends LightBase {
   public PointLight() {
   }

   public PointLight(Color var1) {
      super(var1);
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGPointLight();
   }
}
