package org.apache.commons.lang3.concurrent;

public class ConcurrentRuntimeException extends RuntimeException {
   private static final long serialVersionUID = -6582182735562919670L;

   protected ConcurrentRuntimeException() {
   }

   public ConcurrentRuntimeException(Throwable var1) {
      super(ConcurrentUtils.checkedException(var1));
   }

   public ConcurrentRuntimeException(String var1, Throwable var2) {
      super(var1, ConcurrentUtils.checkedException(var2));
   }
}
