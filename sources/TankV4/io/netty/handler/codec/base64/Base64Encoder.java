package io.netty.handler.codec.base64;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.util.List;

@ChannelHandler.Sharable
public class Base64Encoder extends MessageToMessageEncoder {
   private final boolean breakLines;
   private final Base64Dialect dialect;

   public Base64Encoder() {
      this(true);
   }

   public Base64Encoder(boolean var1) {
      this(var1, Base64Dialect.STANDARD);
   }

   public Base64Encoder(boolean var1, Base64Dialect var2) {
      if (var2 == null) {
         throw new NullPointerException("dialect");
      } else {
         this.breakLines = var1;
         this.dialect = var2;
      }
   }

   protected void encode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      var3.add(Base64.encode(var2, var2.readerIndex(), var2.readableBytes(), this.breakLines, this.dialect));
   }

   protected void encode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.encode(var1, (ByteBuf)var2, var3);
   }
}
