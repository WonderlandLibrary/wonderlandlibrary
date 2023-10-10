package io.netty.handler.codec.http.cors;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

public class CorsHandler extends ChannelDuplexHandler {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(CorsHandler.class);
   private final CorsConfig config;
   private HttpRequest request;

   public CorsHandler(CorsConfig var1) {
      this.config = var1;
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      if (this.config.isCorsSupportEnabled() && var2 instanceof HttpRequest) {
         this.request = (HttpRequest)var2;
         if (this.request != false) {
            this.handlePreflight(var1, this.request);
            return;
         }
      }

      var1.fireChannelRead(var2);
   }

   private void handlePreflight(ChannelHandlerContext var1, HttpRequest var2) {
      DefaultHttpResponse var3 = new DefaultHttpResponse(var2.getProtocolVersion(), HttpResponseStatus.OK);
      if (var3 != null) {
         this.setAllowMethods(var3);
         this.setAllowHeaders(var3);
         this.setAllowCredentials(var3);
         this.setMaxAge(var3);
      }

      var1.writeAndFlush(var3).addListener(ChannelFutureListener.CLOSE);
   }

   private void setAllowCredentials(HttpResponse var1) {
      if (this.config.isCredentialsAllowed()) {
         var1.headers().set((String)"Access-Control-Allow-Credentials", (Object)"true");
      }

   }

   private void setExposeHeaders(HttpResponse var1) {
      if (!this.config.exposedHeaders().isEmpty()) {
         var1.headers().set((String)"Access-Control-Expose-Headers", (Iterable)this.config.exposedHeaders());
      }

   }

   private void setAllowMethods(HttpResponse var1) {
      var1.headers().set((String)"Access-Control-Allow-Methods", (Iterable)this.config.allowedRequestMethods());
   }

   private void setAllowHeaders(HttpResponse var1) {
      var1.headers().set((String)"Access-Control-Allow-Headers", (Iterable)this.config.allowedRequestHeaders());
   }

   private void setMaxAge(HttpResponse var1) {
      var1.headers().set((String)"Access-Control-Max-Age", (Object)this.config.maxAge());
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      if (this.config.isCorsSupportEnabled() && var2 instanceof HttpResponse) {
         HttpResponse var4 = (HttpResponse)var2;
         if (var4 != null) {
            this.setAllowCredentials(var4);
            this.setAllowHeaders(var4);
            this.setExposeHeaders(var4);
         }
      }

      var1.writeAndFlush(var2, var3);
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      logger.error("Caught error in CorsHandler", var2);
      var1.fireExceptionCaught(var2);
   }
}
