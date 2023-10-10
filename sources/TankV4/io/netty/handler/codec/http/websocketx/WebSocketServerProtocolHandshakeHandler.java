package io.netty.handler.codec.http.websocketx;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;

class WebSocketServerProtocolHandshakeHandler extends ChannelInboundHandlerAdapter {
   private final String websocketPath;
   private final String subprotocols;
   private final boolean allowExtensions;

   public WebSocketServerProtocolHandshakeHandler(String var1, String var2, boolean var3) {
      this.websocketPath = var1;
      this.subprotocols = var2;
      this.allowExtensions = var3;
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      FullHttpRequest var3 = (FullHttpRequest)var2;
      if (var3.getMethod() != HttpMethod.GET) {
         sendHttpResponse(var1, var3, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
      } else {
         WebSocketServerHandshakerFactory var4 = new WebSocketServerHandshakerFactory(getWebSocketLocation(var1.pipeline(), var3, this.websocketPath), this.subprotocols, this.allowExtensions);
         WebSocketServerHandshaker var5 = var4.newHandshaker(var3);
         if (var5 == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(var1.channel());
         } else {
            ChannelFuture var6 = var5.handshake(var1.channel(), var3);
            var6.addListener(new ChannelFutureListener(this, var1) {
               final ChannelHandlerContext val$ctx;
               final WebSocketServerProtocolHandshakeHandler this$0;

               {
                  this.this$0 = var1;
                  this.val$ctx = var2;
               }

               public void operationComplete(ChannelFuture var1) throws Exception {
                  if (!var1.isSuccess()) {
                     this.val$ctx.fireExceptionCaught(var1.cause());
                  } else {
                     this.val$ctx.fireUserEventTriggered(WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
                  }

               }

               public void operationComplete(Future var1) throws Exception {
                  this.operationComplete((ChannelFuture)var1);
               }
            });
            WebSocketServerProtocolHandler.setHandshaker(var1, var5);
            var1.pipeline().replace((ChannelHandler)this, "WS403Responder", WebSocketServerProtocolHandler.forbiddenHttpRequestResponder());
         }

      }
   }

   private static void sendHttpResponse(ChannelHandlerContext var0, HttpRequest var1, HttpResponse var2) {
      ChannelFuture var3 = var0.channel().writeAndFlush(var2);
      if (!HttpHeaders.isKeepAlive(var1) || var2.getStatus().code() != 200) {
         var3.addListener(ChannelFutureListener.CLOSE);
      }

   }

   private static String getWebSocketLocation(ChannelPipeline var0, HttpRequest var1, String var2) {
      String var3 = "ws";
      if (var0.get(SslHandler.class) != null) {
         var3 = "wss";
      }

      return var3 + "://" + var1.headers().get("Host") + var2;
   }
}
