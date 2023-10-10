package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.jboss.marshalling.Marshaller;

@ChannelHandler.Sharable
public class MarshallingEncoder extends MessageToByteEncoder {
   private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
   private final MarshallerProvider provider;

   public MarshallingEncoder(MarshallerProvider var1) {
      this.provider = var1;
   }

   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception {
      Marshaller var4 = this.provider.getMarshaller(var1);
      int var5 = var3.writerIndex();
      var3.writeBytes(LENGTH_PLACEHOLDER);
      ChannelBufferByteOutput var6 = new ChannelBufferByteOutput(var3);
      var4.start(var6);
      var4.writeObject(var2);
      var4.finish();
      var4.close();
      var3.setInt(var5, var3.writerIndex() - var5 - 4);
   }
}
