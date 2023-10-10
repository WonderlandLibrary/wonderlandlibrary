package io.netty.handler.codec.base64;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;

@ChannelHandler.Sharable
public class Base64Decoder extends MessageToMessageDecoder {
   private final Base64Dialect dialect;

   public Base64Decoder() {
      this(Base64Dialect.STANDARD);
   }

   public Base64Decoder(Base64Dialect var1) {
      if (var1 == null) {
         throw new NullPointerException("dialect");
      } else {
         this.dialect = var1;
      }
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      var3.add(Base64.decode(var2, var2.readerIndex(), var2.readableBytes(), this.dialect));
   }

   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (ByteBuf)var2, var3);
   }
}
