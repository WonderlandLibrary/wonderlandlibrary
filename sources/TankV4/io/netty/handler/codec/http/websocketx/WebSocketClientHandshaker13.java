package io.netty.handler.codec.http.websocketx;

import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.URI;

public class WebSocketClientHandshaker13 extends WebSocketClientHandshaker {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketClientHandshaker13.class);
   public static final String MAGIC_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
   private String expectedChallengeResponseString;
   private final boolean allowExtensions;

   public WebSocketClientHandshaker13(URI var1, WebSocketVersion var2, String var3, boolean var4, HttpHeaders var5, int var6) {
      super(var1, var2, var3, var5, var6);
      this.allowExtensions = var4;
   }

   protected FullHttpRequest newHandshakeRequest() {
      URI var1 = this.uri();
      String var2 = var1.getPath();
      if (var1.getQuery() != null && !var1.getQuery().isEmpty()) {
         var2 = var1.getPath() + '?' + var1.getQuery();
      }

      if (var2 == null || var2.isEmpty()) {
         var2 = "/";
      }

      byte[] var3 = WebSocketUtil.randomBytes(16);
      String var4 = WebSocketUtil.base64(var3);
      String var5 = var4 + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
      byte[] var6 = WebSocketUtil.sha1(var5.getBytes(CharsetUtil.US_ASCII));
      this.expectedChallengeResponseString = WebSocketUtil.base64(var6);
      if (logger.isDebugEnabled()) {
         logger.debug(String.format("WS Version 13 Client Handshake key: %s. Expected response: %s.", var4, this.expectedChallengeResponseString));
      }

      int var7 = var1.getPort();
      if (var7 == -1) {
         if ("wss".equals(var1.getScheme())) {
            var7 = 443;
         } else {
            var7 = 80;
         }
      }

      DefaultFullHttpRequest var8 = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, var2);
      HttpHeaders var9 = var8.headers();
      var9.add((String)"Upgrade", (Object)"WebSocket".toLowerCase()).add((String)"Connection", (Object)"Upgrade").add((String)"Sec-WebSocket-Key", (Object)var4).add((String)"Host", (Object)(var1.getHost() + ':' + var7));
      String var10 = "http://" + var1.getHost();
      if (var7 != 80 && var7 != 443) {
         var10 = var10 + ':' + var7;
      }

      var9.add((String)"Sec-WebSocket-Origin", (Object)var10);
      String var11 = this.expectedSubprotocol();
      if (var11 != null && !var11.isEmpty()) {
         var9.add((String)"Sec-WebSocket-Protocol", (Object)var11);
      }

      var9.add((String)"Sec-WebSocket-Version", (Object)"13");
      if (this.customHeaders != null) {
         var9.add(this.customHeaders);
      }

      return var8;
   }

   protected void verify(FullHttpResponse var1) {
      HttpResponseStatus var2 = HttpResponseStatus.SWITCHING_PROTOCOLS;
      HttpHeaders var3 = var1.headers();
      if (!var1.getStatus().equals(var2)) {
         throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + var1.getStatus());
      } else {
         String var4 = var3.get("Upgrade");
         if (!"WebSocket".equalsIgnoreCase(var4)) {
            throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + var4);
         } else {
            String var5 = var3.get("Connection");
            if (!"Upgrade".equalsIgnoreCase(var5)) {
               throw new WebSocketHandshakeException("Invalid handshake response connection: " + var5);
            } else {
               String var6 = var3.get("Sec-WebSocket-Accept");
               if (var6 == null || !var6.equals(this.expectedChallengeResponseString)) {
                  throw new WebSocketHandshakeException(String.format("Invalid challenge. Actual: %s. Expected: %s", var6, this.expectedChallengeResponseString));
               }
            }
         }
      }
   }

   protected WebSocketFrameDecoder newWebsocketDecoder() {
      return new WebSocket13FrameDecoder(false, this.allowExtensions, this.maxFramePayloadLength());
   }

   protected WebSocketFrameEncoder newWebSocketEncoder() {
      return new WebSocket13FrameEncoder(true);
   }
}
