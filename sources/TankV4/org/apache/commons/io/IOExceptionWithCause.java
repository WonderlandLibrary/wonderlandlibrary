package org.apache.commons.io;

import java.io.IOException;

public class IOExceptionWithCause extends IOException {
   private static final long serialVersionUID = 1L;

   public IOExceptionWithCause(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }

   public IOExceptionWithCause(Throwable var1) {
      super(var1 == null ? null : var1.toString());
      this.initCause(var1);
   }
}
