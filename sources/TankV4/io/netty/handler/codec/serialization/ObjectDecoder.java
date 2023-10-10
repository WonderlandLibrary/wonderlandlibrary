package io.netty.handler.codec.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ObjectDecoder extends LengthFieldBasedFrameDecoder {
   private final ClassResolver classResolver;

   public ObjectDecoder(ClassResolver var1) {
      this(1048576, var1);
   }

   public ObjectDecoder(int var1, ClassResolver var2) {
      super(var1, 0, 4, 0, 4);
      this.classResolver = var2;
   }

   protected Object decode(ChannelHandlerContext var1, ByteBuf var2) throws Exception {
      ByteBuf var3 = (ByteBuf)super.decode(var1, var2);
      return var3 == null ? null : (new CompactObjectInputStream(new ByteBufInputStream(var3), this.classResolver)).readObject();
   }

   protected ByteBuf extractFrame(ChannelHandlerContext var1, ByteBuf var2, int var3, int var4) {
      return var2.slice(var3, var4);
   }
}
