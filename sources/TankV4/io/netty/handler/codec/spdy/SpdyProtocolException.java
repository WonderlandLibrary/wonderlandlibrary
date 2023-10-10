package io.netty.handler.codec.spdy;

public class SpdyProtocolException extends Exception {
   private static final long serialVersionUID = 7870000537743847264L;

   public SpdyProtocolException() {
   }

   public SpdyProtocolException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public SpdyProtocolException(String var1) {
      super(var1);
   }

   public SpdyProtocolException(Throwable var1) {
      super(var1);
   }
}
