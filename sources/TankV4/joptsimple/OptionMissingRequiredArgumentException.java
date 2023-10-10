package joptsimple;

import java.util.Collection;

class OptionMissingRequiredArgumentException extends OptionException {
   private static final long serialVersionUID = -1L;

   OptionMissingRequiredArgumentException(Collection var1) {
      super(var1);
   }

   public String getMessage() {
      return "Option " + this.multipleOptionMessage() + " requires an argument";
   }
}
