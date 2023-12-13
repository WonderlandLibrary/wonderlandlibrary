package joptsimple;

import java.util.Collection;

class MissingRequiredOptionException extends OptionException {
   private static final long serialVersionUID = -1L;

   protected MissingRequiredOptionException(Collection var1) {
      super(var1);
   }

   public String getMessage() {
      return "Missing required option(s) " + this.multipleOptionMessage();
   }
}
