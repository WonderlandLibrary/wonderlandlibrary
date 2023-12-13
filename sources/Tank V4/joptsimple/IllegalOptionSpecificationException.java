package joptsimple;

import java.util.Collections;

class IllegalOptionSpecificationException extends OptionException {
   private static final long serialVersionUID = -1L;

   IllegalOptionSpecificationException(String var1) {
      super(Collections.singletonList(var1));
   }

   public String getMessage() {
      return this.singleOptionMessage() + " is not a legal option character";
   }
}
