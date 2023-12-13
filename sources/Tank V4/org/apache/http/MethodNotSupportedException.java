package org.apache.http;

public class MethodNotSupportedException extends HttpException {
   private static final long serialVersionUID = 3365359036840171201L;

   public MethodNotSupportedException(String var1) {
      super(var1);
   }

   public MethodNotSupportedException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
