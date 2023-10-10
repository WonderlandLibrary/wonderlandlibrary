package joptsimple;

import java.util.Collections;

class UnacceptableNumberOfNonOptionsException extends OptionException {
   private static final long serialVersionUID = -1L;
   private final int minimum;
   private final int maximum;
   private final int actual;

   UnacceptableNumberOfNonOptionsException(int var1, int var2, int var3) {
      super(Collections.singletonList("[arguments]"));
      this.minimum = var1;
      this.maximum = var2;
      this.actual = var3;
   }

   public String getMessage() {
      return String.format("actual = %d, minimum = %d, maximum = %d", this.actual, this.minimum, this.maximum);
   }
}
