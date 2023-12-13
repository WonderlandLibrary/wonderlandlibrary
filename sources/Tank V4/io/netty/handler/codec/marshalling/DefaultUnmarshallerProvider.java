package io.netty.handler.codec.marshalling;

import io.netty.channel.ChannelHandlerContext;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

public class DefaultUnmarshallerProvider implements UnmarshallerProvider {
   private final MarshallerFactory factory;
   private final MarshallingConfiguration config;

   public DefaultUnmarshallerProvider(MarshallerFactory var1, MarshallingConfiguration var2) {
      this.factory = var1;
      this.config = var2;
   }

   public Unmarshaller getUnmarshaller(ChannelHandlerContext var1) throws Exception {
      return this.factory.createUnmarshaller(this.config);
   }
}
