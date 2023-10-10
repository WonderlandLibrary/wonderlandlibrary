package io.netty.handler.codec.http.websocketx;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class WebSocketServerHandshaker13 extends WebSocketServerHandshaker {
   public static final String WEBSOCKET_13_ACCEPT_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
   private final boolean allowExtensions;

   public WebSocketServerHandshaker13(String var1, String var2, boolean var3, int var4) {
      super(WebSocketVersion.V13, var1, var2, var4);
      this.allowExtensions = var3;
   }

   protected FullHttpResponse newHandshakeResponse(FullHttpRequest var1, HttpHeaders var2) {
      DefaultFullHttpResponse var3 = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS);
      if (var2 != null) {
         var3.headers().add(var2);
      }

      String var4 = var1.headers().get("Sec-WebSocket-Key");
      if (var4 == null) {
         throw new WebSocketHandshakeException("not a WebSocket request: missing key");
      } else {
         String var5 = var4 + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
         byte[] var6 = WebSocketUtil.sha1(var5.getBytes(CharsetUtil.US_ASCII));
         String var7 = WebSocketUtil.base64(var6);
         if (logger.isDebugEnabled()) {
            logger.debug(String.format("WS Version 13 Server Handshake key: %s. Response: %s.", var4, var7));
         }

         var3.headers().add((String)"Upgrade", (Object)"WebSocket".toLowerCase());
         var3.headers().add((String)"Connection", (Object)"Upgrade");
         var3.headers().add((String)"Sec-WebSocket-Accept", (Object)var7);
         String var8 = var1.headers().get("Sec-WebSocket-Protocol");
         if (var8 != null) {
            String var9 = this.selectSubprotocol(var8);
            if (var9 == null) {
               throw new WebSocketHandshakeException("Requested subprotocol(s) not supported: " + var8);
            }

            var3.headers().add((String)"Sec-WebSocket-Protocol", (Object)var9);
         }

         return var3;
      }
   }

   protected WebSocketFrameDecoder newWebsocketDecoder() {
      return new WebSocket13FrameDecoder(true, this.allowExtensions, this.maxFramePayloadLength());
   }

   protected WebSocketFrameEncoder newWebSocketEncoder() {
      return new WebSocket13FrameEncoder(false);
   }
}
