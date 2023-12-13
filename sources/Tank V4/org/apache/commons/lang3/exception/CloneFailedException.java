package org.apache.commons.lang3.exception;

public class CloneFailedException extends RuntimeException {
   private static final long serialVersionUID = 20091223L;

   public CloneFailedException(String var1) {
      super(var1);
   }

   public CloneFailedException(Throwable var1) {
      super(var1);
   }

   public CloneFailedException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
