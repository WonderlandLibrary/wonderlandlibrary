package javafx.scene;

import com.sun.javafx.sg.prism.NGAmbientLight;
import com.sun.javafx.sg.prism.NGNode;
import javafx.scene.paint.Color;

public class AmbientLight extends LightBase {
   public AmbientLight() {
   }

   public AmbientLight(Color var1) {
      super(var1);
   }

   /** @deprecated */
   @Deprecated
   protected NGNode impl_createPeer() {
      return new NGAmbientLight();
   }
}
