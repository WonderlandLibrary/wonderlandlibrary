package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.util.regex.Pattern;

public class WebSocketServerHandshaker00 extends WebSocketServerHandshaker {
   private static final Pattern BEGINNING_DIGIT = Pattern.compile("[^0-9]");
   private static final Pattern BEGINNING_SPACE = Pattern.compile("[^ ]");

   public WebSocketServerHandshaker00(String var1, String var2, int var3) {
      super(WebSocketVersion.V00, var1, var2, var3);
   }

   protected FullHttpResponse newHandshakeResponse(FullHttpRequest var1, HttpHeaders var2) {
      if ("Upgrade".equalsIgnoreCase(var1.headers().get("Connection")) && "WebSocket".equalsIgnoreCase(var1.headers().get("Upgrade"))) {
         boolean var3 = var1.headers().contains("Sec-WebSocket-Key1") && var1.headers().contains("Sec-WebSocket-Key2");
         DefaultFullHttpResponse var4 = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, new HttpResponseStatus(101, var3 ? "WebSocket Protocol Handshake" : "Web Socket Protocol Handshake"));
         if (var2 != null) {
            var4.headers().add(var2);
         }

         var4.headers().add((String)"Upgrade", (Object)"WebSocket");
         var4.headers().add((String)"Connection", (Object)"Upgrade");
         String var5;
         if (var3) {
            var4.headers().add((String)"Sec-WebSocket-Origin", (Object)var1.headers().get("Origin"));
            var4.headers().add((String)"Sec-WebSocket-Location", (Object)this.uri());
            var5 = var1.headers().get("Sec-WebSocket-Protocol");
            String var6;
            if (var5 != null) {
               var6 = this.selectSubprotocol(var5);
               if (var6 == null) {
                  throw new WebSocketHandshakeException("Requested subprotocol(s) not supported: " + var5);
               }

               var4.headers().add((String)"Sec-WebSocket-Protocol", (Object)var6);
            }

            var6 = var1.headers().get("Sec-WebSocket-Key1");
            String var7 = var1.headers().get("Sec-WebSocket-Key2");
            int var8 = (int)(Long.parseLong(BEGINNING_DIGIT.matcher(var6).replaceAll("")) / (long)BEGINNING_SPACE.matcher(var6).replaceAll("").length());
            int var9 = (int)(Long.parseLong(BEGINNING_DIGIT.matcher(var7).replaceAll("")) / (long)BEGINNING_SPACE.matcher(var7).replaceAll("").length());
            long var10 = var1.content().readLong();
            ByteBuf var12 = Unpooled.buffer(16);
            var12.writeInt(var8);
            var12.writeInt(var9);
            var12.writeLong(var10);
            var4.content().writeBytes(WebSocketUtil.md5(var12.array()));
         } else {
            var4.headers().add((String)"WebSocket-Origin", (Object)var1.headers().get("Origin"));
            var4.headers().add((String)"WebSocket-Location", (Object)this.uri());
            var5 = var1.headers().get("WebSocket-Protocol");
            if (var5 != null) {
               var4.headers().add((String)"WebSocket-Protocol", (Object)this.selectSubprotocol(var5));
            }
         }

         return var4;
      } else {
         throw new WebSocketHandshakeException("not a WebSocket handshake request: missing upgrade");
      }
   }

   public ChannelFuture close(Channel var1, CloseWebSocketFrame var2, ChannelPromise var3) {
      return var1.writeAndFlush(var2, var3);
   }

   protected WebSocketFrameDecoder newWebsocketDecoder() {
      return new WebSocket00FrameDecoder(this.maxFramePayloadLength());
   }

   protected WebSocketFrameEncoder newWebSocketEncoder() {
      return new WebSocket00FrameEncoder();
   }
}
