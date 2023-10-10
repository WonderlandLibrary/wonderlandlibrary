package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import java.util.List;
import javax.net.ssl.SSLEngine;

public abstract class SpdyOrHttpChooser extends ByteToMessageDecoder {
   private final int maxSpdyContentLength;
   private final int maxHttpContentLength;

   protected SpdyOrHttpChooser(int var1, int var2) {
      this.maxSpdyContentLength = var1;
      this.maxHttpContentLength = var2;
   }

   protected abstract SpdyOrHttpChooser.SelectedProtocol getProtocol(SSLEngine var1);

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      if (var1 == null) {
         var1.pipeline().remove((ChannelHandler)this);
      }

   }

   protected void addSpdyHandlers(ChannelHandlerContext var1, SpdyVersion var2) {
      ChannelPipeline var3 = var1.pipeline();
      var3.addLast((String)"spdyDecoder", (ChannelHandler)(new SpdyFrameDecoder(var2)));
      var3.addLast((String)"spdyEncoder", (ChannelHandler)(new SpdyFrameEncoder(var2)));
      var3.addLast((String)"spdySessionHandler", (ChannelHandler)(new SpdySessionHandler(var2, true)));
      var3.addLast((String)"spdyHttpEncoder", (ChannelHandler)(new SpdyHttpEncoder(var2)));
      var3.addLast((String)"spdyHttpDecoder", (ChannelHandler)(new SpdyHttpDecoder(var2, this.maxSpdyContentLength)));
      var3.addLast((String)"spdyStreamIdHandler", (ChannelHandler)(new SpdyHttpResponseStreamIdHandler()));
      var3.addLast((String)"httpRquestHandler", (ChannelHandler)this.createHttpRequestHandlerForSpdy());
   }

   protected void addHttpHandlers(ChannelHandlerContext var1) {
      ChannelPipeline var2 = var1.pipeline();
      var2.addLast((String)"httpRquestDecoder", (ChannelHandler)(new HttpRequestDecoder()));
      var2.addLast((String)"httpResponseEncoder", (ChannelHandler)(new HttpResponseEncoder()));
      var2.addLast((String)"httpChunkAggregator", (ChannelHandler)(new HttpObjectAggregator(this.maxHttpContentLength)));
      var2.addLast((String)"httpRquestHandler", (ChannelHandler)this.createHttpRequestHandlerForHttp());
   }

   protected abstract ChannelInboundHandler createHttpRequestHandlerForHttp();

   protected ChannelInboundHandler createHttpRequestHandlerForSpdy() {
      return this.createHttpRequestHandlerForHttp();
   }

   public static enum SelectedProtocol {
      SPDY_3("spdy/3"),
      SPDY_3_1("spdy/3.1"),
      HTTP_1_1("http/1.1"),
      HTTP_1_0("http/1.0"),
      UNKNOWN("Unknown");

      private final String name;
      private static final SpdyOrHttpChooser.SelectedProtocol[] $VALUES = new SpdyOrHttpChooser.SelectedProtocol[]{SPDY_3, SPDY_3_1, HTTP_1_1, HTTP_1_0, UNKNOWN};

      private SelectedProtocol(String var3) {
         this.name = var3;
      }

      public String protocolName() {
         return this.name;
      }

      public static SpdyOrHttpChooser.SelectedProtocol protocol(String var0) {
         SpdyOrHttpChooser.SelectedProtocol[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            SpdyOrHttpChooser.SelectedProtocol var4 = var1[var3];
            if (var4.protocolName().equals(var0)) {
               return var4;
            }
         }

         return UNKNOWN;
      }
   }
}
