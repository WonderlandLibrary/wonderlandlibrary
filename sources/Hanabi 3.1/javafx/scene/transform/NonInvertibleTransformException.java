package javafx.scene.transform;

import javafx.beans.NamedArg;

public class NonInvertibleTransformException extends Exception {
   public NonInvertibleTransformException(@NamedArg("message") String var1) {
      super(var1);
   }
}
