package joptsimple;

import java.util.Collection;
import java.util.Collections;

class UnconfiguredOptionException extends OptionException {
   private static final long serialVersionUID = -1L;

   UnconfiguredOptionException(String var1) {
      this((Collection)Collections.singletonList(var1));
   }

   UnconfiguredOptionException(Collection var1) {
      super(var1);
   }

   public String getMessage() {
      return "Option " + this.multipleOptionMessage() + " has not been configured on this parser";
   }
}
