package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.jboss.marshalling.Marshaller;

@ChannelHandler.Sharable
public class CompatibleMarshallingEncoder extends MessageToByteEncoder {
   private final MarshallerProvider provider;

   public CompatibleMarshallingEncoder(MarshallerProvider var1) {
      this.provider = var1;
   }

   protected void encode(ChannelHandlerContext var1, Object var2, ByteBuf var3) throws Exception {
      Marshaller var4 = this.provider.getMarshaller(var1);
      var4.start(new ChannelBufferByteOutput(var3));
      var4.writeObject(var2);
      var4.finish();
      var4.close();
   }
}
