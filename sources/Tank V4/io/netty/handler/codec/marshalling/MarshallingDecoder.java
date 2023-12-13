package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.jboss.marshalling.Unmarshaller;

public class MarshallingDecoder extends LengthFieldBasedFrameDecoder {
   private final UnmarshallerProvider provider;

   public MarshallingDecoder(UnmarshallerProvider var1) {
      this(var1, 1048576);
   }

   public MarshallingDecoder(UnmarshallerProvider var1, int var2) {
      super(var2, 0, 4, 0, 4);
      this.provider = var1;
   }

   protected Object decode(ChannelHandlerContext var1, ByteBuf var2) throws Exception {
      ByteBuf var3 = (ByteBuf)super.decode(var1, var2);
      if (var3 == null) {
         return null;
      } else {
         Unmarshaller var4 = this.provider.getUnmarshaller(var1);
         ChannelBufferByteInput var5 = new ChannelBufferByteInput(var3);
         var4.start(var5);
         Object var6 = var4.readObject();
         var4.finish();
         var4.close();
         return var6;
      }
   }

   protected ByteBuf extractFrame(ChannelHandlerContext var1, ByteBuf var2, int var3, int var4) {
      return var2.slice(var3, var4);
   }
}
