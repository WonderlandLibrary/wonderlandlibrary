package org.apache.http;

public class HttpException extends Exception {
   private static final long serialVersionUID = -5437299376222011036L;

   public HttpException() {
   }

   public HttpException(String var1) {
      super(var1);
   }

   public HttpException(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }
}
