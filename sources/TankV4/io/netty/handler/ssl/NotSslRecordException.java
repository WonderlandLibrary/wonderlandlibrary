package io.netty.handler.ssl;

import javax.net.ssl.SSLException;

public class NotSslRecordException extends SSLException {
   private static final long serialVersionUID = -4316784434770656841L;

   public NotSslRecordException() {
      super("");
   }

   public NotSslRecordException(String var1) {
      super(var1);
   }

   public NotSslRecordException(Throwable var1) {
      super(var1);
   }

   public NotSslRecordException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
