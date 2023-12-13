package joptsimple;

import java.util.Collection;

class OptionArgumentConversionException extends OptionException {
   private static final long serialVersionUID = -1L;
   private final String argument;

   OptionArgumentConversionException(Collection var1, String var2, Throwable var3) {
      super(var1, var3);
      this.argument = var2;
   }

   public String getMessage() {
      return "Cannot parse argument '" + this.argument + "' of option " + this.multipleOptionMessage();
   }
}
