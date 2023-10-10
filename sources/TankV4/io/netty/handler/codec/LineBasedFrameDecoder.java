package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

public class LineBasedFrameDecoder extends ByteToMessageDecoder {
   private final int maxLength;
   private final boolean failFast;
   private final boolean stripDelimiter;
   private boolean discarding;
   private int discardedBytes;

   public LineBasedFrameDecoder(int var1) {
      this(var1, true, false);
   }

   public LineBasedFrameDecoder(int var1, boolean var2, boolean var3) {
      this.maxLength = var1;
      this.failFast = var3;
      this.stripDelimiter = var2;
   }

   protected final void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      Object var4 = this.decode(var1, var2);
      if (var4 != null) {
         var3.add(var4);
      }

   }

   protected Object decode(ChannelHandlerContext var1, ByteBuf var2) throws Exception {
      int var3 = findEndOfLine(var2);
      int var4;
      int var5;
      if (!this.discarding) {
         if (var3 >= 0) {
            var5 = var3 - var2.readerIndex();
            int var6 = var2.getByte(var3) == 13 ? 2 : 1;
            if (var5 > this.maxLength) {
               var2.readerIndex(var3 + var6);
               this.fail(var1, var5);
               return null;
            } else {
               ByteBuf var7;
               if (this.stripDelimiter) {
                  var7 = var2.readBytes(var5);
                  var2.skipBytes(var6);
               } else {
                  var7 = var2.readBytes(var5 + var6);
               }

               return var7;
            }
         } else {
            var4 = var2.readableBytes();
            if (var4 > this.maxLength) {
               this.discardedBytes = var4;
               var2.readerIndex(var2.writerIndex());
               this.discarding = true;
               if (this.failFast) {
                  this.fail(var1, "over " + this.discardedBytes);
               }
            }

            return null;
         }
      } else {
         if (var3 >= 0) {
            var4 = this.discardedBytes + var3 - var2.readerIndex();
            var5 = var2.getByte(var3) == 13 ? 2 : 1;
            var2.readerIndex(var3 + var5);
            this.discardedBytes = 0;
            this.discarding = false;
            if (!this.failFast) {
               this.fail(var1, var4);
            }
         } else {
            this.discardedBytes = var2.readableBytes();
            var2.readerIndex(var2.writerIndex());
         }

         return null;
      }
   }

   private void fail(ChannelHandlerContext var1, int var2) {
      this.fail(var1, String.valueOf(var2));
   }

   private void fail(ChannelHandlerContext var1, String var2) {
      var1.fireExceptionCaught(new TooLongFrameException("frame length (" + var2 + ") exceeds the allowed maximum (" + this.maxLength + ')'));
   }

   private static int findEndOfLine(ByteBuf var0) {
      int var1 = var0.writerIndex();

      for(int var2 = var0.readerIndex(); var2 < var1; ++var2) {
         byte var3 = var0.getByte(var2);
         if (var3 == 10) {
            return var2;
         }

         if (var3 == 13 && var2 < var1 - 1 && var0.getByte(var2 + 1) == 10) {
            return var2;
         }
      }

      return -1;
   }
}
