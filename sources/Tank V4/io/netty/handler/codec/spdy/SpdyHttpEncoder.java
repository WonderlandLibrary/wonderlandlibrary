package io.netty.handler.codec.spdy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class SpdyHttpEncoder extends MessageToMessageEncoder {
   private final int spdyVersion;
   private int currentStreamId;

   public SpdyHttpEncoder(SpdyVersion var1) {
      if (var1 == null) {
         throw new NullPointerException("version");
      } else {
         this.spdyVersion = var1.getVersion();
      }
   }

   protected void encode(ChannelHandlerContext var1, HttpObject var2, List var3) throws Exception {
      boolean var4 = false;
      boolean var5 = false;
      SpdySynStreamFrame var7;
      if (var2 instanceof HttpRequest) {
         HttpRequest var6 = (HttpRequest)var2;
         var7 = this.createSynStreamFrame(var6);
         var3.add(var7);
         var5 = var7.isLast();
         var4 = true;
      }

      if (var2 instanceof HttpResponse) {
         HttpResponse var13 = (HttpResponse)var2;
         if (var13.headers().contains("X-SPDY-Associated-To-Stream-ID")) {
            var7 = this.createSynStreamFrame(var13);
            var5 = var7.isLast();
            var3.add(var7);
         } else {
            SpdySynReplyFrame var15 = this.createSynReplyFrame(var13);
            var5 = var15.isLast();
            var3.add(var15);
         }

         var4 = true;
      }

      if (var2 instanceof HttpContent && !var5) {
         HttpContent var14 = (HttpContent)var2;
         var14.content().retain();
         DefaultSpdyDataFrame var16 = new DefaultSpdyDataFrame(this.currentStreamId, var14.content());
         var16.setLast(var14 instanceof LastHttpContent);
         if (!(var14 instanceof LastHttpContent)) {
            var3.add(var16);
         } else {
            LastHttpContent var8 = (LastHttpContent)var14;
            HttpHeaders var9 = var8.trailingHeaders();
            if (var9.isEmpty()) {
               var3.add(var16);
            } else {
               DefaultSpdyHeadersFrame var10 = new DefaultSpdyHeadersFrame(this.currentStreamId);
               Iterator var11 = var9.iterator();

               while(var11.hasNext()) {
                  Entry var12 = (Entry)var11.next();
                  var10.headers().add((String)var12.getKey(), var12.getValue());
               }

               var3.add(var10);
               var3.add(var16);
            }
         }

         var4 = true;
      }

      if (!var4) {
         throw new UnsupportedMessageTypeException(var2, new Class[0]);
      }
   }

   private SpdySynStreamFrame createSynStreamFrame(HttpMessage var1) throws Exception {
      int var2 = SpdyHttpHeaders.getStreamId(var1);
      int var3 = SpdyHttpHeaders.getAssociatedToStreamId(var1);
      byte var4 = SpdyHttpHeaders.getPriority(var1);
      String var5 = SpdyHttpHeaders.getUrl(var1);
      String var6 = SpdyHttpHeaders.getScheme(var1);
      SpdyHttpHeaders.removeStreamId(var1);
      SpdyHttpHeaders.removeAssociatedToStreamId(var1);
      SpdyHttpHeaders.removePriority(var1);
      SpdyHttpHeaders.removeUrl(var1);
      SpdyHttpHeaders.removeScheme(var1);
      var1.headers().remove("Connection");
      var1.headers().remove("Keep-Alive");
      var1.headers().remove("Proxy-Connection");
      var1.headers().remove("Transfer-Encoding");
      DefaultSpdySynStreamFrame var7 = new DefaultSpdySynStreamFrame(var2, var3, var4);
      if (var1 instanceof FullHttpRequest) {
         HttpRequest var8 = (HttpRequest)var1;
         SpdyHeaders.setMethod(this.spdyVersion, var7, var8.getMethod());
         SpdyHeaders.setUrl(this.spdyVersion, var7, var8.getUri());
         SpdyHeaders.setVersion(this.spdyVersion, var7, var1.getProtocolVersion());
      }

      if (var1 instanceof HttpResponse) {
         HttpResponse var10 = (HttpResponse)var1;
         SpdyHeaders.setStatus(this.spdyVersion, var7, var10.getStatus());
         SpdyHeaders.setUrl(this.spdyVersion, var7, var5);
         SpdyHeaders.setVersion(this.spdyVersion, var7, var1.getProtocolVersion());
         var7.setUnidirectional(true);
      }

      if (this.spdyVersion >= 3) {
         String var11 = HttpHeaders.getHost(var1);
         var1.headers().remove("Host");
         SpdyHeaders.setHost(var7, var11);
      }

      if (var6 == null) {
         var6 = "https";
      }

      SpdyHeaders.setScheme(this.spdyVersion, var7, var6);
      Iterator var12 = var1.headers().iterator();

      while(var12.hasNext()) {
         Entry var9 = (Entry)var12.next();
         var7.headers().add((String)var9.getKey(), var9.getValue());
      }

      this.currentStreamId = var7.getStreamId();
      var7.setLast(isLast(var1));
      return var7;
   }

   private SpdySynReplyFrame createSynReplyFrame(HttpResponse var1) throws Exception {
      int var2 = SpdyHttpHeaders.getStreamId(var1);
      SpdyHttpHeaders.removeStreamId(var1);
      var1.headers().remove("Connection");
      var1.headers().remove("Keep-Alive");
      var1.headers().remove("Proxy-Connection");
      var1.headers().remove("Transfer-Encoding");
      DefaultSpdySynReplyFrame var3 = new DefaultSpdySynReplyFrame(var2);
      SpdyHeaders.setStatus(this.spdyVersion, var3, var1.getStatus());
      SpdyHeaders.setVersion(this.spdyVersion, var3, var1.getProtocolVersion());
      Iterator var4 = var1.headers().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         var3.headers().add((String)var5.getKey(), var5.getValue());
      }

      this.currentStreamId = var2;
      var3.setLast(isLast(var1));
      return var3;
   }

   private static boolean isLast(HttpMessage var0) {
      if (var0 instanceof FullHttpMessage) {
         FullHttpMessage var1 = (FullHttpMessage)var0;
         if (var1.trailingHeaders().isEmpty() && !var1.content().isReadable()) {
            return true;
         }
      }

      return false;
   }

   protected void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.encode(var1, (HttpObject)var2, var3);
   }
}
