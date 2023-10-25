package io.netty.handler.codec.marshalling;

import io.netty.channel.ChannelHandlerContext;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

public class DefaultUnmarshallerProvider implements UnmarshallerProvider {
  private final MarshallerFactory factory;
  
  private final MarshallingConfiguration config;
  
  public DefaultUnmarshallerProvider(MarshallerFactory factory, MarshallingConfiguration config) {
    this.factory = factory;
    this.config = config;
  }
  
  public Unmarshaller getUnmarshaller(ChannelHandlerContext ctx) throws Exception {
    return this.factory.createUnmarshaller(this.config);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\marshalling\DefaultUnmarshallerProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */