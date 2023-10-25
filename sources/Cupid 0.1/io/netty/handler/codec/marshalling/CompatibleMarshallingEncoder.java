package io.netty.handler.codec.marshalling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.jboss.marshalling.Marshaller;

@Sharable
public class CompatibleMarshallingEncoder extends MessageToByteEncoder<Object> {
  private final MarshallerProvider provider;
  
  public CompatibleMarshallingEncoder(MarshallerProvider provider) {
    this.provider = provider;
  }
  
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    Marshaller marshaller = this.provider.getMarshaller(ctx);
    marshaller.start(new ChannelBufferByteOutput(out));
    marshaller.writeObject(msg);
    marshaller.finish();
    marshaller.close();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\marshalling\CompatibleMarshallingEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */