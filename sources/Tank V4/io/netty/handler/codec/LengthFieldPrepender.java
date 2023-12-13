package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class LengthFieldPrepender extends MessageToByteEncoder {
   private final int lengthFieldLength;
   private final boolean lengthIncludesLengthFieldLength;
   private final int lengthAdjustment;

   public LengthFieldPrepender(int var1) {
      this(var1, false);
   }

   public LengthFieldPrepender(int var1, boolean var2) {
      this(var1, 0, var2);
   }

   public LengthFieldPrepender(int var1, int var2) {
      this(var1, var2, false);
   }

   public LengthFieldPrepender(int var1, int var2, boolean var3) {
      if (var1 != 1 && var1 != 2 && var1 != 3 && var1 != 4 && var1 != 8) {
         throw new IllegalArgumentException("lengthFieldLength must be either 1, 2, 3, 4, or 8: " + var1);
      } else {
         this.lengthFieldLength = var1;
         this.lengthIncludesLengthFieldLength = var3;
         this.lengthAdjustment = var2;
      }
   }

   protected void encode(ChannelHandlerContext var1, ByteBuf var2, ByteBuf var3) throws Exception {
      int var4 = var2.readableBytes() + this.lengthAdjustment;
      if (this.lengthIncludesLengthFieldLength) {
         var4 += this.lengthFieldLength;
      }

      if (var4 < 0) {
         throw new IllegalArgumentException("Adjusted frame length (" + var4 + ") is less than zero");
      } else {
         switch(this.lengthFieldLength) {
         case 1:
            if (var4 >= 256) {
               throw new IllegalArgumentException("length does not fit into a byte: " + var4);
            }

            var3.writeByte((byte)var4);
            break;
         case 2:
            if (var4 >= 65536) {
               throw new IllegalArgumentException("length does not fit into a short integer: " + var4);
            }

            var3.writeShort((short)var4);
            break;
         case 3:
            if (var4 >= 16777216) {
               throw new IllegalArgumentException("length does not fit into a medium integer: " + var4);
            }

            var3.writeMedium(var4);
            break;
         case 4:
            var3.writeInt(var4);
            break;
         case 5:
         case 6:
         case 7:
         default:
            throw new Error("should not reach here");
         case 8:
            var3.writeLong((long)var4);
         }

         var3.writeBytes(var2, var2.readerIndex(), var2.readableBytes());
      }
   }

   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception {
      this.encode(var1, (ByteBuf)var2, var3);
   }
}
