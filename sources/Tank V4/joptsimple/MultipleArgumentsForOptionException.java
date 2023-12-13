package joptsimple;

import java.util.Collection;

class MultipleArgumentsForOptionException extends OptionException {
   private static final long serialVersionUID = -1L;

   MultipleArgumentsForOptionException(Collection var1) {
      super(var1);
   }

   public String getMessage() {
      return "Found multiple arguments for option " + this.multipleOptionMessage() + ", but you asked for only one";
   }
}
