package org.apache.commons.lang3.concurrent;

public class ConcurrentException extends Exception {
   private static final long serialVersionUID = 6622707671812226130L;

   protected ConcurrentException() {
   }

   public ConcurrentException(Throwable var1) {
      super(ConcurrentUtils.checkedException(var1));
   }

   public ConcurrentException(String var1, Throwable var2) {
      super(var1, ConcurrentUtils.checkedException(var2));
   }
}
