package org.apache.http;

public class ProtocolException extends HttpException {
   private static final long serialVersionUID = -2143571074341228994L;

   public ProtocolException() {
   }

   public ProtocolException(String var1) {
      super(var1);
   }

   public ProtocolException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
