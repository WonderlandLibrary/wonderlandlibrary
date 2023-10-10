package io.netty.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
   private final int frameLength;

   public FixedLengthFrameDecoder(int var1) {
      if (var1 <= 0) {
         throw new IllegalArgumentException("frameLength must be a positive integer: " + var1);
      } else {
         this.frameLength = var1;
      }
   }

   protected final void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      Object var4 = this.decode(var1, var2);
      if (var4 != null) {
         var3.add(var4);
      }

   }

   protected Object decode(ChannelHandlerContext var1, ByteBuf var2) throws Exception {
      return var2.readableBytes() < this.frameLength ? null : var2.readBytes(this.frameLength);
   }
}
