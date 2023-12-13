package io.netty.handler.codec.http.websocketx;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.util.concurrent.Future;
import java.net.URI;

public abstract class WebSocketClientHandshaker {
   private final URI uri;
   private final WebSocketVersion version;
   private volatile boolean handshakeComplete;
   private final String expectedSubprotocol;
   private volatile String actualSubprotocol;
   protected final HttpHeaders customHeaders;
   private final int maxFramePayloadLength;

   protected WebSocketClientHandshaker(URI var1, WebSocketVersion var2, String var3, HttpHeaders var4, int var5) {
      this.uri = var1;
      this.version = var2;
      this.expectedSubprotocol = var3;
      this.customHeaders = var4;
      this.maxFramePayloadLength = var5;
   }

   public URI uri() {
      return this.uri;
   }

   public WebSocketVersion version() {
      return this.version;
   }

   public int maxFramePayloadLength() {
      return this.maxFramePayloadLength;
   }

   public boolean isHandshakeComplete() {
      return this.handshakeComplete;
   }

   private void setHandshakeComplete() {
      this.handshakeComplete = true;
   }

   public String expectedSubprotocol() {
      return this.expectedSubprotocol;
   }

   public String actualSubprotocol() {
      return this.actualSubprotocol;
   }

   private void setActualSubprotocol(String var1) {
      this.actualSubprotocol = var1;
   }

   public ChannelFuture handshake(Channel var1) {
      if (var1 == null) {
         throw new NullPointerException("channel");
      } else {
         return this.handshake(var1, var1.newPromise());
      }
   }

   public final ChannelFuture handshake(Channel var1, ChannelPromise var2) {
      FullHttpRequest var3 = this.newHandshakeRequest();
      HttpResponseDecoder var4 = (HttpResponseDecoder)var1.pipeline().get(HttpResponseDecoder.class);
      if (var4 == null) {
         HttpClientCodec var5 = (HttpClientCodec)var1.pipeline().get(HttpClientCodec.class);
         if (var5 == null) {
            var2.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpResponseDecoder or HttpClientCodec"));
            return var2;
         }
      }

      var1.writeAndFlush(var3).addListener(new ChannelFutureListener(this, var2) {
         final ChannelPromise val$promise;
         final WebSocketClientHandshaker this$0;

         {
            this.this$0 = var1;
            this.val$promise = var2;
         }

         public void operationComplete(ChannelFuture var1) {
            if (var1.isSuccess()) {
               ChannelPipeline var2 = var1.channel().pipeline();
               ChannelHandlerContext var3 = var2.context(HttpRequestEncoder.class);
               if (var3 == null) {
                  var3 = var2.context(HttpClientCodec.class);
               }

               if (var3 == null) {
                  this.val$promise.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec"));
                  return;
               }

               var2.addAfter(var3.name(), "ws-encoder", this.this$0.newWebSocketEncoder());
               this.val$promise.setSuccess();
            } else {
               this.val$promise.setFailure(var1.cause());
            }

         }

         public void operationComplete(Future var1) throws Exception {
            this.operationComplete((ChannelFuture)var1);
         }
      });
      return var2;
   }

   protected abstract FullHttpRequest newHandshakeRequest();

   public final void finishHandshake(Channel var1, FullHttpResponse var2) {
      this.verify(var2);
      this.setActualSubprotocol(var2.headers().get("Sec-WebSocket-Protocol"));
      this.setHandshakeComplete();
      ChannelPipeline var3 = var1.pipeline();
      HttpContentDecompressor var4 = (HttpContentDecompressor)var3.get(HttpContentDecompressor.class);
      if (var4 != null) {
         var3.remove((ChannelHandler)var4);
      }

      ChannelHandlerContext var5 = var3.context(HttpResponseDecoder.class);
      if (var5 == null) {
         var5 = var3.context(HttpClientCodec.class);
         if (var5 == null) {
            throw new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec");
         }

         var3.replace((String)var5.name(), "ws-decoder", this.newWebsocketDecoder());
      } else {
         if (var3.get(HttpRequestEncoder.class) != null) {
            var3.remove(HttpRequestEncoder.class);
         }

         var3.replace((String)var5.name(), "ws-decoder", this.newWebsocketDecoder());
      }

   }

   protected abstract void verify(FullHttpResponse var1);

   protected abstract WebSocketFrameDecoder newWebsocketDecoder();

   protected abstract WebSocketFrameEncoder newWebSocketEncoder();

   public ChannelFuture close(Channel var1, CloseWebSocketFrame var2) {
      if (var1 == null) {
         throw new NullPointerException("channel");
      } else {
         return this.close(var1, var2, var1.newPromise());
      }
   }

   public ChannelFuture close(Channel var1, CloseWebSocketFrame var2, ChannelPromise var3) {
      if (var1 == null) {
         throw new NullPointerException("channel");
      } else {
         return var1.writeAndFlush(var2, var3);
      }
   }
}
