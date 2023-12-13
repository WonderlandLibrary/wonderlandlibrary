package io.netty.handler.codec.http.websocketx;

public class WebSocketHandshakeException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public WebSocketHandshakeException(String var1) {
      super(var1);
   }

   public WebSocketHandshakeException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
