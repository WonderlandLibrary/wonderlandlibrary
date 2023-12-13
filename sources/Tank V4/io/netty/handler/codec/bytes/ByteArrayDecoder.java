package io.netty.handler.codec.bytes;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

public class ByteArrayDecoder extends MessageToMessageDecoder {
   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      byte[] var4 = new byte[var2.readableBytes()];
      var2.getBytes(0, (byte[])var4);
      var3.add(var4);
   }

   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (ByteBuf)var2, var3);
   }
}
