package io.netty.handler.codec.string;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.nio.charset.Charset;
import java.util.List;

@ChannelHandler.Sharable
public class StringDecoder extends MessageToMessageDecoder {
   private final Charset charset;

   public StringDecoder() {
      this(Charset.defaultCharset());
   }

   public StringDecoder(Charset var1) {
      if (var1 == null) {
         throw new NullPointerException("charset");
      } else {
         this.charset = var1;
      }
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      var3.add(var2.toString(this.charset));
   }

   protected void decode(ChannelHandlerContext var1, Object var2, List var3) throws Exception {
      this.decode(var1, (ByteBuf)var2, var3);
   }
}
