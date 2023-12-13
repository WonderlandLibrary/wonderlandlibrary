package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.FileRegion;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;
import java.util.List;

public abstract class HttpObjectEncoder extends MessageToMessageEncoder {
   private static final byte[] CRLF = new byte[]{13, 10};
   private static final byte[] ZERO_CRLF = new byte[]{48, 13, 10};
   private static final byte[] ZERO_CRLF_CRLF = new byte[]{48, 13, 10, 13, 10};
   private static final ByteBuf CRLF_BUF;
   private static final ByteBuf ZERO_CRLF_CRLF_BUF;
   private static final int ST_INIT = 0;
   private static final int ST_CONTENT_NON_CHUNK = 1;
   private static final int ST_CONTENT_CHUNK = 2;
   private int state = 0;

   protected void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      ByteBuf var4 = null;
      if (var2 instanceof HttpMessage) {
         if (this.state != 0) {
            throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(var2));
         }

         HttpMessage var5 = (HttpMessage)var2;
         var4 = var1.alloc().buffer();
         this.encodeInitialLine(var4, var5);
         HttpHeaders.encode(var5.headers(), var4);
         var4.writeBytes(CRLF);
         this.state = HttpHeaders.isTransferEncodingChunked(var5) ? 2 : 1;
      }

      if (!(var2 instanceof HttpContent) && !(var2 instanceof ByteBuf) && !(var2 instanceof FileRegion)) {
         if (var4 != null) {
            var3.add(var4);
         }
      } else {
         if (this.state == 0) {
            throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(var2));
         }

         int var6 = contentLength(var2);
         if (this.state == 1) {
            if (var6 > 0) {
               if (var4 != null && var4.writableBytes() >= var6 && var2 instanceof HttpContent) {
                  var4.writeBytes(((HttpContent)var2).content());
                  var3.add(var4);
               } else {
                  if (var4 != null) {
                     var3.add(var4);
                  }

                  var3.add(encodeAndRetain(var2));
               }
            } else if (var4 != null) {
               var3.add(var4);
            } else {
               var3.add(Unpooled.EMPTY_BUFFER);
            }

            if (var2 instanceof LastHttpContent) {
               this.state = 0;
            }
         } else {
            if (this.state != 2) {
               throw new Error();
            }

            if (var4 != null) {
               var3.add(var4);
            }

            this.encodeChunkedContent(var1, var2, var6, var3);
         }
      }

   }

   private void encodeChunkedContent(ChannelHandlerContext var1, Object var2, int var3, List var4) {
      ByteBuf var6;
      if (var3 > 0) {
         byte[] var5 = Integer.toHexString(var3).getBytes(CharsetUtil.US_ASCII);
         var6 = var1.alloc().buffer(var5.length + 2);
         var6.writeBytes(var5);
         var6.writeBytes(CRLF);
         var4.add(var6);
         var4.add(encodeAndRetain(var2));
         var4.add(CRLF_BUF.duplicate());
      }

      if (var2 instanceof LastHttpContent) {
         HttpHeaders var7 = ((LastHttpContent)var2).trailingHeaders();
         if (var7.isEmpty()) {
            var4.add(ZERO_CRLF_CRLF_BUF.duplicate());
         } else {
            var6 = var1.alloc().buffer();
            var6.writeBytes(ZERO_CRLF);
            HttpHeaders.encode(var7, var6);
            var6.writeBytes(CRLF);
            var4.add(var6);
         }

         this.state = 0;
      } else if (var3 == 0) {
         var4.add(Unpooled.EMPTY_BUFFER);
      }

   }

   public boolean acceptOutboundMessage(Object var1) throws Exception {
      return var1 instanceof HttpObject || var1 instanceof ByteBuf || var1 instanceof FileRegion;
   }

   private static Object encodeAndRetain(Object var0) {
      if (var0 instanceof ByteBuf) {
         return ((ByteBuf)var0).retain();
      } else if (var0 instanceof HttpContent) {
         return ((HttpContent)var0).content().retain();
      } else if (var0 instanceof FileRegion) {
         return ((FileRegion)var0).retain();
      } else {
         throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(var0));
      }
   }

   private static int contentLength(Object var0) {
      if (var0 instanceof HttpContent) {
         return ((HttpContent)var0).content().readableBytes();
      } else if (var0 instanceof ByteBuf) {
         return ((ByteBuf)var0).readableBytes();
      } else if (var0 instanceof FileRegion) {
         return (int)((FileRegion)var0).count();
      } else {
         throw new IllegalStateException("unexpected message type: " + StringUtil.simpleClassName(var0));
      }
   }

   /** @deprecated */
   @Deprecated
   protected static void encodeAscii(String var0, ByteBuf var1) {
      HttpHeaders.encodeAscii0(var0, var1);
   }

   protected abstract void encodeInitialLine(ByteBuf var1, HttpMessage var2) throws Exception;

   static {
      CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(CRLF.length).writeBytes(CRLF));
      ZERO_CRLF_CRLF_BUF = Unpooled.unreleasableBuffer(Unpooled.directBuffer(ZERO_CRLF_CRLF.length).writeBytes(ZERO_CRLF_CRLF));
   }
}
