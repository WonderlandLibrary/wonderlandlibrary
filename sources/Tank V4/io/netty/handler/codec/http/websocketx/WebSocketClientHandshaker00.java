package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.net.URI;
import java.nio.ByteBuffer;

public class WebSocketClientHandshaker00 extends WebSocketClientHandshaker {
   private ByteBuf expectedChallengeResponseBytes;

   public WebSocketClientHandshaker00(URI var1, WebSocketVersion var2, String var3, HttpHeaders var4, int var5) {
      super(var1, var2, var3, var4, var5);
   }

   protected FullHttpRequest newHandshakeRequest() {
      int var1 = WebSocketUtil.randomNumber(1, 12);
      int var2 = WebSocketUtil.randomNumber(1, 12);
      int var3 = Integer.MAX_VALUE / var1;
      int var4 = Integer.MAX_VALUE / var2;
      int var5 = WebSocketUtil.randomNumber(0, var3);
      int var6 = WebSocketUtil.randomNumber(0, var4);
      int var7 = var5 * var1;
      int var8 = var6 * var2;
      String var9 = Integer.toString(var7);
      String var10 = Integer.toString(var8);
      var9 = insertRandomCharacters(var9);
      var10 = insertRandomCharacters(var10);
      var9 = insertSpaces(var9, var1);
      var10 = insertSpaces(var10, var2);
      byte[] var11 = WebSocketUtil.randomBytes(8);
      ByteBuffer var12 = ByteBuffer.allocate(4);
      var12.putInt(var5);
      byte[] var13 = var12.array();
      var12 = ByteBuffer.allocate(4);
      var12.putInt(var6);
      byte[] var14 = var12.array();
      byte[] var15 = new byte[16];
      System.arraycopy(var13, 0, var15, 0, 4);
      System.arraycopy(var14, 0, var15, 4, 4);
      System.arraycopy(var11, 0, var15, 8, 8);
      this.expectedChallengeResponseBytes = Unpooled.wrappedBuffer(WebSocketUtil.md5(var15));
      URI var16 = this.uri();
      String var17 = var16.getPath();
      if (var16.getQuery() != null && !var16.getQuery().isEmpty()) {
         var17 = var16.getPath() + '?' + var16.getQuery();
      }

      if (var17 == null || var17.isEmpty()) {
         var17 = "/";
      }

      DefaultFullHttpRequest var18 = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, var17);
      HttpHeaders var19 = var18.headers();
      var19.add((String)"Upgrade", (Object)"WebSocket").add((String)"Connection", (Object)"Upgrade").add((String)"Host", (Object)var16.getHost());
      int var20 = var16.getPort();
      String var21 = "http://" + var16.getHost();
      if (var20 != 80 && var20 != 443) {
         var21 = var21 + ':' + var20;
      }

      var19.add((String)"Origin", (Object)var21).add((String)"Sec-WebSocket-Key1", (Object)var9).add((String)"Sec-WebSocket-Key2", (Object)var10);
      String var22 = this.expectedSubprotocol();
      if (var22 != null && !var22.isEmpty()) {
         var19.add((String)"Sec-WebSocket-Protocol", (Object)var22);
      }

      if (this.customHeaders != null) {
         var19.add(this.customHeaders);
      }

      var19.set((String)"Content-Length", (Object)var11.length);
      var18.content().writeBytes(var11);
      return var18;
   }

   protected void verify(FullHttpResponse var1) {
      HttpResponseStatus var2 = new HttpResponseStatus(101, "WebSocket Protocol Handshake");
      if (!var1.getStatus().equals(var2)) {
         throw new WebSocketHandshakeException("Invalid handshake response getStatus: " + var1.getStatus());
      } else {
         HttpHeaders var3 = var1.headers();
         String var4 = var3.get("Upgrade");
         if (!"WebSocket".equalsIgnoreCase(var4)) {
            throw new WebSocketHandshakeException("Invalid handshake response upgrade: " + var4);
         } else {
            String var5 = var3.get("Connection");
            if (!"Upgrade".equalsIgnoreCase(var5)) {
               throw new WebSocketHandshakeException("Invalid handshake response connection: " + var5);
            } else {
               ByteBuf var6 = var1.content();
               if (!var6.equals(this.expectedChallengeResponseBytes)) {
                  throw new WebSocketHandshakeException("Invalid challenge");
               }
            }
         }
      }
   }

   private static String insertRandomCharacters(String var0) {
      int var1 = WebSocketUtil.randomNumber(1, 12);
      char[] var2 = new char[var1];
      int var3 = 0;

      while(true) {
         int var4;
         do {
            if (var3 >= var1) {
               for(var4 = 0; var4 < var1; ++var4) {
                  int var5 = WebSocketUtil.randomNumber(0, var0.length());
                  String var6 = var0.substring(0, var5);
                  String var7 = var0.substring(var5);
                  var0 = var6 + var2[var4] + var7;
               }

               return var0;
            }

            var4 = (int)(Math.random() * 126.0D + 33.0D);
         } while((33 >= var4 || var4 >= 47) && (58 >= var4 || var4 >= 126));

         var2[var3] = (char)var4;
         ++var3;
      }
   }

   private static String insertSpaces(String var0, int var1) {
      for(int var2 = 0; var2 < var1; ++var2) {
         int var3 = WebSocketUtil.randomNumber(1, var0.length() - 1);
         String var4 = var0.substring(0, var3);
         String var5 = var0.substring(var3);
         var0 = var4 + ' ' + var5;
      }

      return var0;
   }

   protected WebSocketFrameDecoder newWebsocketDecoder() {
      return new WebSocket00FrameDecoder(this.maxFramePayloadLength());
   }

   protected WebSocketFrameEncoder newWebSocketEncoder() {
      return new WebSocket00FrameEncoder();
   }
}
