package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SpdyHttpDecoder extends MessageToMessageDecoder {
   private final int spdyVersion;
   private final int maxContentLength;
   private final Map messageMap;

   public SpdyHttpDecoder(SpdyVersion var1, int var2) {
      this(var1, var2, new HashMap());
   }

   protected SpdyHttpDecoder(SpdyVersion var1, int var2, Map var3) {
      if (var1 == null) {
         throw new NullPointerException("version");
      } else if (var2 <= 0) {
         throw new IllegalArgumentException("maxContentLength must be a positive integer: " + var2);
      } else {
         this.spdyVersion = var1.getVersion();
         this.maxContentLength = var2;
         this.messageMap = var3;
      }
   }

   protected FullHttpMessage putMessage(int var1, FullHttpMessage var2) {
      return (FullHttpMessage)this.messageMap.put(var1, var2);
   }

   protected FullHttpMessage getMessage(int var1) {
      return (FullHttpMessage)this.messageMap.get(var1);
   }

   protected FullHttpMessage removeMessage(int var1) {
      return (FullHttpMessage)this.messageMap.remove(var1);
   }

   protected void decode(ChannelHandlerContext var1, SpdyFrame var2, List var3) throws Exception {
      int var5;
      DefaultSpdyRstStreamFrame var21;
      if (var2 instanceof SpdySynStreamFrame) {
         SpdySynStreamFrame var4 = (SpdySynStreamFrame)var2;
         var5 = var4.getStreamId();
         if (SpdyCodecUtil.isServerId(var5)) {
            int var6 = var4.getAssociatedToStreamId();
            if (var6 == 0) {
               var21 = new DefaultSpdyRstStreamFrame(var5, SpdyStreamStatus.INVALID_STREAM);
               var1.writeAndFlush(var21);
               return;
            }

            String var7 = SpdyHeaders.getUrl(this.spdyVersion, var4);
            DefaultSpdyRstStreamFrame var25;
            if (var7 == null) {
               var25 = new DefaultSpdyRstStreamFrame(var5, SpdyStreamStatus.PROTOCOL_ERROR);
               var1.writeAndFlush(var25);
               return;
            }

            if (var4.isTruncated()) {
               var25 = new DefaultSpdyRstStreamFrame(var5, SpdyStreamStatus.INTERNAL_ERROR);
               var1.writeAndFlush(var25);
               return;
            }

            try {
               FullHttpResponse var8 = createHttpResponse(this.spdyVersion, var4);
               SpdyHttpHeaders.setStreamId(var8, var5);
               SpdyHttpHeaders.setAssociatedToStreamId(var8, var6);
               SpdyHttpHeaders.setPriority(var8, var4.getPriority());
               SpdyHttpHeaders.setUrl(var8, var7);
               if (var4.isLast()) {
                  HttpHeaders.setContentLength(var8, 0L);
                  var3.add(var8);
               } else {
                  this.putMessage(var5, var8);
               }
            } catch (Exception var12) {
               DefaultSpdyRstStreamFrame var9 = new DefaultSpdyRstStreamFrame(var5, SpdyStreamStatus.PROTOCOL_ERROR);
               var1.writeAndFlush(var9);
            }
         } else {
            if (var4.isTruncated()) {
               DefaultSpdySynReplyFrame var18 = new DefaultSpdySynReplyFrame(var5);
               var18.setLast(true);
               SpdyHeaders.setStatus(this.spdyVersion, var18, HttpResponseStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
               SpdyHeaders.setVersion(this.spdyVersion, var18, HttpVersion.HTTP_1_0);
               var1.writeAndFlush(var18);
               return;
            }

            try {
               FullHttpRequest var17 = createHttpRequest(this.spdyVersion, var4);
               SpdyHttpHeaders.setStreamId(var17, var5);
               if (var4.isLast()) {
                  var3.add(var17);
               } else {
                  this.putMessage(var5, var17);
               }
            } catch (Exception var11) {
               DefaultSpdySynReplyFrame var23 = new DefaultSpdySynReplyFrame(var5);
               var23.setLast(true);
               SpdyHeaders.setStatus(this.spdyVersion, var23, HttpResponseStatus.BAD_REQUEST);
               SpdyHeaders.setVersion(this.spdyVersion, var23, HttpVersion.HTTP_1_0);
               var1.writeAndFlush(var23);
            }
         }
      } else if (var2 instanceof SpdySynReplyFrame) {
         SpdySynReplyFrame var13 = (SpdySynReplyFrame)var2;
         var5 = var13.getStreamId();
         if (var13.isTruncated()) {
            DefaultSpdyRstStreamFrame var20 = new DefaultSpdyRstStreamFrame(var5, SpdyStreamStatus.INTERNAL_ERROR);
            var1.writeAndFlush(var20);
            return;
         }

         try {
            FullHttpResponse var19 = createHttpResponse(this.spdyVersion, var13);
            SpdyHttpHeaders.setStreamId(var19, var5);
            if (var13.isLast()) {
               HttpHeaders.setContentLength(var19, 0L);
               var3.add(var19);
            } else {
               this.putMessage(var5, var19);
            }
         } catch (Exception var10) {
            var21 = new DefaultSpdyRstStreamFrame(var5, SpdyStreamStatus.PROTOCOL_ERROR);
            var1.writeAndFlush(var21);
         }
      } else {
         FullHttpMessage var22;
         if (var2 instanceof SpdyHeadersFrame) {
            SpdyHeadersFrame var14 = (SpdyHeadersFrame)var2;
            var5 = var14.getStreamId();
            var22 = this.getMessage(var5);
            if (var22 == null) {
               return;
            }

            if (!var14.isTruncated()) {
               Iterator var24 = var14.headers().iterator();

               while(var24.hasNext()) {
                  Entry var27 = (Entry)var24.next();
                  var22.headers().add((String)var27.getKey(), var27.getValue());
               }
            }

            if (var14.isLast()) {
               HttpHeaders.setContentLength(var22, (long)var22.content().readableBytes());
               this.removeMessage(var5);
               var3.add(var22);
            }
         } else if (var2 instanceof SpdyDataFrame) {
            SpdyDataFrame var15 = (SpdyDataFrame)var2;
            var5 = var15.getStreamId();
            var22 = this.getMessage(var5);
            if (var22 == null) {
               return;
            }

            ByteBuf var26 = var22.content();
            if (var26.readableBytes() > this.maxContentLength - var15.content().readableBytes()) {
               this.removeMessage(var5);
               throw new TooLongFrameException("HTTP content length exceeded " + this.maxContentLength + " bytes.");
            }

            ByteBuf var28 = var15.content();
            int var29 = var28.readableBytes();
            var26.writeBytes(var28, var28.readerIndex(), var29);
            if (var15.isLast()) {
               HttpHeaders.setContentLength(var22, (long)var26.readableBytes());
               this.removeMessage(var5);
               var3.add(var22);
            }
         } else if (var2 instanceof SpdyRstStreamFrame) {
            SpdyRstStreamFrame var16 = (SpdyRstStreamFrame)var2;
            var5 = var16.getStreamId();
            this.removeMessage(var5);
         }
      }

   }

   private static FullHttpRequest createHttpRequest(int var0, SpdyHeadersFrame var1) throws Exception {
      HttpMethod var2 = SpdyHeaders.getMethod(var0, var1);
      String var3 = SpdyHeaders.getUrl(var0, var1);
      HttpVersion var4 = SpdyHeaders.getVersion(var0, var1);
      SpdyHeaders.removeMethod(var0, var1);
      SpdyHeaders.removeUrl(var0, var1);
      SpdyHeaders.removeVersion(var0, var1);
      DefaultFullHttpRequest var5 = new DefaultFullHttpRequest(var4, var2, var3);
      SpdyHeaders.removeScheme(var0, var1);
      if (var0 >= 3) {
         String var6 = SpdyHeaders.getHost(var1);
         SpdyHeaders.removeHost(var1);
         HttpHeaders.setHost(var5, (String)var6);
      }

      Iterator var8 = var1.headers().iterator();

      while(var8.hasNext()) {
         Entry var7 = (Entry)var8.next();
         var5.headers().add((String)var7.getKey(), var7.getValue());
      }

      HttpHeaders.setKeepAlive(var5, true);
      var5.headers().remove("Transfer-Encoding");
      return var5;
   }

   private static FullHttpResponse createHttpResponse(int var0, SpdyHeadersFrame var1) throws Exception {
      HttpResponseStatus var2 = SpdyHeaders.getStatus(var0, var1);
      HttpVersion var3 = SpdyHeaders.getVersion(var0, var1);
      SpdyHeaders.removeStatus(var0, var1);
      SpdyHeaders.removeVersion(var0, var1);
      DefaultFullHttpResponse var4 = new DefaultFullHttpResponse(var3, var2);
      Iterator var5 = var1.headers().iterator();

      while(var5.hasNext()) {
         Entry var6 = (Entry)var5.next();
         var4.headers().add((String)var6.getKey(), var6.getValue());
      }

      HttpHeaders.setKeepAlive(var4, true);
      var4.headers().remove("Transfer-Encoding");
      var4.headers().remove("Trailer");
      return var4;
   }

   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (SpdyFrame)var2, var3);
   }
}
