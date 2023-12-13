package javafx.css;

import com.sun.javafx.css.PseudoClassState;

public abstract class PseudoClass {
   public static PseudoClass getPseudoClass(String var0) {
      return PseudoClassState.getPseudoClass(var0);
   }

   public abstract String getPseudoClassName();
}
